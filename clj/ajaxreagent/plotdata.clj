(ns ajaxreagent.plotdata)

(def region-chart
  {:chart {:type "column"}
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
   :credits {:enabled false}
   :series [{:name "Year 1800"
             :data [1107 1531 1635 1203 2]
             :color "red"}
            {:name "Year 1900"
             :data [1233 1756 1947 1408 6]
             :color "green"}
            {:name "Year 2008"
             :data [1973 2914 4054 732 34]
             :color "blue"}]})

;
(def line-chart
  {:chart {:type "line"}
   :title {:text "Test line"}
   :series [{:name "Line 1"
             :color "red"
             :dashStyle "Solid"
             :data [{:x 1 :y 1} {:x 2 :y 2}]}

            ;
            {:name "Line 2"
                      :color "blue"
                      :dashStyle "Solid"
                      :data [{:x 1 :y 2} {:x 2 :y 3}]}]})
;
(def heatmap-chart
  {:chart {:type "heatmap"}
   :title {:text "Test heatmap"}

   :xAxis {:categories ["BM1" "BM2"]
           :title {:text nil}}
   :yAxis {:categories ["SUBJ1" "SUBJ2"]
           :title {:text nil}}

   :colorAxis {:min 0
               :minColor "#fff",
               :max 50
               :maxColor "#0f5",}


   :series [{:type "heatmap"
             :borderWidth 1
             :data
              [[0 0 15] [1 0 20] [0 1 5] [1 1 10]]
             :dataLabels {:enabled true}}]})
