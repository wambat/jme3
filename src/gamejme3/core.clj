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
  (:require [gamejme3.game :as game]
            [gamejme3.text-game :as tgame])
  (:use clojure.pprint))

 
(defonce ^:dynamic *app-settings* (doto (AppSettings. true)
                                (.setFullscreen false)
                                (.setTitle "jm-tut1")))

(def speed 5)

(def app (proxy [SimpleApplication] []
           (simpleInitApp []
             (org.lwjgl.input.Mouse/setGrabbed false)
             (game/init this))

           (simpleUpdate [tpf]
             (game/update this tpf))))


(defn stop []
  (doto app
    (.stop)))

(defn launch-3d-app []
  (doto app
    (.setShowSettings false)
    (.setPauseOnLostFocus false)
    (.setSettings *app-settings*)
    (.start)))

(defn -main [& args]
  ;(launch-3d-app)
  (tgame/main)
  )
