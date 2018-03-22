(ns ajaxreagent.querybuilderarray
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
(def query-controls (r/atom [{:id beg-uuid :rules [] :combinator (-> combinators first :name)}]))
;
(def query-str (r/atom "aaa"))

; (defn build-query [tree-list]
;   (let [res
;         (reduce
;           (fn [acc item]
;             (let [keys-item (-> item keys set)
;                   new-item {}]
;               (if-not (nil? (keys-item :rules))
;                 (do
;                   (as-> new-item $
;                     (assoc $ :id (:id item))
;                     (if (> (-> item :rules count) 0)
;                       (as-> $ $$
;                         (assoc $$ :rules (build-query (:rules item)))
;                         (assoc $$ :combinator (:combinator item)))
;                       (assoc $ :combinator (:combinotor item)))
;                     (conj acc $)))
;
;                 (conj acc {:id (:id item)
;                            :field (:field item)
;                            :value (:value item)
;                            :operator (:operator item second)}))))
;           []
;           tree-list)]
;     ;(js/console.log (clj->js res)
;     res))

;
(defn build-query [tree-list]
  (let [res
        (reduce
          (fn [acc item]
            (let [keys-item (-> item keys set)
                  new-item []]
              (if-not (nil? (keys-item :rules))
                (do
                  (as-> acc $
                    (conj $ :and)
                    (if (> (-> item :rules count) 0)
                      (as-> new-item $$
                        (conj $ (build-query (:rules item))))
                      (conj $ new-item))))


                (conj acc "AAA"))))
          []
          tree-list)]
    ;(js/console.log (clj->js res)
    res))

;default :id
;(conj path :rules))

;
(defn update-control-list [control-list on-update-control-list]
  (if (nil? on-update-control-list)
    (swap! query-controls (fn [] control-list))
    (on-update-control-list control-list)))

(defn set-ruls-value [control-list path value on-update-control-list]
  (as-> control-list $
    (update-in $ path
       (fn [] value))
    (update-control-list $ on-update-control-list)
    (swap! query-str #(build-query $))))

(defn widget-field-component [props]
  (let [
        props-state props
        render
        (fn []
          (fn []
            (let [{:keys [control-list path field-item margin-left api-props]} (r/props (r/current-component))
                  {:keys [fields operators onQueryChange]} api-props
                  {:keys [valueEditor]} (:controlElements api-props)]


              [:div {:style {:margin-left (str margin-left "px") :margin-top "10px"}}
               [:select.btn.btn-info.btn-sm {:style {:float "left"}
                                             :value (:field field-item)
                                             :on-change
                                             (fn [e]
                                               (set-ruls-value control-list (conj path :field) (.. e -target -value) nil))}
                (map
                  (fn [item]
                    [:option  {:key (:name item) :value (:name item)} (:label item)])
                  fields)]


               [:select.btn.btn-info.btn-sm {:style {:float "left"}
                                             :value (:operator field-item)
                                             :on-change
                                             (fn [e]
                                               (set-ruls-value control-list
                                                               (conj path :operator)
                                                               (.. e -target -value)
                                                               nil))}
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
                              control-list
                              (conj path :value)
                              (.. e -target -value)
                              nil))}]
                 [valueEditor {:handleOnChange
                               (fn [value]
                                  (set-ruls-value control-list
                                     (conj path :value) value nil))}])


               [:button.btn.btn-info.btn-sm
                  {:on-click
                   (fn [e]
                     (swap! query-controls
                       update-in (drop-last path)
                       (fn [rule]
                         (reduce
                           (fn [acc item]
                             (if-not (= (:id item) (:id field-item))
                               (conj acc item)
                               acc))
                           []
                           rule))))}
                "x"]])))]

    (r/create-class
      {
        :reagent-render render})))



(defn widget-rule-component [props]
  (let [
        render
        (fn []
          (let [{:keys [control-list path rule-item margin-left api-props]} (r/props (r/current-component))
                {:keys [onQueryChange]} api-props]
            [:div {:style {:margin-left (str margin-left "px") :margin-top "10px"}}
             [:select.btn.btn-primary.btn-sm {:style {:float "left"}}
                                       :value (-> rule-item second :combinator)
                                       :on-change
                                       (fn [e]
                                         (set-ruls-value control-list (conj path :combinator) (.. e -target -value) nil))


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
                 (as-> control-list $
                       (update-in $  (conj path :rules)
                         (fn [rule]
                           (let [new-uuid (-> (random-uuid) str keyword)]
                             (conj rule {:id new-uuid
                                         :field (-> fields first :name)
                                         :value ""
                                         :operator (-> operators first :name)}))))
                  (update-control-list $ nil)))}
              "+ Rule"]

             [:button.btn.btn-primary.btn-sm
              {:on-click
               (fn [e]
                 (as-> control-list $
                   (update-in $ (conj path :rules)
                     (fn [rule]
                       (let [new-uuid (-> (random-uuid) str keyword)]
                         (conj rule {:id new-uuid
                                     :rules []
                                     :combinator (-> combinators first :name)}))))
                  (update-control-list $ nil)))}
              (str "+ Group")]

             [:button.btn.btn-primary.btn-sm
              {:on-click
               (fn [e]
                 (swap! query-controls
                    update-in (drop-last path)
                        (fn [rule]
                          (reduce
                            (fn [acc item]
                              (if-not (= (:id item) (:id rule-item))
                                (conj acc item)
                                acc))
                            []
                            rule))))}
              "x"]]))]



    (r/create-class
      {
        :reagent-render render})))

