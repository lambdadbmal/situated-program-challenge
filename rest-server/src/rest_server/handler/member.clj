(ns rest-server.handler.member
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]
            [rest-server.boundary.db.member :as db.member]))

;; 作成したMembers protocol経由でアクセスする。
(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [_]
    (println "launch member/list")
    ;; (println "db" db)
    [::response/ok (db.member/list-members db)]))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  (fn [{[_ member] :ataraxy/result}]
    (println "launch member/create" member "\n")
    (let [result (db.member/create-member db member)]
      (println "result:" result "\n")
      [::response/ok result]))
  )

(defmethod ig/init-key ::fetch [_ {:keys [db]}]
  (fn [{[_ member-id] :ataraxy/result}]
    (println "launch member/fetch" member-id)
    [::response/ok (db.member/fetch-member db member-id)]))