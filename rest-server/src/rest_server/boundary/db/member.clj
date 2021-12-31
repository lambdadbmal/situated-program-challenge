(ns rest-server.boundary.db.member
  (:require [duct.database.sql]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [camel-snake-kebab.core :refer [->kebab-case ->snake_case]]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [clojure.set :as set])
  (:import (duct.database.sql Boundary)))

(defn member-with-id [member]
  (set/rename-keys member {:id :member-id}))

;; protocolを作り、インターフェースの抽象化を図る。(JavaでInterfaceを切る感じかな?)
(defprotocol Members
  (list-members [db])
  (create-member [db member])
  (fetch-member [db member-id])
  (fetch-members [db member-ids])
  )

(extend-protocol Members
  ;; duct.database.sql.Boundaryを拡張するのは、mockやstub化を簡単にするためらしい。
  ;; https://github.com/duct-framework/database.sql
  Boundary
  (list-members [{db :spec}]                                ;; DuctのDB設定のmapからは:specを抜き出して使う。
    (jdbc/query db (sql/format (sql/build :select :* :from :members))))
  (create-member [{db :spec} member]
    (member-with-id
      (first
        (jdbc/insert! db :members (transform-keys ->snake_case member)))))
  (fetch-member [{db :spec} member-id]
    (member-with-id
      (first
        (jdbc/query db
                    (sql/format
                      (sql/build :select :*
                                 :from :members
                                 :where [:= :id member-id])))))
    )
  (fetch-members [db member-ids]
    (member-with-id
      (jdbc/query db
                  (sql/format
                    (sql/build :select :*
                               :from :members
                               :where [:in :id member-ids])))))
  )
