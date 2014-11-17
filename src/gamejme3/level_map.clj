(ns gamejme3.level-map
  (:import [])
  (:use clojure.pprint)
  )

(def types #{:fire :ice :block :player})
(declare walls-fn)
(defn create-level [side]
  (let [dim-x side
        dim-y side
        dim-z side
        level {:dim-x side
             :dim-y side
             :dim-z side
             :map-fn (fn [x y z] (walls-fn x y z dim-x dim-y dim-z (fn [_ _ _] nil)))}]
     level
    )
  )

(defn walls-fn [x y z dim-x dim-y dim-z blocks-fn]
  (if (or (= x 0)
          (= y 0)
          (= z 0)
          (= x dim-x)
          (= y dim-y)
          (= z dim-z)) 
    :block
    (blocks-fn x y z))
  )
