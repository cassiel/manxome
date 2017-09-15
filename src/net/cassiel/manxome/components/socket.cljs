(ns net.cassiel.manxome.components.socket
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.lifecycle :refer [stopping starting]]
            [oops.core :as o]
            [cljs.core.async :as async :refer [<! >!]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(defrecord SOCKET [socket chan installed?]
  Object
  (toString [this] "SOCKET")

  component/Lifecycle
  (start [this]
    (starting this
              :on installed?
              :action #(let [s (js/WebSocket. "ws://localhost:8081")
                             ch (async/chan)]
                         (set! (.-onmessage s) (fn [e] (js/console.log (.-data e))))
                         (assoc this
                                :socket s
                                :chan ch
                                :installed? true))))

  (stop [this]
    (stopping this
              :on installed?
              :action #(do
                         (async/close! chan)
                         (.close socket)
                         (assoc this
                                :socket nil
                                :chan nil
                                :installed? false)))))
