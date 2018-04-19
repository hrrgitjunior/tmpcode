(ns ajaxreagent.colorpicker
    (:require
       [reagent.core :as r]))




(defn colorpicker-component [props]
  (let [{:keys [container plot]} props
        dom (r/atom nil)


        colorpicker-render
          (fn []
            [:div {:style {:min-width "310px" :max-width "100%"
                           :height "100%" :margin-right "10px"}}])]

    (r/render [
               (r/create-class
                 {
                   :reagent-render colorpicker-render})]
            container)))
