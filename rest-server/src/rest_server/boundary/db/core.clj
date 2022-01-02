(ns rest-server.boundary.db.core
  (:require [clojure.java.jdbc :as jdbc]
            [honeysql.core :as sql]
            [rest-server.util :as util]))

(defn select [{db :spec} sql-map]
  (->> sql-map
       sql/format
       (jdbc/query db)
       util/transform-keys-to-kebab))

(defn select-one [{db :spec} sql-map]
  (->> sql-map
       sql/format
       (jdbc/query db)
       util/transform-keys-to-kebab
       first))

;; insert!でわざわざidカラムだけを取り出して返しているのは、insert-multi!との返値の整合性のため。
;; 実際のアプリの仕様的にはINSERTしたレコード全体を返した方が良いとは思うけど、
;; そうでない要件の場合も考えればidカラムだけを返しておいて、全体の取得は別の関数でやった方が良い実装なのかもしれない。
(defn insert! [{db :spec} table row-map & {:keys [id-col]
                                           :or   {id-col :id}}]
  (->> row-map
       util/transform-keys-to-snake
       (jdbc/insert! db table)
       first
       id-col))

;; じゃあ、insert-multi!ってなんだよ?って話になる。
;; https://blog.micheam.com/2019/09/24/clojure-getting-started-with-next-jdbc/
;; 単にinsertの複数行版で、おそらくはパフォーマンス上の理由で大量の行をinsertする際にはこちらを使うべきなのだろう。
;; 当然、そんな大量なinsertを行なった後、レコード全体を返すなんてメモリと帯域の無駄なのでidカラムだけを返すべきだろう。
(defn insert-multi! [{db :spec} table row-maps]
  (->> row-maps
       util/transform-keys-to-snake
       (jdbc/insert-multi! db table)))