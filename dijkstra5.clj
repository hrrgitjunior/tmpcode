(ns hrr.dijkstra5)

;(def graph [{:name "a0" :from nil :dist 0
;             :child
;              [{:a1 3}
;               {:a2 2}]}
;            {:name "a1" :from nil :dist nil
;             :child
;               [{:a3 3}]}
;            {:name "a2" :from nil :dist nil
;             :child
;              [{:a3 4}]}
;            {:name "a3" :from nil :dist nil
;             :child nil}])



(def graph [{:name "s" :from nil :dist 0
             :child
              [{:a1 3}
               {:a2 5}
               {:a3 7}]}

            {:name "a1" :from nil :dist nil
             :child
                   [{:a4 4}]}

            {:name "a2" :from nil :dist nil
             :child
              [{:a4 2}
               {:a5 2}]}

            {:name "a3" :from nil :dist nil
             :child
             [{:a5 3}
              {:a6 2}]}

            {:name "a4" :from nil :dist nil
             :child
             [{:t 3}]}

            {:name "a5" :from nil :dist nil
             :child
             [{:a7 20}
              {:a6 2}]}

            {:name "a6" :from nil :dist nil
             :child
             [{:t 10}]}

            {:name "a7" :from nil :dist nil
             :child
             [{:t 5}]}

            {:name "t" :from nil :dist nil
             :child nil}])
;






(defn get-children [node tmp-path]
  (let [res
        (->> tmp-path (filter #(= (:name %) node)) first :child)
        nodes (map :name tmp-path)]
    (->> (into {} res)
      (reduce
        (fn [acc [k v]]
          (if-not (nil? ((set nodes) (name k)))
            (conj acc {k v})
            acc))
        []))))





(defn remove-node [name tmp-path]
  (let [res
        (->> tmp-path (filter #(not= (:name %) name)))]
    res))



(defn sort-tmp-path [tmp-path]
  (let [res
        (sort-by :dist #(and (and (not (nil? %1)) (not (nil? %2)))
                             (< %1 %2)) tmp-path)]
    (println "==== sort ====" res)
    res))


(defn array-to-hash [tmp-path]
  (let [res
        (->> tmp-path
          (reduce
            (fn [acc item]
              (assoc acc (-> item :name keyword) {:from (:from item) :dist (:dist item)}))
            {}))]
    res))

(defn hash-to-array [hash-path tmp-path]
  (let [res
        (->> tmp-path
          (map
            (fn [item]
              (let [node-name (as-> (:name item) $ (keyword $))]
                 (assoc item :from (-> hash-path node-name :from)
                             :dist (-> hash-path node-name :dist))))))]

    res))



(defn modify-tmp-path [from-node children tmp-path min-dist]
  (let [hash-path
        (array-to-hash tmp-path)
        res
        (->> (into {} children)
          (reduce
            (fn [acc [name-child dist-child]]
              (let [dist-node (-> hash-path name-child :dist)]

                (if (or (nil? dist-node)
                        (> dist-node (+ min-dist dist-child)))

                    (as-> acc $
                      (assoc-in $ [name-child :dist] (+ min-dist dist-child))
                      (assoc-in $ [name-child :from] from-node))

                 acc)))
            hash-path))]
    (hash-to-array res tmp-path)))


(defn get-node-name [node]
  (-> node first name))

(defn walk-tmp-path [end-node find-short-path tmp-path]
  (let [prev-node
        (as-> ((keyword end-node) tmp-path) $
              (if-not (nil? $)
                (-> $  keys first name)
                $))
        end-dist (-> ((keyword end-node) tmp-path) vals first)]
    (as-> find-short-path $
          (if-not (nil? prev-node)
            (walk-tmp-path prev-node (conj $ {(keyword end-node) end-dist}) tmp-path)
            (conj $ {(keyword end-node) end-dist})))))


(defn short-path [from-node end-node node-path beg-dist]
  (println "=== short path ====" from-node  beg-dist)
  (let [children (get-children from-node (:tmp-path node-path)) ;get min-node
        beg-dist (-> (:tmp-path node-path) first :dist)
        final-path (:final-path node-path)
        node-path (as-> node-path $
                        (as-> (:tmp-path $) $$
                           (remove-node from-node $$)
                           (modify-tmp-path from-node children $$ beg-dist)
                           (sort-tmp-path $$) ;-> find min distance
                           (if (not= (-> $$ first :name) end-node)
                             (let [
                                   min-node (-> $$ first :name) ; get first node from tmp-path -> min dist
                                   min-from (-> $$ first :from)
                                   min-dist (-> $$ first :dist)
                                   final-path (as-> final-path  $fp
                                                    (assoc $fp (keyword min-node) {(keyword min-from) min-dist})) ; added min node to final-path
                                   {tmp-path :tmp-path  final-path :final-path}
                                   (short-path min-node end-node
                                               {:tmp-path $$ :final-path final-path} ;recur with min-node
                                               min-dist)]
                               {:tmp-path tmp-path :final-path final-path})
                             (do
                               (println "+++++ BEFORE WALK +++" $$)
                               (let [
                                     dij-end-node (->> $$ (filter #(= (:name %) end-node)))
                                     from-node (-> dij-end-node first :from)
                                     dist-node (-> dij-end-node first :dist)
                                     final-path
                                     (as-> final-path $fp
                                           (assoc $fp (keyword end-node) {(keyword from-node)
                                                                          dist-node})
                                           (walk-tmp-path end-node [] $fp))] ;walk final-path
                                 {:tmp-path $$ :final-path final-path})))))]

       node-path))



(defn sort-test []
  (let [arr [{:node "a1" :from "a0" :dist 5} {:node "a2" :from "a0" :dist 7}]
        sb
        (sort-by :dist #(> %1 %2) arr)]

    sb))