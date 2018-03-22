(ns ajaxreagent.querybuildercomponent
    (:require
       [reagent.core :as r]))

;
(def combinators
  [
      {:name "and" :label "AND"}
      {:name "or"  :label "OR"}])

;
(def fields [
             {:name "firstName" :label "First Name"}
             {:name "lastName" :label "Last Name"}])


(def operators
  [
      {:name "null" :label "Is Null"}
      {:name "notNull" :label "s Not Null"}
      {:name "in" :label "In"}
      {:name "notIn" :label "Not In"}
      {:name "=" :label "="}
      {:name "!=" :label "!="}
      {:name "<" :label "<"}
      {:name ">" :label ">"}
      {:name "<=" :label "<="}
      {:name ">=" :label ">="}])


(def beg-uuid (-> (random-uuid) str keyword))
(def query-controls (r/atom {beg-uuid {:path [beg-uuid] :rules {} :combinator (-> combinators first :name)}}))
;
(def query-str (r/atom "aaa"))

(defn build-query [tree-list]
  (let [res
        (reduce
          (fn [acc item]
            (let [keys-item (-> item second keys set)
                  new-item {}]
              (if-not (nil? (keys-item :rules))
                (do
                  (as-> new-item $
                    (assoc $ :id (first item))
                    (if (> (-> item second :rules count) 0)
                      (as-> $ $$
                        (assoc $$ :rules (build-query (-> item second :rules)))
                        (assoc $$ :combinator (-> item second :combinator)))
                      (assoc $ :combinator (-> item second :combinator)))
                    (conj acc $)))

                (conj acc {:id (-> item first)
                           :field (-> item second :field)
                           :value (-> item second :value)
                           :operator (-> item second :operator)}))))
          []
          tree-list)]
    ;(js/console.log (clj->js res)
    res))

;default :id
;(conj path :rules))

