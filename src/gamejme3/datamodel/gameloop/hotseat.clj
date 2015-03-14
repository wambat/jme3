(ns gamejme3.datamodel.gameloop.hotseat
  (:require [gamejme3.datamodel.actors.proto :refer :all]
            [reduce-fsm :as fsm]))

(defn on-end-turn [val & _]
  (update-in val [:hand] #(if (= % :red)
                            :blue
                            :red)))

(fsm/defsm command-fsm
  [[:make-command
    [{:action :end-turn}] -> {:action on-end-turn} :end-command
    [{:action :unit-selected}]  -> :select-action]

   [:select-action
    [{:action :move-selected}] -> :select-move-target
    [{:action :attack-selected}] -> :select-attack-target
   ; [{:action :wait}] -> {:action on-wait} :make-command
   ; [{:action :mode-changed}] -> {:action on-mode-changed} :select-action
    ]

   [:select-attack-target
    [{:action :target-canceled}] -> :select-action
   ; [{:action :target-selected}] -> {:action on-attack-target-selected} :make-command
    ]
   
   [:select-move-target
   ; [{:action :target-selected}] -> {:action on-move-target-selected} :select-action
    [{:action :target-canceled}] -> :select-action]
   [:end-command {:is-terminal true}]])

(defn rep-ls [state ux]
  (.draw-board! ux state)
  (cons state (lazy-seq 
               (rep-ls (.get-input ux state) ux))))

(defn main-loop [initial-state ux]
  (doall (take 5 (command-fsm (rep-ls initial-state ux)))))



















