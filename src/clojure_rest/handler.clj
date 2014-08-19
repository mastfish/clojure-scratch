(ns clojure-rest.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/user/:id" [id]
  	(str "Hello user " id))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
