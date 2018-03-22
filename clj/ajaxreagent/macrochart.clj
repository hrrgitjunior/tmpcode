(ns ajaxreagent.macrochart)


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
