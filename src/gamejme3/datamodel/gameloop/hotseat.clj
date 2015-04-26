(ns gamejme3.datamodel.gameloop.hotseat
  (:require [gamejme3.datamodel.actors.proto :refer :all]
            [automat.core :as a]
            [puget.printer :as pp]
            [automat.viz :refer (view)]))

(defn inc-turn [st]
  (update-in st [:turn] inc))

(defn on-end-turn [state _]
  (-> state 
      (update-in [:hand] #(if (= % :red)
                            :blue
                            :red))
      inc-turn))

(defn on-new-state [_ _]
  {:hand :red
   :turn 0})

(defn on-make-move [state action]
  (-> state 
      (assoc :action (:action action))
      (assoc :last-action-params (:action-params action))))


(def fsm (a/compile [[:start (a/$ :on-start)] 
                     (a/* (a/or [:move (a/$ :on-make-move)]
                                [:turn (a/$ :on-end-turn)]))
                     :quit]
                    {:signal :action
                     :reducers {:on-start on-new-state
                                :on-make-move on-make-move
                                :on-end-turn on-end-turn}}))

(def fsm-adv (partial a/advance fsm))

(defn main-loop [initial-state ux]
  (loop [state initial-state]
    (.draw-board! ux (:value state))
    (let [command (.get-input ux state)
          _ (pp/cprint command)
          new-state (fsm-adv state command)]
      (if (:accepted? new-state) 
        (do 
          (.draw-board! ux (:value state))
          new-state)
        (recur new-state)))))