;
(defn set-ruls-value [path value & [onQueryChange]]
  (swap! query-controls
    update-in path
         (fn [] value))
  (swap! query-str #(build-query @query-controls))
  (when onQueryChange (onQueryChange (build-query @query-controls))))

(defn widget-field-component [props]
  (let [
        props-state props
        render
        (fn []
          (fn []
            (let [{:keys [field-item margin-left api-props]} (r/props (r/current-component))
                  {path :path} (second field-item)
                  field-uuid (first field-item)
                  field-item (-> field-item second)
                  {:keys [fields operators onQueryChange]} api-props
                  {:keys [valueEditor]} (:controlElements api-props)]


              [:div {:style {:margin-left (str margin-left "px") :margin-top "10px"}}
               [:select.btn.btn-info.btn-sm {:style {:float "left"}
                                             :value (:field field-item)
                                             :on-change
                                             (fn [e]
                                               (set-ruls-value (conj path :field) (.. e -target -value)))}
                (map
                  (fn [item]
                    [:option  {:key (:name item) :value (:name item)} (:label item)])
                  fields)]


               [:select.btn.btn-info.btn-sm {:style {:float "left"}
                                             :value (:operator field-item)
                                             :on-change
                                             (fn [e]
                                               (set-ruls-value (conj path :operator)
                                                               (.. e -target -value)
                                                               onQueryChange))}
                   (map
                     (fn [item]
                       [:option {:key (:name item) :value (:name item)} (:label item)])
                     operators)]

               (if (nil? valueEditor)
                 [:input {:type "text"
                          :value (:value field-item)
                          :on-change
                          (fn [e]
                            (set-ruls-value
                              (conj path :value)
                              (.. e -target -value)
                              onQueryChange))}]
                 [valueEditor {:handleOnChange
                               (fn [value]
                                  (set-ruls-value
                                     (conj path :value) value onQueryChange))}])


               [:button.btn.btn-info.btn-sm
                  {:on-click
                   (fn [e]
                     (swap! query-controls
                       update-in
                          (if (> (count path) 2)
                            (-> path drop-last)
                            path)
                         dissoc field-uuid)
                     ;(swap! query-str #(build-query @query-controls))
                     (when onQueryChange (onQueryChange (build-query @query-controls))))}
                "x"]])))]

    (r/create-class
      {
        :reagent-render render})))



(defn widget-rule-component [props]
  (let [
        render
        (fn []
          (let [{:keys [rule-item margin-left api-props]} (r/props (r/current-component))
                {path :path} (second rule-item)
                rule-uuid (first rule-item)
                {:keys [onQueryChange]} api-props]
            [:div {:style {:margin-left (str margin-left "px") :margin-top "10px"}}
             [:select.btn.btn-primary.btn-sm {:style {:float "left"}}
                                       :value (-> rule-item second :combinator)
                                       :on-change
                                       (fn [e]
                                         (set-ruls-value (conj path :combinator) (.. e -target -value)))



              (map
                (fn [item]
                  [:option {:key (:name item)
                            :value (:name item)}
                      (:label item)])
                combinators)]


             [:button.btn.btn-primary.btn-sm
              {:style {:float "left"}
               :on-click
               (fn [e]
                 (swap! query-controls
                     update-in  (conj path :rules)
                       (fn [rule]
                         (let [new-uuid (-> (random-uuid) str keyword)]
                           (assoc rule new-uuid {:path
                                                 (as-> path $
                                                   (conj $ :rules)
                                                   (conj $ new-uuid))
                                                 :field (-> fields first :name)
                                                 :value ""
                                                 :operator (-> operators first :name)}))))
                 ;(swap! query-str #(build-query @query-controls))
                 (when onQueryChange (onQueryChange (build-query @query-controls))))}

              "+ Rule"]

             [:button.btn.btn-primary.btn-sm
              {:on-click
               (fn [e]
                 (swap! query-controls
                   update-in (conj path :rules)
                     (fn [rule]
                       (let [new-uuid (-> (random-uuid) str keyword)]
                         (assoc rule new-uuid {:path
                                               (as-> path $
                                                 (conj $ :rules)
                                                 (conj $ new-uuid))
                                               :rules {}
                                               :combinator (-> combinators first :name)}))))
                 ;(swap! query-str #(build-query @query-controls)))}
                 (when onQueryChange (onQueryChange (build-query @query-controls))))}


              (str "+ Group")]

             [:button.btn.btn-primary.btn-sm
              {:on-click
               (fn [e]
                 (swap! query-controls
                    update-in
                       (if (> (count path) 2)
                         (-> path drop-last)
                         path)
                      dissoc rule-uuid)
                ;(swap! query-str #(build-query @query-controls))
                (when onQueryChange (onQueryChange (build-query @query-controls))))}
              "x"]]))]



    (r/create-class
      {
        :reagent-render render})))

;
(defn build-widgets [props]
  (let [{control-list :control-list margin-left :margin-left api-props :api-props} props
        res
        (into [:div.query-container]
          (reduce
            (fn [acc item]
              (let [keys-item (-> item second keys set)]
                (if-not (nil? (keys-item :rules))
                  (as-> (conj acc [widget-rule-component {:rule-item item :margin-left margin-left :api-props api-props}]) $
                    (if (> (-> item second :rules count) 0)
                      (conj $ (build-widgets {:control-list (-> item second :rules)
                                              :margin-left (+ margin-left 20)
                                              :api-props api-props}))
                      $))
                  (conj acc [widget-field-component {:field-item item :margin-left margin-left :fields fields :operators operators :api-props api-props}]))))
            []
            control-list))]
    res))


(defn query-builder-component [props]
  (let [
        did-mount
        (fn [])
         ;(swap! query-str #(build-query @query-controls)))

        render
        (fn []
          (let [api-props (r/props (r/current-component))]
          ;  (js/console.log "TREE LIST >>>" (clj->js @query-controls))
            [:div.col-lg-12
             [:div.col-lg-12 {:style {:min-height "550px"}}
              [:h4 "Query Builder Container"]
              (build-widgets
                {:control-list @query-controls
                 :margin-left 0
                 :api-props
                   (as-> api-props $
                     (if (nil? (:fields api-props))
                       (assoc $ :fields fields) $)
                     (if (nil? (:operators api-props))
                      (assoc $ :operators operators) $))})]

             [:div.col-lg-12 {:style {:min-height "500px"}}
              [:pre (with-out-str (cljs.pprint/pprint @query-str))]]]))]




    (r/create-class
      {
        :component-did-mount did-mount
        :reagent-render render})))
