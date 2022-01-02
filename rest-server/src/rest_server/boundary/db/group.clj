(ns rest-server.boundary.db.group
  (:require [duct.database.sql]
            [honeysql.core :as sql]
            [camel-snake-kebab.core :refer [->kebab-case ->snake_case]]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [clojure.java.jdbc :as jdbc]
            [clojure.set :as set]
            [tick.core :as t])
  (:import (duct.database.sql Boundary)))

(defn group-with-id [group]
  (set/rename-keys group {:id :group-id}))

(defn group-with-name [group]
  (set/rename-keys group {:group-name :name}))

(defn now []
  (java.sql.Timestamp/from (t/now)))

(defprotocol Groups
  (list-groups [db])
  (create-group [db group])
  (fetch-group [db group-id])
  (fetch-group-admin-members [db group-id])
  (create-group-members [db group-members]))

(extend-protocol Groups
  Boundary
  (list-groups [{db :spec}]
    (jdbc/query db (sql/format (sql/build :select :* :from :groups))))
  (create-group [{db :spec} group]
    (let [record (assoc (transform-keys ->snake_case (group-with-name group)) :created_at (now))]
      (group-with-id
        (first
          (jdbc/insert! db :groups record))))))
