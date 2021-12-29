(ns rest-server.handler.venue
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]))

(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [{[_ group-id] :ataraxy/result}]
    (println "launch venue/list" group-id)
    [::response/ok]))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  (fn [{[_ group-id venue] :ataraxy/result}]
    (println "launch venue/create" group-id venue)
    [::response/ok])
  )