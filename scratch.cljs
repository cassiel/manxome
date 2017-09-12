(ns user
  (:require [chord.client :refer [ws-ch]]
            [cljs.core.async :as async :refer [<! >! put! close!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def A (atom nil))
(def CLOSE (atom false))

;; CHORD
(go
  (let [{:keys [ws-channel error]} (<! (ws-ch "ws://localhost:8081"
                                              :format :str))]
    (if-not error
      (go
        (loop []
          (if @CLOSE
            (async/close! ws-channel)
            (when-let [{:keys [message error]} (<! ws-channel)]
              (if error
                (js/console.log "Error: " (pr-str error))
                (do
                  (swap! A conj message)
                  (recur)))))))
      (js/console.log "Error:" (pr-str error)))))

(count (deref A))
(reset! CLOSE true)
(reset! CLOSE false)

(js/console.log (take 20 (deref A)))

;; socket.io (nope: does http://.)
;;(def socket (io.connect "ws://localhost:8081"))

;; Raw!
(def x (js/WebSocket. "ws://localhost:8081"))

(.-protocol x)
(.-readyState x)

(set! (.-onmessage x) (fn [e] (js/console.log (.-data e))))

(.send x "get_scene")

(.close x)
