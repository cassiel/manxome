(ns ^:figwheel-always net.cassiel.server.core
  (:require [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(println "Hello from the Node!")

(def -main (fn [] nil))
(set! *main-cli-fn* -main) ;; this is required
