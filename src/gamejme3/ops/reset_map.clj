(ns gamejme3.ops.reset-map
  (:import [com.jme3 app.SimpleApplication
            ])
  (:require 
   [gamejme3.actors.proto :as proto]
   [gamejme3.actors.wall]
   [gamejme3.actors.peasant]
   )
  (:use clojure.pprint)
  )

(defn reset-map [m scene-pivot asset-manager]
  (.detachAllChildren scene-pivot)
  (doseq [mapitem m]
    (if (:type mapitem)
      (let [fname (name (:type mapitem))
            protsym (symbol (str "->" (clojure.string/capitalize (name (:type mapitem)))))
            realization (ns-resolve (symbol (str "gamejme3.actors." fname)) protsym)
            i (realization mapitem)
            ]
        (doto scene-pivot 
          (.attachChild (proto/model i asset-manager))
          )
        ))
    )
  )