;
(defn build-widgets [props]
  (let [{control-list :control-list
         margin-left :margin-left
         api-props :api-props
         path :path
         query-controls :query-controls} props
        res
        (into [:div.query-container]
          (:controls
            (reduce
              (fn [acc item]
                (let [keys-item (-> item  keys set)]
                    (as-> acc $
                      (update $ :index inc)
                      (if-not (nil? (keys-item :rules))
                        (update $ :controls
                          (fn [cntrls]
                            (conj cntrls [widget-rule-component
                                          {:control-list query-controls
                                           :path (conj path (:index $))
                                           :rule-item item
                                           :margin-left margin-left
                                           :api-props api-props}])))
                        (update $ :controls
                          (fn [cntrls]
                            (conj cntrls [widget-field-component
                                          {:control-list query-controls
                                           :path (conj path (:index $))
                                           :field-item item
                                           :margin-left margin-left
                                           :api-props api-props}]))))


                      (if (> (-> item :rules count) 0)
                        (update $ :controls
                          (fn [cntrls]
                            (conj cntrls (build-widgets {:control-list (-> item :rules)
                                                         :margin-left (+ margin-left 20)
                                                         :api-props api-props
                                                         :path (conj path (:index $) :rules)
                                                         :query-controls query-controls}))))
                        $))))
              {:controls [] :index -1}
              control-list)))]
    res))


(defn query-builder-component [props]
  (let [
        did-mount
        (fn [])

        render
        (fn []
          (let [api-props (r/props (r/current-component))
                control-list
                (if (nil? (:control-list api-props)) @query-controls (:control-list api-props))]
            (js/console.log "TREE LIST >>>" (clj->js control-list))
            (println "QUERY STATEMENT >>>" (with-out-str (cljs.pprint/pprint @query-str)))
            [:div.col-lg-12
             [:div.col-lg-12 {:style {:min-height "550px"}}
              [:h4 "Query Builder Container"]
              (build-widgets
                {:control-list control-list
                 :margin-left 0
                 :api-props
                   (as-> api-props $
                     (if (nil? (:fields api-props))
                       (assoc $ :fields fields) $)
                     (if (nil? (:operators api-props))
                      (assoc $ :operators operators) $))
                 :path []
                 :query-controls control-list})]


             [:div.col-lg-12 {:style {:min-height "500px"}}
              [:pre (with-out-str (cljs.pprint/pprint @query-str))]]]))]




    (r/create-class
      {
        :component-did-mount did-mount
        :reagent-render render})))
