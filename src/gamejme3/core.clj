(ns gamejme3.core
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
  (:use clojure.pprint)
  )
(def desktop-cfg (.getResource (.getContextClassLoader (Thread/currentThread))
                   "com/jme3/asset/Desktop.cfg"))

(def assetManager (JmeSystem/newAssetManager desktop-cfg))
 
(def ^:dynamic *app-settings* (doto (AppSettings. true)
                                (.setFullscreen false)
                                (.setTitle "jm-tut1")))

(def speed 5)

(defn create-figure [length side]
  (loop [position {:x 0 :y 0 :z 0}
         length length
         result []]
    (if (< length 1)
      (conj result position)
      (recur (update-in position [(get [:x :y :z] (rand-int 3))] #(+ % side))
             (dec length)
             (conj result position)))))

(defn create-figure-set [count side]
  (map 
   #(create-figure % side) 
   (range 1 (+ count 1))))

(defn make-sky []
  (SkyFactory/createSky assetManager "Textures/Sky/Bright/BrightSky.dds" false)
  )
(defn make-test-cube [x y z color side]
  (let [b1 (Box. (Vector3f. x y z) side side side)
        b1geom (Geometry. "Box" b1)
        b1mat (Material. assetManager
                         "Common/MatDefs/Misc/Unshaded.j3md")
        ]
    (.setColor b1mat "Color" color)
    (.setBlendMode (.getAdditionalRenderState  b1mat) RenderState$BlendMode/Alpha ) 
    (.setQueueBucket b1geom RenderQueue$Bucket/Transparent)
    (.setMaterial b1geom b1mat)
    b1geom
    )
  )

(defn init [app]
  (let [l1 (DirectionalLight.)
        pivot (Node. "pivot")
        ]
    (org.lwjgl.input.Mouse/setGrabbed false)
    (.setColor l1 (ColorRGBA/Blue))
    (.setDirection l1 (.normalizeLocal (Vector3f. 1 0 -2)))
    (.detachAllChildren (.getRootNode app)) 
    (doseq [figure (create-figure 5 1)]
      (pprint figure)
      (doto pivot 
        (.attachChild (make-test-cube (:x figure) (:y figure) (:z figure) (ColorRGBA. (rand) (rand) (rand) 0.8) 0.5))))
    

    (doto (.getRootNode app) 
      (.attachChild (make-sky))
      (.addLight l1)
      (.attachChild pivot))
    (.rotate pivot 0.4 0.4 0)))

(declare update)
(def app (proxy [SimpleApplication] []
             (simpleInitApp []
               (init this))
             (simpleUpdate [tpf]
               (update this tpf))
))

(defn update [app tpf]
  (let [subj (.getChild (.getRootNode app) "pivot") ] 
    (.rotate subj 0 (* 2 tpf) 0))
  )

(defn stop []
  (doto app
    (.stop))
  )
(defn -main [& args]
  (doto app
    (.setShowSettings false)
    (.setPauseOnLostFocus false)
    (.setSettings *app-settings*)
    (.start)))
