(ns gamejme3.datamodel.actors.proto
  (:require []))

(defn pack-attack-damage
  [unit-state world-state]
  (* (-> unit-state :params :actions :attack :basedamage) 
     (:state :units unit-state)))

(defn add-mode-damage-mult [mode mult base-damage-fn unit-state world-state]
  (if (= (-> unit-state :state :mode) mode)
    (* (base-damage-fn unit-state world-state) mult)
    (base-damage-fn unit-state world-state)))

(defn take-pack-damage
  [unit-state base-damage world-state]
  (update-in unit-state [:state :destructable :health] - (quot base-damage (-> unit-state :params :destructable :health))))

(defn stands?
  [type unit-state world-state]
  true)

(def all-vulns [:ground :air :navy])


















