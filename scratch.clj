(ns user
  (:require [clojure.core.async :refer [<! >! put! close! go]]
            [taoensso.sente  :as sente]))

(def x (sente/make-channel-socket-client! "ws://localhost:8081" {:type :ws}))
