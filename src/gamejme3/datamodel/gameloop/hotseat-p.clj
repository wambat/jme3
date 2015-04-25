(ns gamejme3.datamodel.gameloop.hotseat
  (:require [gamejme3.datamodel.actors.proto :refer :all]
            [reduce-fsm :as fsm]))

(defn change-hand [state]
  (update-in state [:hand] #(if (= % :red)
                              :blue
                              :red)))
(defn inc-turn [st]
  (update-in st [:count] inc))


(defn rep-ls [state ux]
  (.draw-board! ux state)
  (cons state (lazy-seq 
               (rep-ls (.get-input ux state) ux))))

(defn main-loop [initial-state ux]
  (loop [state initial-state]
    (.draw-board! ux state)
    (let [state (.get-input ux state)]
      (condp = (:action state)
        :end-turn
        (recur (-> state inc-turn change-hand))
        state))))



















