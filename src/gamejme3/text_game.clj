(ns gamejme3.text-game
  (:require [gamejme3.ux.ux-proto :as p]
            [gamejme3.ux.test.ux :as ux]
            [gamejme3.datamodel.gameloop.hotseat :as hotseat]
            [clojure.pprint :as pp]))
(defn main []
  (println "Start game")
  (let [game-ux (ux/make-ux [*in* *out*])
        state {}]
    (hotseat/main-loop state game-ux)
    ))










