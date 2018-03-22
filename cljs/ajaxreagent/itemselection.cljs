(ns ajaxreagent.itemselection
    (:require
       [reagent.core :as r]
       [ajaxreagent.querybuilderarray :refer [query-builder-component]]))


;
(defn onQueryChange [query-result]
  (js/console.log "Item Selection Query Build Result >>>", (clj->js query-result)))

(defn value-editor [props]
  (let [
        render
        (fn []
          (let [props (r/props (r/current-component))
                {handleOnChange :handleOnChange} props]
            [:div {:style {:float "left"}}
             [:select
              {:on-change
                (fn [e]
                  (handleOnChange (.. e -target -value)))}
              [:option {:key "AAA" :value "aaa" :label "AAA"}]
              [:option {:key "BBB" :value "bbb" :label "BBB"}]]]))]


    (r/create-class
      {
        :reagent-render render})))

(defn item-selection-component [props]
  (let [{:keys [container]} props

        did-mount
        (fn [])

        render
        (fn []
          [:div.col-lg-12
           [:div.col-lg-8 {:style {:min-height "550px"}}
            [:h4 "Item Selection"]
            [query-builder-component
             { :fields
               [
                {:name "firstName" :label "First Name"}
                {:name "address" :label "Address"}]
               :controlElements
               {:valueEditor value-editor}
               :onQueryChange onQueryChange}]]])]




    (r/render [
               (r/create-class
                 {
                   :component-did-mount did-mount
                   :reagent-render render})]
            container)))
