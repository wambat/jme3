(ns gamejme3.ux.test.ux
  (:require [gamejme3.ux.ux-proto :as p]
            [puget.printer :as pp]))

(defn make-ux [io]
  (reify
    p/UXproto
    (draw-board! [this state]
      (pp/cprint "State:" )
      (pp/cprint state))

    (get-input [this state]
      (println "action N param N")
      (-> state 
          (assoc-in [:action] (read-string (read-line)))
          (assoc-in [:action-params] (read-string (read-line)))))))
