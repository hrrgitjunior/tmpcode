(ns ajaxreagent.matrixtable
    (:require
       [reagent.core :as r]))

(def state
  { :bm-sample-selection
    [ {:uuid "bm-sample-1" :subj-selection "subj-sel-1" :bm-derived "bm-derived-1"}
      {:uuid "bm-sample-2" :subj-selection nil}
      {:uuid "bm-sample-3" :subj-selection "subj-sel-2"}]

    :subject-selection
    [ {:uuid "subj-sel-1" :bm-def "bm-def-1"}
      {:uuid "subj-sel-2" :bm-def "bm-def-2"}]

    :bm-definition
    [ {:uuid "bm-def-1"}
      {:uuid "bm-def-2"}]

    :bm-derived
    [ {:uuid "bm-derived-1"}]})

(defn load-matrix-columns-title []
  (as-> [] $
    (into $
      (->> (:bm-sample-selection state)
        (mapv
          (fn [{:keys [uuid subj-selection]}]
            uuid))))

    (into $
      (->> (:subject-selection state)
        (mapv
          (fn [{:keys [uuid bm-def]}]
            uuid))))

    (into $
      (->> (:bm-definition state)
        (mapv
          (fn [{:keys [uuid]}]
            uuid))))

    (into $
      (->> (:bm-derived state)
        (mapv
          (fn [{:keys [uuid]}]
            uuid))))))


(defn init-matrix-data [columns-title]
  (let [res
        (reduce
          (fn [acc title]
            (conj acc
              (merge {:name title}
                (reduce
                  (fn [acc item]
                    (merge acc {(keyword item) 0}))
                  {}
                  columns-title))))

          []
          columns-title)]
    (println "=== MAKE MATRIX DATA ===")
    (cljs.pprint/pprint res)
    res))


(defn set-value-from-state [matrix columns-title state-field key-field]
  (let [res
        (reduce
          (fn [acc {:keys [uuid] :as item}]
            (let [index (.indexOf columns-title uuid)]
              (if-not (nil? (key-field item))
                (assoc-in acc [index (-> (get item key-field) keyword)] 1)
                acc)))

          matrix
          (state-field state))]
    res))


(defn load-matrix-data [matrix columns-title]
  (as-> matrix $
    (set-value-from-state $ columns-title :bm-sample-selection :subj-selection)
    (set-value-from-state $ columns-title :bm-sample-selection :bm-derived)
    (set-value-from-state $ columns-title :subject-selection :bm-def)
    (set-value-from-state $ columns-title :bm-definition :bm-derived)))

(def matrix-data
  [
   {:bm-s1 0 :subj-sel1 1 :bm-def1 0}
   {:bm-s1 1 :subj-sel1 0 :bm-def1 1}])

(defn draw-head-cell [column-names]
  (into [:tr]
    (->> column-names
      (mapv (fn [column]
              [:th {:style {:border  "1px solid black" :width 100}} column])))))


(defn draw-row-cell [row]
  (into [:tr]
    (->> (keys row)
      (mapv (fn [cell]
              [:td {:style {:border  "1px solid black"
                            :background-color (if (= (cell row) 1) "#aaa" "white")}}
                (cell row)])))))

(defn matrix-table-component [props]
  (let [{:keys [container plot]} props
        did-mount
        (fn [])
         ;(swap! query-str #(build-query @query-controls)))

        render
        (fn []
          (let [columns-matrix-title (load-matrix-columns-title)
                matrix (init-matrix-data columns-matrix-title)
                matrix (load-matrix-data matrix columns-matrix-title)]
            (println "=== RENDER ===" columns-matrix-title)
            [:div.col-lg-12
             [:h5 "Matrix Table"]
             [:table {:border (str "1px" "solid" "black") :style {:margin "auto"}}
              [:thead
                (draw-head-cell (-> matrix first keys))]
              (into [:tbody]
                (mapv
                    (fn [row]
                      (draw-row-cell row))
                    matrix))]]))]




    (r/render [
               (r/create-class
                 { :component-did-mount did-mount
                   :reagent-render render})]
            container)))
