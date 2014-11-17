(ns gamejme3.actors.proto)

(defprotocol IActor 
  (model [this asset] "Get model")
  (get-available-actions [this state] "Get actions that can be called")
  (get-animation-fn-for-action [this action] "Get animation function for certain action")
  )

(defprotocol IMovable
  (move! [this scene actor x y] "Move to position")
  )
