(ns gamejme3.ux.ux-proto)

(defprotocol UXproto 
  (draw-board! [this state] "Draw a state")
  (get-input [this state] "Get a command"))
