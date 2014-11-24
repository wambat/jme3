(ns gamejme3.level-map
  (:import [])
  (:use clojure.pprint)
  )

(def types #{:fire :ice :wall :player})
(declare walls-fn ground-fn add-peasants-fn)
(defn create-level [side]
  (let [dim-x side
        dim-y side
        dim-z side
        level {:dim-x side
               :dim-y side
               :dim-z side
               :map-fn (fn [x y z] (some #(% x y z dim-x dim-y dim-z) [ground-fn add-peasants-fn]))}]
    level
    )
  )

(defn add-peasants-fn [x y z dim-x dim-y dim-z ]
  (if (and (= z 0)
           (= y 1)
           (= x (quot dim-x 2)))
    :peasant
    nil))

(defn ground-fn [x y z dim-x dim-y dim-z ]
  (if (= y 0)
    :wall
    nil))

(defn walls-fn [x y z dim-x dim-y dim-z]
  "creates box around space"
  (if (or (= x 0)
          (= y 0)
          (= z 0)
          (= x (dec dim-x))
          (= y (dec dim-y))
          (= z (dec dim-z))) 
    :wall
    nil)
  )
