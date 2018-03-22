(ns ajaxreagent.treecomponent333
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
(def tree (r/atom {beg-uuid {:path [beg-uuid] :rules {} :combinator (-> combinators first :name)}}))
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
(defn set-ruls-value [path value]
  (swap! tree
    update-in path
         (fn [] value))
  (swap! query-str #(build-query @tree)))

(defn tree-field-component [props]
  (let [
        props-state props
        render
        (fn []
          (fn []
            (let [{:keys [field-item margin-left fields operators]} (r/props (r/current-component))
                  {path :path} (second field-item)
                  field-item (-> field-item second)
                  rule-uuid (first field-item)]


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
                                               (set-ruls-value (conj path :operator) (.. e -target -value)))}
                   (map
                     (fn [item]
                       [:option {:key (:name item) :value (:name item)} (:label item)])
                     operators)]


               [:input {:type "text"
                        :value (:value field-item)
                        :on-change
                        (fn [e]
                          (set-ruls-value (conj path :value) (.. e -target -value)))}]

               [:button.btn.btn-info.btn-sm
                {:on-click
                 (fn [e]
                   (swap! tree
                      update-in
                         (if (> (count path) 2)
                           (-> path drop-last)
                           path)
                        dissoc rule-uuid))}

                "x"]])))]




    (r/create-class
      {
        :reagent-render render})))



(defn tree-rule-component [props]
  (let [
        render
        (fn []
          (let [{:keys [rule-item margin-left]} (r/props (r/current-component))
                {path :path} (second rule-item)
                rule-uuid (first rule-item)]
            [:div {:style {:margin-left (str margin-left "px") :margin-top "10px"}}
             [:select.btn.btn-primary {:style {:float "left"}
                                       :value (-> rule-item second :combinator)
                                       :on-change
                                       (fn [e]
                                         (set-ruls-value (conj path :combinator) (.. e -target -value)))}



              (map
                (fn [item]
                  [:option {:key (:name item)
                            :value (:name item)}
                      (:label item)])
                combinators)]


             [:button.btn.btn-primary {:style {:float "left"}
                                       :on-click
                                       (fn [e]
                                         (swap! tree
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
                                         (swap! query-str #(build-query @tree)))}

               "+ Rule"]

             [:button.btn.btn-primary
              {:on-click
               (fn [e]
                 (swap! tree
                   update-in (conj path :rules)
                     (fn [rule]
                       (let [new-uuid (-> (random-uuid) str keyword)]
                         (assoc rule new-uuid {:path
                                               (as-> path $
                                                 (conj $ :rules)
                                                 (conj $ new-uuid))
                                               :rules {}
                                               :combinator (-> combinators first :name)}))))
                 (swap! query-str #(build-query @tree)))}


              (str "+ Group")]

             [:button.btn.btn-primary
              {:on-click
               (fn [e]
                 (swap! tree
                    update-in
                       (if (> (count path) 2)
                         (-> path drop-last)
                         path)
                      dissoc rule-uuid)
                (swap! query-str #(build-query @tree)))}

              "x"]]))]



    (r/create-class
      {
        :reagent-render render})))

;
(defn build-tree [tree-list margin-left]
  (let [res
        (into [:div.tree-container]
          (reduce
            (fn [acc item]
              (let [keys-item (-> item second keys set)]
                (if-not (nil? (keys-item :rules))
                  (as-> (conj acc [tree-rule-component {:rule-item item :margin-left margin-left}]) $
                    (if (> (-> item second :rules count) 0)
                      (conj $ (build-tree (-> item second :rules) (+ margin-left 20)))
                      $))
                  (conj acc [tree-field-component {:field-item item :margin-left margin-left :fields fields :operators operators}]))))
            []
            tree-list))]
    res))


(defn tree-container-component [props]
  (let [{:keys [container]} props

        did-mount
        (fn []
          (println "++++ did-mounat")
         (swap! query-str #(build-query @tree)))

        render
        (fn []
          (js/console.log "TREE LIST >>>" (clj->js @tree))
          (js/console.log "QUERY LIST >>>" (clj->js @query-str))
          [:div.col-lg-12
           [:div.col-lg-12 {:style {:min-height "550px"}}
            [:h4 "Tree container"]
            (build-tree @tree 0)]

           [:div.col-lg-12 {:style {:min-height "500px"}}
            [:pre (with-out-str (cljs.pprint/pprint @query-str))]]])]



    (r/render [
               (r/create-class
                 {
                   :component-did-mount did-mount
                   :reagent-render render})]
            container)))
