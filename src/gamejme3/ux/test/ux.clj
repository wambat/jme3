(ns gamejme3.ux.test.ux
  (:require [gamejme3.ux.ux-proto :as p]
            [clojure.string :as st]
            [puget.printer :as pp]))

(defn make-ux [io]
  (reify
    p/UXproto
    (draw-board! [this state]
      (pp/cprint "State:" )
      (pp/cprint state))

    (get-input [this state]
      (println "action N param N")
      (let [s (st/split (read-line) #"\s" 2)
            ret {:action (keyword (read-string (first s)))}
            ret (if (> (count s) 1)
                  (assoc ret :action-params (read-string (second s)))
                  ret)]
        ret))))
