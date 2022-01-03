(ns rest-server.handler.member
  (:require [ataraxy.response :as response]
            [clojure.set :as set]
            [integrant.core :as ig]
            [rest-server.boundary.db.member :as db.member]))

(defn member-with-id [member]
  (set/rename-keys member {:id :member-id}))

;; 作成したMembers protocol経由でアクセスする。
(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [_]
    (println "launch member/list")
    [::response/ok (db.member/list-members db)]))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  ;; TODO: 何らかの要因でdb.*/create-*が失敗した場合のエラー処理をするべき。
  (fn [{[_ member] :ataraxy/result}]
    (println "launch member/create" member)
    (let [id (db.member/create-member db member)]
      ;; insertの結果得たid値と、APIへの入力値とを結合して返値としている。
      ;; 厳密にDBにinsertした時点の結果を返したい場合は、id値を使ってDBからデータを取り直す必要がある。
      [::response/ok (-> member
                         (assoc :id id)
                         member-with-id)]))
  )

(defmethod ig/init-key ::fetch [_ {:keys [db]}]
  (fn [{[_ member-id] :ataraxy/result}]
    (println "launch member/fetch" member-id)
    [::response/ok (db.member/fetch-member db member-id)]))