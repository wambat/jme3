(ns gamejme3.game-state
  (:import [com.jme3 app.SimpleApplication
            input.KeyInput
            input.controls.KeyTrigger
            input.controls.Trigger
            input.controls.ActionListener
            ])
  (:require [gamejme3.level-map :as level]
            [gamejme3.ops.reset-map :as reset-map]
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
  (fn [entry _ _] (:op entry)))

(defmethod process-op :reset-map [{value :value} scene-pivot asset-manager]
  (reset-map/reset-map value scene-pivot asset-manager)
  )

(defmethod process-op :default [val _ _]
  (throw (IllegalArgumentException. 
          (str "Unknown op " (:op val)))))
