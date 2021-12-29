(ns rest-server.handler.group
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]))

(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [{[_] :ataraxy/result}]
    (println "launch group/list")
    [::response/ok]))

(defmethod ig/init-key ::join [_ {:keys [db]}]
  (fn [{[_ member-id event-id member] :ataraxy/result}]
    (println "launch group/join" member-id event-id member)
    [::response/ok]))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  (fn [{[_ group] :ataraxy/result}]
    (println "launch group/create" group)
    [::response/ok])
  )

;(defmethod ig/init-key ::join [_ {:keys [db]}]
;  (fn [request]
;    (println "launch group/join" request)
;    [::response/ok]))