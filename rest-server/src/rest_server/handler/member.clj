(ns rest-server.handler.member
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]))

;(defmethod ig/init-key ::list [_ {:keys [db]}]
;  (fn [_]
;    (println "launch member/list")
;    [::response/ok]))

(defn- select-members [{db :spec}]
  (jdbc/query db "SELECT * FROM members"))

;; まずは直接DBを呼び出してみよう
(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [_]
    (println "launch member/list")
    ;; (println "db" db)
    (let [members (select-members db)]
      [::response/ok members])))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  (fn [{[_ member] :ataraxy/result}]
    (println "launch member/create" member)
    [::response/ok])
  )

(defmethod ig/init-key ::fetch [_ {:keys [db]}]
  (fn [{[_ member-id] :ataraxy/result}]
    (println "launch member/fetch" member-id)
    [::response/ok]))