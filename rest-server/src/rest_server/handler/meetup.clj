(ns rest-server.handler.meetup
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]))

(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [{[_ group-id] :ataraxy/result}]
    (println "launch meetup/list" group-id)
    [::response/ok]))

(defmethod ig/init-key ::join [_ {:keys [db]}]
  (fn [{[_ member-id event-id] :ataraxy/result}]
    (println "launch meetup/join" member-id event-id)
    [::response/ok]))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  (fn [{[_ group-id meetup] :ataraxy/result}]
    (println "launch meetup/create" group-id meetup)
    [::response/ok])
  )

(defmethod ig/init-key ::fetch [_ {:keys [db]}]
  (fn [{[_ group-id event-id] :ataraxy/result}]
    (println "launch meetup/fetch" group-id event-id)
    [::response/ok]))

;(defmethod ig/init-key ::join [_ {:keys [db]}]
;  (fn [request]
;    (println "launch meetup/join" request)
;    [::response/ok]))