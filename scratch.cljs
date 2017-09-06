(ns user
  (:require [chord.client :refer [ws-ch]]
            [cljs.core.async :refer [<! >! put! close!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def A (atom 0))

(go
  (let [{:keys [ws-channel error]} (<! (ws-ch "ws://localhost:8081"))]
    (if-not error
      (go
        (loop []
          (let [_ (<! ws-channel)]
            (swap! A inc)
            (recur))))
      (js/console.log "Error:" (pr-str error)))))

(deref A)
