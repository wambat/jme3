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

(def mappings {:pause (KeyTrigger. KeyInput/KEY_SPACE)})
(defn get-mappings []
  mappings
  )

