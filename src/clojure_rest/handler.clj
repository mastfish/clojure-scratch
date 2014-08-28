(ns clojure-rest.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-http.client :as client]
            [cheshire.core :refer :all]))

(defn input [] (client/get "http://search-service.production.dbg.westfield.com/api/search/master/search.json?centre=sydney&term=wid"))
(defn json [] (parse-string (:body (input))))
(def results ((json) "results"))
(defn score [input] (get input "score"))
(defn flat_results [] (reduce concat(map results (keys results))))
(defn score_total [] (reduce + (map score (flat_results))))

(defroutes app-routes
	(GET "/" []
    (generate-string (score_total)))
  (GET "/user/:id" [id]
  	(str "Hello user " id))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
