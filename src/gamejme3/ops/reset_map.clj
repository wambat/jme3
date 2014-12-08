(ns gamejme3.ops.reset-map
  (:import [com.jme3 app.SimpleApplication
            ])
  (:require 
   [gamejme3.actors.resolver :as res]
   [gamejme3.actors.proto :as proto]
   )
  (:use clojure.pprint)
  )

(defn reset-map [m scene-pivot asset-manager]
  (.detachAllChildren scene-pivot)
  (doseq [mapitem m]
    (if (:type mapitem)
      (let [fname (name (:type mapitem))
            realization (res/resolve-realization fname)
            i (realization mapitem)
            ]
        (doto scene-pivot 
          (.attachChild (proto/model i asset-manager))
          )
        ))
    )
  )
