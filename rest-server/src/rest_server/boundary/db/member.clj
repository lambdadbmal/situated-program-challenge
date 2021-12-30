(ns rest-server.boundary.db.member
  (:require [duct.database.sql]
            [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql])
  (:import (duct.database.sql Boundary)))

;; protocolを作り、インターフェースの抽象化を図る。(JavaでInterfaceを切る感じかな?)
(defprotocol Members
  (list-members [db])
  ;(create-member [db member])
  ;(fetch-member [db member-id])
  ;(fetch-members [db member-ids])
  )

(extend-protocol Members
  ;; duct.database.sql.Boundaryを拡張するのは、mockやstub化を簡単にするためらしい。
  ;; https://github.com/duct-framework/database.sql
  Boundary
  (list-members [{db :spec}]                                ;; DuctのDB設定のmapからは:specを抜き出して使う。
    (jdbc/query db (sql/format (sql/build :select :* :from :members))))
  ;(create-member [db member])
  ;(fetch-member [db member-id])
  ;(fetch-members [db member-ids])
  )
