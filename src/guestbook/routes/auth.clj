(ns guestbook.routes.auth
  (:require [compojure.core :refer [defroutes GET POST]]
            [guestbook.views.layout :as layout]
            [hiccup.form :refer :all]
            [noir.response :refer [redirect]]))
 
(defn control [field name text]
  (list (label name text)
        (field name)
        [:br]))
        
(defn registration-page []
  (layout/common
    (form-to [:post "/register"]
      (control text-field :id "screen name")
      (control password-field :pass "password")
      (control password-field :pass1 "password confirmation")
      (submit-button "create account")
      )))
      
(defroutes auth-routes
  (GET "/register" [_] (registration-page))
  (POST "/register" [id pass pass1]
    (if (= pass pass1)
      (redirect "/")
      (registration-page))))