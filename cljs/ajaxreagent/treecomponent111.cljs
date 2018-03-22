(ns ajaxreagent.treecomponent111
    (:require
       [reagent.core :as r]))



(defn tree-child-component []
  (let [
        render
        (fn []
          [:div
           [:button.btn.btn-info.btn-sm {:style {:float "left"}} "name"]
           [:button.btn.btn-info.btn-sm {:style {:float "left"}} "combinatory"]
           [:button.btn.btn-info.btn-sm {:style {:float "left"}} "editor"]
           [:button.btn.btn-info.btn-sm "remove"]])]


    (r/create-class
      {
        :reagent-render render})))

(defn tree-root-component [props]
  (let [
        props-state props
        render
        (fn []
          (let [{:keys [tree-list margin-left]} props-state]
            (println "+ROOT COMP+++" (count (:child tree-list)))
            [:div {:style {:margin-left (str margin-left "px") :margin-top "10px"}}
             [:button.btn.btn-primary {:style {:float "left"}} "Logic"]

             [:button.btn.btn-primary {:style {:float "left"}
                                       :on-click
                                        (fn [e]
                                          (swap! tree-list
                                            (fn [obj] (conj obj {:root {:child {}}}))))}
               "Add Rule"]

             [:button.btn.btn-primary
              "Add Group"]

             (into [:div.tree-child]
               (map
                 (fn [item]
                   [tree-child-component])
                (:child tree-list)))]))]



    (r/create-class
      {
        :reagent-render render})))

;
(defn build-tree [tree-list margin-left]
  (println "+++" tree-list)
  (let [res
        (into [:div.tree-container]
          (reduce
            (fn [acc item]
              (println "@@@" item)
              (if (> (count (:root item)) 0)
                (as-> acc $
                  (conj $ [tree-root-component {:tree-list item :margin-left margin-left}])
                  (conj $ (build-tree (:root item) (+ margin-left 20))))
                (conj acc [tree-root-component {:tree-list item :margin-left margin-left}])))
            []
            tree-list))]
    (println "===" res)
    res))


(defn tree-container-component [props]
  (let [{:keys [container]} props

        tree (r/atom [{:root [{:root [] :child []}] :child [{}{}]}
                      {:root [{:root [{:root [] :child []}]
                               :child [{}]}]}
                      {:root [] :child []}])

        render
          (fn []
            (let [roots
                  (map (fn [item] (:root item)) @tree)]

              [:div {:style {:min-width "310px" :max-width "100%"
                             :height "100%" :margin-right "10px"}}
                [:h4 "Tree container"]

                (build-tree @tree 0)]))]
                ; (into [:div.tree-container]
                ;   (map
                ;     (fn [item]
                ;       [tree-root-component {:tree-list tree}])
                ;     roots))]))]

    (r/render [
               (r/create-class
                 {
                   :reagent-render render})]
            container)))
