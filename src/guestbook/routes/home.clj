(ns guestbook.routes.home
  (:require [compojure.core :refer :all]
            [guestbook.views.layout :as layout]
            [hiccup.form :refer :all]
            [guestbook.models.db :as db]))

(defn format-time [timestamp]
  (-> "dd/MM/yyyy"
    (java.text.SimpleDateFormat.)
    (.format timestamp)))

(defn show-guests []
  [:ul.guests
    (for [{:keys [message name timestamp]}
          (db/read-guests)]
      [:li
        [:blockquote message]
        [:p "-" [:cite name]]
        [:time (format-time timestamp)]])])

(defn home [& [name error message]]
  (layout/common [:h1 "Guestbook"]
    [:p "Welcome to my guestbook"]
    [:p error]
    ; calling our show-guests function
    (show-guests)
    [:hr]
    ; TODO: use form helpers
    (form-to [:post "/"]
      [:p "Name: "]
      (text-field "name" name)
      [:p "Message: "]
      (text-area {:rows 10 :cols 40} "message" message)
      [:br]
      (submit-button "comment"))))
      
(defn save-message [name message]
  (cond
    (empty? name)
    (home name message "you forget to leave a name")
    
    (empty? message)
    (home name message "dont you have something to say?")
    
    :else
    (do
      (db/save-message name message)
      (home))))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" [name message] (save-message name message)))
