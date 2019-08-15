(ns ajaxreagent.jqdatatable
    (:require
       [reagent.core :as r]))

;
(defn data-table-component [props]
  (let [{:keys [container]} props
        dt (r/atom nil)
        did-mount
        (fn [this]
          (let [data [{:col1 111 :col2 "aaa"} {:col1 222 :col2 "bbb"}]]

            (println data)
            (reset! dt
              (. (js/$ (r/dom-node this)) DataTable
                (clj->js {:data data
                          :columns [{:data "col1"} {:data "col2"}]
                          :scrollY "200px"
                          :dom "Rlfrtip"
                          :colReorder { :allowReorder false}


                          :paging true
                          :scrollCollapse true
                          :autoWidth true
                          :fixedColumns true})))))


        render
        (fn []
          [:table { :style {:width "100%"}
                    :class "table table-bordered table-hover dataTable no-footer"
                    :id "tableid"}])]

    (r/render [
               (r/create-class
                 { :component-did-mount did-mount
                   :reagent-render render})]
            container)))
