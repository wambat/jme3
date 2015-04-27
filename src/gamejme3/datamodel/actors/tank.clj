(ns gamejme3.datamodel.actors.tank
  (:require [gamejme3.datamodel.actors.proto :refer :all]))

(def unit
  {:view "T"
   :description "Tank"
   :stackable {:max 5}
   :destructable {:health 5
                  :vulnerable [:ground]}
   :moveable {:points 10
              :terrains {:planes 2
                         :roads 1}}
   :attacker {:basedamage 5
              :range 0
              :targets :ground}})
