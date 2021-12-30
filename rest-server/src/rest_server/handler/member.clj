(ns rest-server.handler.member
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]
            [rest-server.boundary.db.member :as db.member]))

;(defmethod ig/init-key ::list [_ {:keys [db]}]
;  (fn [_]
;    (println "launch member/list")
;    [::response/ok]))

;; JDBC経由で生のSQLを文字列で組み立ててクエリーを発行する場合
;(defn- select-members [{db :spec}]
;  (jdbc/query db "SELECT * FROM members"))

;; JDBC経由でHoneySQLで組み立てたSQLを発行する場合
;(defn- select-members2 [{db :spec}]
;  (jdbc/query db (sql/format (sql/build :select :* :from :members))))

;; 作成したMembers protocol経由でアクセスする。
(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [_]
    (println "launch member/list")
    ;; (println "db" db)
    [::response/ok (db.member/list-members db)]))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  (fn [{[_ member] :ataraxy/result}]
    (println "launch member/create" member)
    [::response/ok])
  )

(defmethod ig/init-key ::fetch [_ {:keys [db]}]
  (fn [{[_ member-id] :ataraxy/result}]
    (println "launch member/fetch" member-id)
    [::response/ok]))