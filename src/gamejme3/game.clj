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
            math.Quaternion
            math.ColorRGBA
            animation.AnimationFactory
            animation.LoopMode
            cinematic.Cinematic
            cinematic.MotionPath
            cinematic.PlayState
            cinematic.events.AnimationEvent
            cinematic.events.MotionEvent
            ])
  (:require 
   [gamejme3.level-map :as level]
   [gamejme3.actions :as actions]
   [gamejme3.game-state :as state]
   [gamejme3.actions.pause :as actions.pause]
   [gamejme3.actions.reinit :as actions.reinit]
   )
  (:use clojure.pprint)
  )

(defonce desktop-cfg (.getResource (.getContextClassLoader (Thread/currentThread))
                   "com/jme3/asset/Desktop.cfg"))
(defonce assetManager (JmeSystem/newAssetManager desktop-cfg))


(defn create-figure [length side]
  (loop [position {:x 0 :y 0 :z 0}
         length length
         result []]
    (if (< length 1)
      (conj result position)
      (recur (update-in position [(get [:x :y :z] (rand-int 3))] #(+ % side))
             (dec length)
             (conj result position)))))


(comment defn create-figure-set [side]
  (comment map 
   #(create-figure % side) 
   (range 1 (+ count 1)))
  (level/substanciate-map (level/create-level 5))
  )

(defn make-sky []
  (SkyFactory/createSky assetManager "Textures/Sky/Bright/BrightSky.dds" false)
  )


(defn set-camera [cam]
;Camera Position: (4.8882446, -1.0686046, 6.6186147)
;Camera Rotation: (0.03566155, 0.9640372, 0.17352314, -0.19811681)
;Camera Direction: (-0.36960772, 0.3486958, -0.8612788)
  (let [rot (Quaternion. 0.03566155, 0.9640372, 0.17352314, -0.19811681)
        loc (Vector3f. 4.8882446, -1.0686046, 6.6186147)]

    (.setLocation cam loc)
    (.setRotation cam rot)
    )
  )

(defn init [app]
  (let [l1 (DirectionalLight.)
        pivot (Node. "pivot")
        cinematic (Cinematic. (.getRootNode app))
        ]
    (.attach (.getStateManager app) cinematic)
    (.setColor l1 (ColorRGBA/Red))
    (.setDirection l1 (.normalizeLocal (Vector3f. 1 0 -2)))
    (.detachAllChildren (.getRootNode app)) 
    (set-camera (.getCamera app))
    (actions/set-bindings (.getInputManager app))
    (state/process-op (first state/example-state) pivot assetManager cinematic)
    ;(state/process-op (second state/example-state) pivot assetManager cinematic)
    

    (doto (.getRootNode app) 
      (.attachChild (make-sky))
      (.addLight l1)
      (.attachChild pivot))
    (.rotate pivot 0.4 0.4 0)))


(defn update [app tpf]
  (if @actions.reinit/reinit-state
    (do (reset! actions.reinit/reinit-state false)
        (init app))
    )
  (if (not @actions.pause/pause-state)
    (let [subj (.getChild (.getRootNode app) "pivot") ] 
      (.rotate subj 0 (* 0.3 tpf) 0)))
  )
