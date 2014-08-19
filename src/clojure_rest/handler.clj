(ns clojure-rest.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-http.client :as client]
            [cheshire.core :refer :all]))

(defn input [] (client/get "http://search-service.production.dbg.westfield.com/api/search/master/search.json?centre=sydney&term=co"))
(defn json [] (parse-string (:body (input))))
(defn result [] (json))

(defroutes app-routes
	(GET "/" []
    (generate-string (result)))
  (GET "/user/:id" [id]
  	(str "Hello user " id))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
