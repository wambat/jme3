(ns gamejme3.game-state
  (:import [com.jme3 app.SimpleApplication
            input.KeyInput
            input.controls.KeyTrigger
            input.controls.Trigger
            input.controls.ActionListener
            cinematic.events.AnimationEvent
            cinematic.events.AnimationEvent
            animation.AnimControl
            animation.AnimationFactory
            animation.LoopMode
            ])
  (:require [gamejme3.level-map :as level]
            [gamejme3.ops.reset-map :as reset-map]
            [gamejme3.actors.proto :as proto]
            [gamejme3.actors.wall]
            [gamejme3.actors.peasant]
            )
  (:use clojure.pprint)
  )

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
      (let [anim-event (AnimationEvent. node "Walk" 3 )
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
