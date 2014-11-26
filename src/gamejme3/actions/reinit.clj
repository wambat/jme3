(ns gamejme3.actions.reinit
  (:import [com.jme3 app.SimpleApplication
            input.KeyInput
            input.controls.KeyTrigger
            input.controls.Trigger
            input.controls.ActionListener
            ])
  (:use clojure.pprint)
  )

(def reinit-state (atom false))

(defn get-actions []
  {:reinit (fn [is-pressed tpf] (if (= is-pressed true) (reset! reinit-state true)))}
  )

