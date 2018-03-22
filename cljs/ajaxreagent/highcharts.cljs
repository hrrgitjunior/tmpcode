(ns ajaxreagent.highcharts
    (:require
       [reagent.core :as r]))


(def chart-config
  {:chart {:type "bar"}
   :title {:text "Historic World Population by Region"}
   :subtitle {:text "Source: Wikipedia.org"}
   :xAxis {:categories ["Africa" "America" "Asia" "Europe" "Oceania"]
           :title {:text nil}}
   :yAxis {:min 0
           :title {:text "Population (millions)"
                   :align "high"}
           :labels {:overflow "justify"}}
   :tooltip {:valueSuffix " millions"}
   :plotOptions {:bar {:dataLabels {:enabled true}}}
   :legend {:layout "vertical"
            :align "right"
            :verticalAlign "top"
            :x -40
            :y 100
            :floating true
            :borderWidth 1
            :shadow true}
   ;:credits {:enabled false}
   :series [{:name "Year 1800"
             :data [107 31 635 203 2]}
            {:name "Year 1900"
             :data [133 156 947 408 6]}
            {:name "Year 2008"
             :data [973 914 4054 732 34]}]})

;
(def heatmap-chart
  {
    :chart {
            :type "heatmap"
            :plotBorderWidth  1}


    :title {
            :text "Sales per employee per weekday"}


    :xAxis {
             :categories ["Alexander", "Marie", "Maximilian", "Sophia", "Lukas", "Maria", "Leon", "Anna", "Tim", "Laura"]}

    :yAxis {
            :categories ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]
            :title nil}


    :colorAxis {
                :min 0,
                :minColor "#FFFFFF",
                :maxColor "blue"}

    :legend {
             :align "right"
             :layout "vertical"
             :margin 0
             :verticalAlign "top"
             :y 25
             :symbolHeight 280}


    :series [{
              :name "ales per employee",
              :borderWidth 1,
              :data [[0, 0, 10], [0, 1, 19], [0, 2, 8], [0, 3, 24], [0, 4, 67], [1, 0, 92], [1, 1, 58], [1, 2, 78], [1, 3, 117], [1, 4, 48], [2, 0, 35], [2, 1, 15],
                     [2, 2, 123], [2, 3, 64], [2, 4, 52], [3, 0, 72], [3, 1, 132], [3, 2, 114], [3, 3, 19], [3, 4, 16], [4, 0, 38], [4, 1, 5], [4, 2, 8], [4, 3, 117],
                     [4, 4, 115],
                     [5, 0, 88], [5, 1, 32], [5, 2, 12], [5, 3, 6], [5, 4, 120], [6, 0, 13], [6, 1, 44], [6, 2, 88], [6, 3, 98], [6, 4, 96], [7, 0, 31], [7, 1, 1],
                     [7, 2, 82],
                     [7, 3, 32],
                     [7, 4, 30], [8, 0, 85], [8, 1, 97], [8, 2, 123], [8, 3, 64], [8, 4, 84], [9, 0, 47], [9, 1, 114], [9, 2, 31], [9, 3, 48], [9, 4, 91]]
              :dataLabels {
                           :enabled true
                           :color "#000000"}}]})



;


(defn chart-component [props]
  (let [{:keys [container plot]} props
        dom (r/atom nil)

        chart-did-mount
          (fn [this]
            (js/Highcharts.Chart. (r/dom-node this)
                                  (clj->js plot)))

        chart-render
          (fn []
            [:div {:style {:min-width "310px" :max-width "100%"
                           :height "100%" :margin-right "10px"}}])]

    (r/render [
               (r/create-class
                 { :component-did-mount chart-did-mount
                   :reagent-render chart-render})]
            container)))
