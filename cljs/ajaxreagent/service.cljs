(ns ajaxreagent.service
  (:require [ajax.core :refer [GET POST]]
            [dommy.core :as dommy :refer-macros [sel sel1]]
            [reagent.core :as r]
            [ajaxreagent.highcharts :refer [chart-component]]
            [ajaxreagent.report :refer [report-component]]
            [ajaxreagent.d3plot :refer [d3plot-component]]
            [ajaxreagent.forcegraph :refer [force-graph-component]]))


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


(defn force-graph-handler [response]
  (let [nodes
        (->> (-> (clj->js response) (aget "body" "nodes") js->clj)
          (mapv
            (fn [item]
              (into {}
                (->> item
                  (map (fn [[k v]] {(keyword k) v})))))))
        ;
        links
        (->> (-> (clj->js response) (aget "body" "links") js->clj)
          (mapv
            (fn [item]
              (into {}
                (->> item
                  (map (fn [[k v]] {(keyword k) v})))))))]


    (force-graph-component { :container (.getElementById js/document "maincontainer-id")
                             :graph {:nodes nodes :links links}})))



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

(defn force-graph-service []
  (POST "/force-graph-data"
        {:params {}
         :handler force-graph-handler
         :error-handler error-handler
         :format :json
         :response-format :json}))
