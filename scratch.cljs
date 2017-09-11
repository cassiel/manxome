(ns user
  (:require [chord.client :refer [ws-ch]]
            [taoensso.sente  :as sente :refer (cb-success?)]
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

;; SENTE

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/" :host "localhost:8081" ; Note the same path as before
       {:type :auto ; e/o #{:auto :ajax :ws}
       })]
  (def chsk       chsk)
  (def ch-chsk    ch-recv) ; ChannelSocket's receive channel
  (def chsk-send! send-fn) ; ChannelSocket's send API fn
  (def chsk-state state)   ; Watchable, read-only atom
  )

(go
  (loop []
    (if @CLOSE
      (async/close! ch-chsk)
      (when-let [{:keys [message error]} (<! ch-chsk)]
        (if error
          (js/console.log "Error: " (pr-str error))
          (do
            (swap! A conj message)
            (recur)))))))


(count (deref A))
(reset! CLOSE true)
(reset! CLOSE false)


(js/console.log (take 20 (deref A)))
