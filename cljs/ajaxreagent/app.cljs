(ns ajaxreagent.app
  (:require [ajaxreagent.greet :as greet]
            [reagent.core :as r]
            [ajaxreagent.highcharts :refer [chart-component]]
            [ajaxreagent.service :refer  [chart-plot-service report-service cluster-dendrogram-service]]
            ;[ajaxreagent.querybuildercomponent :refer [query-builder-component]]
            [ajaxreagent.querybuilderarray :refer [query-builder-component]]
            [ajaxreagent.itemselection :refer [item-selection-component]]
            [webpack.bundle]))




;
(enable-console-print!)
;(println "LEFT PAD"(left-pad 42 5 0))
;

(defn dashboard []
   [:div.content-wrapper
    [:div.container-fluid {:id "maincontainer-id"}]])

(defn nav-vertical []
   [:nav.navbar.navbar-expand-lg.navbar-dark.bg-dark {:id "mainNav"}
     [:a.navbar-brand {:href "index.html"} "Start Figwheel"]
     [:div.collapse.navbar-collapse
       [:ul.navbar-nav.navbar-sidenav
        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Dashboard"}
          [:a.nav-link {:href "index.html"}
            [:i.fa.fa-fw.fa-dashboard]
            [:span.nav-link-text "Dashboard"]]]

        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Dashboard"}
         [:a.nav-link { :href "javascript:"
                       :on-click
                        (fn [e]
                          (greet/say-hello))}
                   [:i.fa.fa-fw.fa-dashboard]
                   [:span.nav-link-text "Test Ajax"]]]
        ;
        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Components"}
          [:a.nav-link.nav-link-collapse.collapsed {:data-toggle "collapse" :href "#collapseComponents" :data-parent "#exampleAccordion"}
            [:i.fa.fa-fw.fa-wrench]
            [:span.nav-link-text "Components"]]

          [:ul.sidenav-second-level.collapse {:id "collapseComponents"}
            [:li
              [:a {:href "#"} "item1"]]
            [:li
              [:a {:href "#"} "item2"]]]]

        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Dashboard"}
         [:a.nav-link { :href "javascript:"
                       :on-click
                       (fn [e]
                         (chart-plot-service "region"))}
           [:span.nav-link-text "Region"]]]

        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Dashboard"}
         [:a.nav-link { :href "javascript:"
                       :on-click
                       (fn [e]
                         (chart-plot-service "line"))}
           [:span.nav-link-text "Line"]]]

        [:li.nav-item {:data-toggle "tooltip" :data-placement "right" :title "Dashboard"}
         [:a.nav-link { :href "javascript:"
                       :on-click
                       (fn [e]
                         (chart-plot-service "heatmap"))}
           [:span.nav-link-text "Heatmap"]]]]]])







(defn app-render []
  [:div
   [nav-vertical]
   [dashboard]])

(defn app []
  (r/create-class
    {:component-did-mount
      (fn [this]
        (println "did mount app"))

     :reagent-render
      (fn [] app-render)}))


(r/render [app] (.-body js/document))


(.log js/console "Hello Cljs 123!")
;(js/console.log (renderToString (createElement "div" nil "Hello World!")))
