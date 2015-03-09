(ns gamejme3.game-state
  (:import 
   datomic.Util
   [com.jme3 app.SimpleApplication
    input.KeyInput
    input.controls.KeyTrigger
    input.controls.Trigger
    input.controls.ActionListener
    cinematic.events.AnimationEvent
    cinematic.events.AnimationEvent
    animation.AnimControl
    animation.AnimationFactory
    animation.LoopMode])
  (:require 
   [datomic.api :as d]
   [clojure.java.io :as io]
   [clojure.edn :as edn]
   [clojure.core.async :refer [mult go tap untap chan alt! >! <! filter> onto-chan pipe close!] :as async]
   [gamejme3.level-map :as level]
   [gamejme3.ops.reset-map :as reset-map]
   [gamejme3.actors.proto :as proto]
   [clojure.pprint :refer :all]
   [gamejme3.actors.wall]
   [gamejme3.actors.peasant]))

(defn transact-all
  "Load and run all transactions from f, where f is any
   resource that can be opened by io/reader."
  [conn f]
  (loop [n 0
         [tx & more] f]
    (if tx
      (recur (+ n (count (:tx-data  @(d/transact conn tx))))
             more)
      {:datoms n})))

(def db-schema (->> "schema.edn"
                    io/resource
                    io/reader
                    Util/readAll))

(def db-seed (->> "seed.edn"
                    io/resource
                    io/reader
                    Util/readAll))

(def db-uri "datomic:dev://localhost:4334/game0")
(def db-uri "datomic:mem://game")

(d/delete-database db-uri)
(d/create-database db-uri)

(def db-conn (d/connect db-uri))

(transact-all db-conn db-schema)

(def tx-chan (chan))
(def tx-chan-bcast (mult tx-chan))
;;Setting up TX watching thread
(async/thread
  (try
    (let [queue (d/tx-report-queue db-conn)]
      (while true
        (let [report (.take queue)]
          ;(println "TX:")
          ;(pprint report)
          (async/>!! tx-chan report))))
    (catch Exception e
          (pprint e)
          (throw e))))

(async/thread
  (let [tap-ch (tap tx-chan-bcast (chan))]
    (while true
      (let [tx (async/<!! tap-ch)]
        (pprint tx)
        (pprint (d/q '[:find ?e ?tx
                       :in $db $tx
                       :where 
                       [$tx ?e ?a ?v ?tx]
                       [(= ?dbv :spatial/type)]
                       [$db ?a :db/ident ?dbv]
                       ] (:db-before tx) (:tx-data tx))) 
        ))))

(transact-all db-conn db-seed)


(def db-with-schema (d/db db-conn))



(def current-board (atom {}))
(def example-state 
  [{:op :reset-map 
    :value (level/substanciate-map (level/create-level 5))}

   {:op :walk
    :value {:id :peasant
            :path [{:x 1 :y 1 :z 3}
                   {:x 1 :y 1 :z 2}
                   {:x 2 :y 1 :z 2}]}}

   {:op :jump
    :value {:id :peasant
            :position {:x 3 :y 1 :z 3}}}

   {:op :attack
    :value {:id :peasant}}])


(defmulti process-op 
  (fn [entry _ _ _] (:op entry)))

(defmethod process-op :reset-map [{value :value} scene-pivot asset-manager _]
  (reset-map/reset-map value scene-pivot asset-manager)
  )

(defmethod process-op :walk [{value :value} scene-pivot asset-manager cinematic]
  (let [id (-> value :id name)
        path (:path value)
        node (.getChild scene-pivot id)
        control (.getControl node AnimControl)
        channel (.createChannel control)
        ]
    (pprint "Playing cinematic")
    (pprint path)
    (.clearChannels control)
    
    (doseq [[index point] (map-indexed vector path)]
      (let [anim-event (AnimationEvent. node "Walk" 3)
            anim-factory (AnimationFactory. 0.7 (str "move" index))]
        (pprint "Playing step")
        (pprint index)
        (pprint point)
        (.enqueueCinematicEvent cinematic anim-event)
        )
      )
    (.fitDuration cinematic)
    (.setSpeed cinematic 1.0)
    (.setLoopMode cinematic LoopMode/DontLoop)
    (.play cinematic)
    )
  )

(defmethod process-op :default [val _ _ _]
  (throw (IllegalArgumentException. 
          (str "Unknown op " (:op val)))))
