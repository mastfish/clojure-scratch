(ns clojure-rest.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-http.client :as client]
            [cheshire.core :refer :all]))

(defn input [] (client/get "http://search-service.production.dbg.westfield.com/api/search/master/search.json?centre=sydney&term=co"))
(defn json [] (parse-string (:body (input))))
(defn results [] ((json) "results"))
(defn products [] ((results) "products"))
(defn score [input] (get input "score"))
(defn score_total [] (reduce + (map score (products))))

(defroutes app-routes
	(GET "/" []
    (generate-string (score_total)))
  (GET "/user/:id" [id]
  	(str "Hello user " id))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
