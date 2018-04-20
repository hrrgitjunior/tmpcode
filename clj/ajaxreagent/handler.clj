(ns ajaxreagent.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :as middleware]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.util.response :as response]
            [hiccup.page :refer [include-js include-css html5]]
            [ajaxreagent.plotdata :as pd]
            [ajaxreagent.report :as r]
            [ajaxreagent.clusterdendrogram :as cd]))

(defroutes app-routes
  (GET "/" [] (response/resource-response "index.html" {:root "public"}))
  (route/resources "/")
  (POST "/hello" request
        (response/response {:body {:greeting "Ring Server"}}))

  (POST "/plot" request
        (response/response
          {:body {:greeting 10 :plot "bla bla"}}))

  (POST "/chart-plot" request
    (fn [req]
      (response/response
        {:body
          (case (-> req :params :plot)
            "region"
              pd/region-chart
            "line"
              pd/line-chart
            "heatmap"
              pd/heatmap-chart
              pd/heatmap-chart)})))

  (POST "/report" request
    (fn [req]
      (response/response
        {:body (r/build-report)})))

  (POST "/cluster-dendrogram" request
    (fn [req]
      (response/response
        {:body (cd/cluster-dendrogram)})))




                           ;(get-in request [:params :user])}))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      wrap-keyword-params
      middleware/wrap-json-params
      middleware/wrap-json-response))
