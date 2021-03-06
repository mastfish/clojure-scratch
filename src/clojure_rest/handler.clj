(ns clojure-rest.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clj-http.client :as client]
            [cheshire.core :refer :all]))

(defn search_endpoint [search] (
  client/get (
    str "http://search-service.production.dbg.westfield.com/api/search/master/search.json?centre=sydney&term=" search)))
; wid = -324

(defn json [data] (parse-string (data :body)))

(defn results [data] (get data "results"))
(defn flat_results [data] (reduce concat(map data (keys data))))
(defn score_total [data] (reduce + (map score data)))
; Map functions
(defn score [data] (get data "score"))

(defn product_endpoint []
  (client/get ( 
    str "http://product-service.production.dbg.westfield.com/api/product/master/products.json"
    )))

(defroutes app-routes
	(GET "/" []
    (generate-string (score_total)))
  (GET "/search/:search" [search]
    (generate-string (hash-map :score_total (score_total (flat_results (results (json (search_endpoint search))))))))
  (GET "/master/products.json" []
    (generate-string (json (product_endpoint))))
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
