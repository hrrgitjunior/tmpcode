(ns ajaxreagent.treecomponent222
    (:require
       [reagent.core :as r]
       [clojure.pprint :refer [pprint]]))

;
(def beg-uuid (str (random-uuid)))
(def tree (r/atom [{:id beg-uuid :index 0 :path [ 0 :rules] :rules [] :combinator "and"}]))
(def query-str (r/atom "aaa"))
(def fields [
             {:name "firstName" :label "First Name"}
             {:name "lastName" :label "Last Name"}])
(def combinators
  [
      {:name "and" :label "AND"}
      {:name "or"  :label "OR"}])

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


;
(def m
      {
        :id "g-b93ab44f-497a-4097-bd77-f31ae0954801",
        :rules [
                {
                  :id "g-5f20e467-404e-4626-92d0-808a08e66912",
                  :rules []
                  :combinator "and"}

                {
                  :id "g-b6bd5da9-5940-4792-b721-f1ec9399f1b3",
                  :rules []
                  :combinator "and"}]

        :combinator "and"})

(defn build-query [tree-list]
  (let [res
        (reduce
          (fn [acc item]
            (let [keys-item (-> item keys set)
                  new-item {}]
              (if-not (nil? (keys-item :rules))
                (do
                    (as-> new-item $
                      (assoc $ :id (:id item))
                      (if (> (count (:rules item)) 0)
                        (as-> $ $$
                          (assoc $$ :rules (build-query (:rules item)))
                          (assoc $$ :combinator (:combinator item)))
                        (assoc $ :combinator (:combinator item)))
                      (conj acc $)))

                (conj acc {:id (:id item) :field (:field item)}))))
          []
          tree-list)]
    (js/console.log (clj->js res))
    res))



(defn tree-child-component [props]
  (let [
        props-state props
        render
        (fn []
          (fn []
            (let [{:keys [field-item margin-left fields operators]} props-state
                  path (:path field-item)]

              [:div {:style {:margin-left (str margin-left "px") :margin-top "10px"}}
               [:select.btn.btn-info.btn-sm {:style {:float "left"}}
                  (map
                    (fn [item]
                      [:option  {:key (:name item) :value (:name item)} (:label item)])
                    fields)]

               [:select.btn.btn-info.btn-sm {:style {:float "left"}}
                   (map
                     (fn [item]
                       [:option {:key (:name item) :value (:name item)} (:label item)])
                     operators)]


               [:input {:type "text"}]
               [:button.btn.btn-info.btn-sm "remove"]])))]


    (r/create-class
      {
        :reagent-render render})))


(defn tree-rule-component [props]
  (let [
        props-state props
        render
        (fn []
          (let [{:keys [rule-item margin-left combinators]} props-state
                path (:path rule-item)]
            [:div {:style {:margin-left (str margin-left "px") :margin-top "10px"}}
             [:select.btn.btn-primary {:style {:float "left"}}
              (map
                (fn [item]
                  (println "+++++" item)
                  [:option {:key (:name item) :value (:name item)} (:label item)])
                combinators)]

             [:button.btn.btn-primary {:style {:float "left"}
                                       :on-click
                                        (fn [e]
                                          (swap! tree
                                            (fn [obj]
                                              (update-in obj path
                                                (fn [rule]
                                                  (conj rule {:id (str (random-uuid))
                                                              :index (count rule)
                                                              :path
                                                                (as-> path $
                                                                  (conj $ (count rule))
                                                                  (conj $ :rule))
                                                              :field ""
                                                              :value ""
                                                              :operator ""}))))))}

               "Add Rule"]

             [:button.btn.btn-primary
              {:on-click
               (fn [e]
                 (swap! tree
                   (fn [obj]
                     (update-in obj path
                       (fn [rule]
                         (conj rule {:id (str (random-uuid))
                                     :index (count rule)
                                     :path
                                       (as-> path $
                                         (conj $ (count rule))
                                         (conj $ :rules))
                                     :rules []
                                     :combinator "and"}))))))}


              (:id rule-item)];ле)"Add Group"]

             [:button
              {:on-click
               (fn [e]
                 (swap! tree
                   (fn [obj]
                     (update-in obj
                       (if (> (count path) 2)
                         (-> path drop-last drop-last)
                         path)
                       (fn [rule]
                         (reduce-kv
                           (fn [acc idx item]
                             (if (>= idx (:index rule-item)) acc
                               (conj acc item)))
                           []
                           rule))))))}



              "Remove"]



  ;-----------render childs ---------------
             (into [:div.tree-child]
               (map
                 (fn [item]
                   [tree-child-component])
                (:child rule-item)))]))]



    (r/create-class
      {
        :reagent-render render})))

;
(defn build-tree [tree-list margin-left]
  (let [res
        (into [:div.tree-container]
          (reduce
            (fn [acc item]
              (let [keys-item (-> item keys set)]
                (if-not (nil? (keys-item :rules))
                  (as-> (conj acc [tree-rule-component {:rule-item item :margin-left margin-left :combinators combinators}]) $
                    (if (> (count (:rules item)) 0)
                      (conj $ (build-tree (:rules item) (+ margin-left 20)))
                      $))
                  (conj acc [tree-child-component {:field-item item :margin-left margin-left :fields fields :operators operators}]))))
            []
            tree-list))]
    res))


(defn tree-container-component [props]
  (let [{:keys [container]} props

        render
          (fn []
            (let [rules
                  (map (fn [item] (:rule item)) @tree)]


              (println m)
              (js/console.log "TREE LIST >>>" (clj->js @tree))
              [:div.col-lg-12
               [:div.col-lg-12
                 [:h4 "Tree container"]
                 (build-tree @tree 0)
                 [:button
                  {:on-click
                   (fn [e]
                     (swap! query-str (fn [obj] (build-query @tree))))}
                  "Build query"]]
               [:div.col-lg-12 {:style {:min-height "500px"}}
                ;[:pre @query-str]
                [:pre (with-out-str (cljs.pprint/pprint @query-str))]]]))]


    (r/render [
               (r/create-class
                 {
                   :reagent-render render})]
            container)))
