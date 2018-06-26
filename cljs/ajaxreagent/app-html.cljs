(ns ajaxreagent.app
  (:require [ajaxreagent.greet :as greet]
            [reagent.core :as r]
            [ajaxreagent.highcharts :refer [chart-component]]
            [ajaxreagent.service :refer  [chart-plot-service report-service cluster-dendrogram-service]]
            ;[ajaxreagent.querybuildercomponent :refer [query-builder-component]]
            [ajaxreagent.querybuilderarray :refer [query-builder-component]]
            [ajaxreagent.itemselection :refer [item-selection-component]]
            [ajaxreagent.app :refer [app-render]]
            [webpack.bundle]))




;
(enable-console-print!)
;(println "LEFT PAD"(left-pad 42 5 0))
;
(def fields [{:name "firstName"  :label "First Name"}
             {:name "lastName"  :label "Last Name" :demo 1}])
;
(def translations
  {
      :fields {
               :title "Fields"}

      :operators {
                  :title "Operators"}

      :value {
              :title "Value"}

      :removeRule {
                   :label "x"
                   :title "Remove rule"}

      :removeGroup {
                    :label "x"
                    :title "Remove group"}

      :addRule {
                :label "Add Rule"
                :title "Add Rule"}

      :addGroup {
                 :label "Add Group"
                 :title "Add group"}

      :combinators {
                    :title "Combinators"}})


(defn value-component [props]
  (let [props-state props
        rule-render
        (fn []
          (let [props (r/props (r/current-component))]
            [:span
             [:input {:type "text"}]]))]

    (r/create-class
           {
             :reagent-render rule-render})))


(defn field-component [props]
  (let [props-state props
        field-render
        (fn []
          (let [props (r/props (r/current-component))
                query-change (:handleOnChange props)]
            (println "field" (:handleOnChange props))
            [:span
             [:select {:on-change (fn [e] (query-change "AAA"))}
              [:option {:value "volvo"} "volvo"]
              [:option {:value "saab"} "saab"]
              [:option {:value "mercedes"} "mercedes"]]]))]


    (r/create-class
           {
             :reagent-render field-render})))



(defn query-component [props]
  (let [{:keys [container]} props
        dom (r/atom nil)

        ;
        did-mount
        (fn [this]
          (let [d (r/dom-node this)]
            (reset! dom d)))

        query-render
        (fn []
          (let [QueryBuilder (-> (aget js/window "deps" "react-querybuilder")
                               (aget  "default"))]


            (println "RRRRR" @dom)
            [:div.react-query {:id "qbid"}
              (when-not (nil? @dom)
               [:> QueryBuilder {:fields (clj->js fields)
                                 :controlElements
                                    {:valueEditor ;(dynamic-control "test")
                                     (value-component "Hristo Radoev")}
                                     ; :fieldSelector
                                     ; (field-component "")}



                                 :controlClassnames
                                  {:fields "btn btn-info btn-sm"
                                   :operators "btn btn-info btn-sm"
                                   :removeRule "btn btn-info btn-sm"
                                   :combinators "btn btn-primary btn-sm"
                                   :addGroup "btn btn-primary btn-sm"
                                   :addRule "btn btn-primary btn-sm"
                                   :removeGroup "btn btn-primary btn-sm"}

                                 :translations
                                  translations

                                 :onQueryChange
                                  (fn [query]
                                    (js/console.log query))}])]))];

    (r/render [
               (r/create-class
                 {
                   :component-did-mount did-mount
                   :reagent-render query-render})]
            container)))


(defn app-render-html []
  [:div.row
   [:div.col-lg-3 {:style
                   {:background-color "#eff"
                    :height "600px"}}
    [:ul {:style
          {:list-style-type "none"}};}

     [:li {:style {:margin-top "20px"}}
      [:a { :href "javascript:" :role "menuitem"
            :on-click
             (fn [e]
               (greet/say-hello))}
       "Test Ajax"]]
     [:li {:style {:margin-top "20px"}}
      [:a { :href "javascript:" :role "menuitem"
            :on-click
            (fn [e]
              (chart-plot-service"region"))}
       "Region"]]

     [:li {:style {:margin-top "20px"}}
      [:a { :href "javascript:" :role "menuitem"
            :on-click
            (fn [e]
              (chart-plot-service "line"))}
       "Line"]]


     [:li {:style {:margin-top "20px"}}
      [:a { :href "javascript:" :role "menuitem"
            :on-click
            (fn [e]
              (chart-plot-service "heatmap"))}
       "Heatmap"]]


     [:li {:style {:margin-top "20px"}}
      [:a { :href "javascript:" :role "menuitem"
            :on-click
            (fn [e]
              (cluster-dendrogram-service))}
       "Cluster Dendrogram"]]


     [:li {:style {:margin-top "20px"}}
      [:a { :href "javascript:" :role "menuitem"
            :on-click
            (fn [e]
              (report-service))}
       "Report"]]

     ;
     [:li {:style {:margin-top "20px"}}
      [:a { :href "javascript:" :role "menuitem"
            :on-click
            (fn [e]
              (query-component
                {:container (.getElementById js/document "maincontainer-id")}))}

       "Query Builder"]]

     [:li {:style {:margin-top "20px"}}
      [:a { :href "javascript:" :role "menuitem"
            :on-click
            (fn [e]
              (item-selection-component
                {:container (.getElementById js/document "maincontainer-id")}))}

       "Query Builder Container"]]]]



   ;----- main container -----
   [:div.col-lg-9
    {:id "maincontainer-id"
     :style
     {:background-color "#fff"
      :height "600px"}}]])


(defn app []
  (r/create-class
    {:component-did-mount
      (fn [this]
        (println "did mount app"))

     :reagent-render
      (fn [] app-render)})) ;Important:::  APP-RENDER from app.js run boostrap menu, APP-RENDER-HTML run html menu from app-html.cljs


(r/render [app] (.-body js/document))


(.log js/console "Hello Cljs!")
;(js/console.log (renderToString (createElement "div" nil "Hello World!")))
