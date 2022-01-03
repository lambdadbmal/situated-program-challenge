(ns rest-server.handler.venue
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]
            [rest-server.boundary.db.venue :as db.venue]))

(defn venue-with-address [{:keys [id name postal-code prefecture city street1 street2]}]
  {:venue-id   id
   :venue-name name
   :address    {:postal-code postal-code
                :prefecture  prefecture
                :city        city
                :address1    street1
                :address2    street2}})

(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [{[_ group-id] :ataraxy/result}]
    (println "launch venue/list" group-id)
    [::response/ok (map venue-with-address
                        (db.venue/list-venues db group-id))]))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  ;; TODO: 何らかの要因でdb.*/create-*が失敗した場合のエラー処理をするべき。
  (fn [{[_ group-id {:keys [address] :as venue}] :ataraxy/result}]
    (println "launch venue/create" group-id venue)
    (let [venue' {:name        (:venue-name venue)
                  :postal-code (:postal-code address)
                  :prefecture  (:prefecture address)
                  :city        (:city address)
                  :street1     (:address1 address)
                  :street2     (:address2 address)
                  :group-id    group-id}
          id (db.venue/create-venue db venue')])
    [::response/ok])
  )