(ns gamejme3.actors.wall
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
  (:require [gamejme3.actors.proto :as p]
             [clojure.pprint :as pp])
  )

(defn make-test-cube [x y z color side asset]
  (pp/pprint (str x ))
  (pp/pprint (str y ))
  (pp/pprint (str z ))
  (pp/pprint (str side ))
  (pp/pprint (str asset))
  (Box. (Vector3f. x y z) side side side)
  (let [b1 (Box. (Vector3f. x y z) side side side)
        b1geom (Geometry. "Box" b1)
        b1mat (Material. asset
                         "Common/MatDefs/Misc/Unshaded.j3md")
        ]
    (.setColor b1mat "Color" color)
    (.setBlendMode (.getAdditionalRenderState  b1mat) RenderState$BlendMode/Alpha ) 
    (.setQueueBucket b1geom RenderQueue$Bucket/Transparent)
    (.setMaterial b1geom b1mat)
    b1geom
    )
  )

(defrecord Wall [params]
  p/IActor
  (model [this asset]
    (let [color (ColorRGBA. (rand) (rand) (rand) 0.8)
          side 0.4
          params (:params this)]
      (pp/pprint params )
      (make-test-cube (:x params) (:y params) (:z params) color side asset)
      )
    )
  (get-available-actions [this state] 
    [:idle]
    )
  (get-animation-fn-for-action [this action]
    (comment case action
      :idle (fn [scene skeleton mesh to-point]
              (let [anim (BABYLON.Animation. "protanim" "position" 30 BABYLON.Animation.ANIMATIONTYPE_VECTOR3 )
                    posmesh (.-position mesh)
                    frames  [{:frame 0 :value posmesh}
                             {:frame 300 :value to-point}]
                    product (BABYLON.Vector3.Cross. posprot postarget)]
                (.setKeys anim (clj->js frames))
                (.push (.-animations mesh) anim)
                (.lookAt @prot to_point 0 0 0)
                (.beginAnimation scene mesh 0 300 false)
                )
              (.beginAnimation scene skeleton 0 300 false 1)
              )
      :run (fn [scene skeleton target]
              (.beginAnimation scene skeleton 0 300 false 3)
              )
      :idle (fn [scene skeleton]
              (.beginAnimation scene skeleton 0 10 true 3)
              )
      )

    )
  p/IMovable
  (move! [this scene actor x y]
    ;(.beginAnimation scene (.-skeleton actor) 0 100 true 0.8)
    )
  )
