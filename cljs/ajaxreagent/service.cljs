(ns ajaxreagent.service
  (:require [ajax.core :refer [GET POST]]
            [dommy.core :as dommy :refer-macros [sel sel1]]
            [reagent.core :as r]
            [ajaxreagent.highcharts :refer [chart-component]]
            [ajaxreagent.report :refer [report-component]]
            [ajaxreagent.d3plot :refer [d3plot-component]]))


(defn handler-chart [response]
  (chart-component
    {:container (.getElementById js/document "maincontainer-id")
     :plot (aget (clj->js response) "body")}))

;
(defn handler-report [response]
  (report-component
    {:container (.getElementById js/document "maincontainer-id")
     :url (aget (clj->js response) "body")}))

;
(defn handler-cluster-dendrogram [response]
  (d3plot-component
    {:container (.getElementById js/document "maincontainer-id")
     :plot-data (aget (clj->js response) "body" "plot-data")}))






(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn chart-plot-service [plot-type]
  (POST "/chart-plot"
        {:params {:plot plot-type}
         :handler handler-chart
         :error-handler error-handler
         :format :json
         :response-format :json}))
;
(defn cluster-dendrogram-service []
  (POST "/cluster-dendrogram"
        {:params {}
         :handler handler-cluster-dendrogram
         :error-handler error-handler
         :format :json
         :response-format :json}))

;
(defn report-service []
  (POST "/report"
        {:params {}
         :handler handler-report
         :error-handler error-handler
         :format :json
         :response-format :json}))
