(ns rest-server.handler.meetup
  (:require [ataraxy.response :as response]
            [integrant.core :as ig]
            [rest-server.boundary.db.meetup :as db.meetup]
            [rest-server.boundary.db.venue :as db.venue]
            [rest-server.handler.member :as member]
            [rest-server.handler.venue :as venue]
            [rest-server.util :as util]))

(defn meetup-with-venue-and-members [{:keys [id title start-at end-at]} venue members]
  {:event-id id
   :title    title
   :start-at start-at
   :end-at   end-at
   :venue    (venue/venue-with-address venue)
   :members  (map member/member-with-id members)})

(defn fetch-meetup-detail [db {:keys [id venue-id] :as meetup}]
  (let [venue (db.venue/fetch-venue db venue-id)
        members (db.meetup/fetch-meetup-members db id)]
    (meetup-with-venue-and-members meetup venue members)))

(defn get-meetup [db meetup-id]
  (when-let [meetup (db.meetup/fetch-meetup db meetup-id)]
    (fetch-meetup-detail db meetup)))

(defmethod ig/init-key ::list [_ {:keys [db]}]
  (fn [{[_ group-id] :ataraxy/result}]
    (println "launch meetup/list" group-id)
    [::response/ok (map (partial fetch-meetup-detail db)
                        (db.meetup/list-meetups db group-id))]))

(defmethod ig/init-key ::create [_ {:keys [db]}]
  ;; TODO: 何らかの要因でdb.meetup/create-meetupが失敗した場合のエラー処理をするべき。
  (fn [{[_ group-id {:keys [start-at end-at]} meetup] :ataraxy/result}]
    (println "launch meetup/create" group-id meetup)
    (let [meetup' (assoc meetup
                    :start-at (util/string->timestamp start-at)
                    :end-ad (util/string->timestamp end-at)
                    :group-id group-id)
          id (db.meetup/create-meetup db meetup)]
      [::response/ok (-> meetup'
                         (assoc :id id)
                         ((partial fetch-meetup-detail db)))])))

(defmethod ig/init-key ::fetch [_ {:keys [db]}]
  (fn [{[_ _ event-id] :ataraxy/result}]
    (println "launch meetup/fetch" event-id)
    (when-let [meetup (get-meetup db event-id)]
      [::response/ok meetup])))

(defmethod ig/init-key ::join [_ {:keys [db]}]
  (fn [{[_ member-id event-id] :ataraxy/result}]
    (println "launch meetup/join" member-id event-id)
    (db.meetup/create-meetup-member db {:meetup-id event-id
                                        :member-id member-id})
    [::response/ok (get-meetup db member-id)]))
