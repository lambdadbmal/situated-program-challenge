(ns rest-server.handler.member
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]))

(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [_]
    (println "launch member/list")
    [::response/ok]))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  (fn [{[_ member] :ataraxy/result}]
    (println "launch member/create")
    [::response/ok])
  )

(defmethod ig/init-key ::fetch [_ {:keys [db]}]
  (fn [{[_ member-id] :ataraxy/result}]
    (println "launch member/fetch" member-id)
    [::response/ok]))