(ns gamejme3.datamodel.actors.infantry
  (:require [gamejme3.datamodel.actors.proto :refer :all]))

(def unit
  {:view "I"
   :stackable {:max 10}
   :destructable {:health 1
                  :vulnerable all-vulns
                  :take-damage take-pack-damage}
   :actions {:move {:points 6
                    :terrains {:planes 2
                               :woods 2
                               :hills 3
                               :roads 1}}
             :attack {:basedamage 1
                      :range 0
                      :targets :ground
                      :damage (partial add-mode-damage-mult :burrowed 1.5 pack-attack-damage)}
             :modes {:borrowed {:can? (partial stands? :ground)}}}})
