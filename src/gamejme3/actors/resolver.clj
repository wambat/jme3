(ns gamejme3.actors.resolver
  (:import [com.jme3 app.SimpleApplication
            ])
  (:require 
   [gamejme3.actors.proto :as proto]
   [gamejme3.actors.wall]
   [gamejme3.actors.peasant]
   )
  (:use clojure.pprint)
  )

(defn resolve-realization [fname]
  (let [
        protsym (symbol (str "->" (clojure.string/capitalize fname)))
        realization (ns-resolve (symbol (str "gamejme3.actors." fname)) protsym)
        ]
    realization
  ))
