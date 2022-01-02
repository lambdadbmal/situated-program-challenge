(ns rest-server.boundary.db.member
  (:require [duct.database.sql]
            [honeysql.core :as sql]
            [rest-server.boundary.db.core :as db])
  (:import (duct.database.sql Boundary)))

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
  (list-members [db]
    (db/select db (sql/build :select :*
                             :from :members)))
  (create-member [db member]
    (db/insert! db :members member))
  (fetch-member [db member-id]
    (db/select-one db (sql/build :select :*
                                 :from :members
                                 :where [:= :id member-id])))
  (fetch-members [db member-ids]
    (db/select db (sql/build :select :*
                             :from :members
                             :where [:in :id member-ids]))))
