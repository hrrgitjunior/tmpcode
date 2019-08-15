(ns ajaxreagent.forcegraph
    (:require
       [reagent.core :as r]))

(defn force-graph-component [props]
  (let [{:keys [container graph]} props
        did-mount
        (fn []
          ;(js/forcegraph (clj->js graph)))
          (js/spingygraph))

        render
        (fn []
          [:div "Force Graph"
           [:canvas {:id "my_canvas" :style { :width 800 :height 600}}]])]



    (r/render [
               (r/create-class
                 { :component-did-mount did-mount
                   :reagent-render render})]
            container)))
