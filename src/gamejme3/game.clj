(ns gamejme3.game
  (:import [com.jme3 app.SimpleApplication
            material.Material
            material.RenderState
            material.RenderState$BlendMode
            light.DirectionalLight
            scene.Geometry
            system.AppSettings
            system.JmeSystem
            util.SkyFactory
            renderer.queue.RenderQueue$Bucket
            scene.shape.Box
            scene.Node
            math.Vector3f
            math.ColorRGBA])
  (:require [gamejme3.actors.proto :as proto]
            [gamejme3.level-map :as level]
            [gamejme3.actions :as actions]
            [gamejme3.actions.pause :as actions.pause]
            [gamejme3.actors.wall]
            [gamejme3.actors.peasant]
            )
  (:use clojure.pprint)
  )

(def desktop-cfg (.getResource (.getContextClassLoader (Thread/currentThread))
                   "com/jme3/asset/Desktop.cfg"))
(def assetManager (JmeSystem/newAssetManager desktop-cfg))


(defn create-figure [length side]
  (loop [position {:x 0 :y 0 :z 0}
         length length
         result []]
    (if (< length 1)
      (conj result position)
      (recur (update-in position [(get [:x :y :z] (rand-int 3))] #(+ % side))
             (dec length)
             (conj result position)))))

(defn create-map [level]
  (for [x (range (:dim-x level))
        y (range (:dim-y level))
        z (range (:dim-z level))
        ]
    (let [type-fn (:map-fn level)]
      {:position {:x x :y y :z z} :type (type-fn x y z)}
      )
    )
  )

(defn create-figure-set [side]
  (comment map 
   #(create-figure % side) 
   (range 1 (+ count 1)))
  (create-map (level/create-level 5))
  )

(defn make-sky []
  (SkyFactory/createSky assetManager "Textures/Sky/Bright/BrightSky.dds" false)
  )


(defn init [app]
  (let [l1 (DirectionalLight.)
        pivot (Node. "pivot")
        ]
    (.setColor l1 (ColorRGBA/Blue))
    (.setDirection l1 (.normalizeLocal (Vector3f. 1 0 -2)))
    (.detachAllChildren (.getRootNode app)) 
    (actions/set-bindings (.getInputManager app))
    (doseq [figure (create-map (level/create-level 5))]
      (if (:type figure)
        (let [fname (name (:type figure))
              protsym (symbol (str "->" (clojure.string/capitalize (name (:type figure)))))
              realization (ns-resolve (symbol (str "gamejme3.actors." fname)) protsym)
              i (realization (:position figure))
              ]
                                        ;(.attachChild (make-test-cube (:x figure) (:y figure) (:z figure)  0.5))
          (doto pivot 
            (.attachChild (proto/model i assetManager))
            )
          ))
      )
    

    (doto (.getRootNode app) 
      (.attachChild (make-sky))
      (.addLight l1)
      (.attachChild pivot))
    (.rotate pivot 0.4 0.4 0)))

(defn add-pause-action []
  
  )

(defn update [app tpf]
  (if (not @actions.pause/pause-state)
    (let [subj (.getChild (.getRootNode app) "pivot") ] 
      (.rotate subj 0 (* 0.1 tpf) 0)))
  )
