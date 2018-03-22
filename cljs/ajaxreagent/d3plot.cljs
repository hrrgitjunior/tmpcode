(ns ajaxreagent.d3plot
    (:require
       [reagent.core :as r]))


;
(defn d3plot-component [props]
  (let [{:keys [container plot-data]} props

        d3plot-did-mount
          (fn [this]
            (js/createClusterDendrogram plot-data (r/dom-node this)))

        d3plot-render
          (fn []
            [:div {:style {:min-width "310px" :max-width "100%"
                           :height "100%" :margin-right "10px"}}])]




    (r/render [
               (r/create-class
                 { :component-did-mount d3plot-did-mount
                   :reagent-render d3plot-render})]
            container)))
