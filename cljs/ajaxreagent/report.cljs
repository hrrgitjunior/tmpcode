(ns ajaxreagent.report
    (:require
       [reagent.core :as r]))


;
(defn report-component [props]
  (let [{:keys [container url]} props

        report-did-mount
          (fn [this])

        report-render
          (fn []
            (println "report")
            (js-debugger)
            [:div {:style {:min-width "310px" :max-width "100%"
                           :height "100%" :margin-left "10px"}}
              [:object {:style
                        {:width "100%"
                         :height "100%"}
                        :data "/test_report_3.pdf"}]])]



    (r/render [
               (r/create-class
                 { :component-did-mount report-did-mount
                   :reagent-render report-render})]
            container)))
