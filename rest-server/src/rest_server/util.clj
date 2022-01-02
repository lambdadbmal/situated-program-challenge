(ns rest-server.util
  (:require [camel-snake-kebab.core :refer [->kebab-case ->snake_case]]
            [camel-snake-kebab.extras :refer [transform-keys]])
  (:import (java.sql Timestamp)
           (java.time Instant)))

(defn transform-keys-to-kebab [m]
  (transform-keys #(->kebab-case % :separator \_) m))

(defn transform-keys-to-snake [m]
  (transform-keys #(->snake_case % :separator \-) m))

(defn string->timestamp [s]
  ;; some-> はnilチェック付きのスレッディングマクロ。処理中にnilを検出したら、そこで処理が終わってnilを返す。
  (some-> s
          Instant/parse
          Timestamp/from))

;; Clojure.java-timeやtickを使う実装もしてみたけど、JVM環境でしか動かさないならこういう実装もあり、という例。
(defn now []
  (Timestamp/from (Instant/now)))
