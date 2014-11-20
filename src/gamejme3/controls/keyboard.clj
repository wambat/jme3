(ns gamejme3.controls.keyboard
  (:import [com.jme3 app.SimpleApplication
            input.KeyInput
            input.controls.KeyTrigger
            input.controls.Trigger
            input.controls.ActionListener
            ])
  (:require [gamejme3.actors.proto :as proto]
            [gamejme3.level-map :as level]
            [gamejme3.actors.wall :as wall])
  (:use clojure.pprint)
  )


(defn make-action-listener [f]
  (reify 
    ActionListener
    (onAction [this name is-pressed tpf]
      (f name is-pressed tpf)
      ))
  )

(def mappings {:pause (KeyTrigger. KeyInput/KEY_SPACE)})
(def actions {:pause (fn [is-pressed tpf] (if (= is-pressed true) (pprint "Pause!")))})

(defn set-bindings [input-manager]
  (.reset input-manager)
  (doseq [i mappings]
    (let [mapping (first i)
          trigger (last i)
          action (get actions mapping)
          fn (fn [n is-pressed tpf]
               (if (= n (name mapping))
                 (action is-pressed tpf)))]
      (pprint (str mapping "|" trigger "|" action))
      (.addMapping input-manager (name mapping) (into-array Trigger [trigger]))
      (.addListener input-manager (make-action-listener fn) (into-array String [(name mapping)]))
      )
    )
  (comment let [keys {:space (KeyTrigger. KeyInput/KEY_SPACE)}]
    (.addMapping input-manager (name :pause) (into-array Trigger [(:space keys)]))
    (.addListener input-manager (make-action-listener inc) (into-array String [(name :pause)]))
    )
  )

