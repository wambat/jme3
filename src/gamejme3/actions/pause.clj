(ns gamejme3.actions.pause
  (:import [com.jme3 app.SimpleApplication
            input.KeyInput
            input.controls.KeyTrigger
            input.controls.Trigger
            input.controls.ActionListener
            ])
  (:use clojure.pprint)
  )

(def pause-state (atom true))

(defn get-actions []
  {:pause (fn [is-pressed tpf] (if (= is-pressed true) (swap! pause-state not)))}
  )

