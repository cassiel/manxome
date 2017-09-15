(ns net.cassiel.manxome.core
  (:require [com.stuartsierra.component :as component]
            [net.cassiel.manxome.components.socket :as socket]))

(enable-console-print!)

(defn system []
  (component/system-map :socket (socket/map->SOCKET {})))

(defonce S (atom (system)))

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Hello world!"}))

(defn start []
  (swap! S component/start))

(defn stop []
  (swap! S component/stop))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  (stop)
  (start))

(stop)
(start)
