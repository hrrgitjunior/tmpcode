(ns hrr.dijkstra-q)

; -- run in console : (short-path "s" "t" {:graph graph :q-nodes q-nodes} 0)

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
                   [{:a4 1}
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


(def q-nodes ["s" "a1" "a2" "a3" "a4" "a5" "a6" "a7" "t"])





(defn get-children [node graph-struct]
  (let [res
        (->> (:graph graph-struct) (filter #(= (:name %) node)) first :child)
        q-nodes (:q-nodes graph-struct)]
    (->> (into {} res)
         (reduce
           (fn [acc [k v]]
             (if-not (nil? ((set q-nodes) (name k)))
               (conj acc {k v})
               acc))
           []))))





(defn remove-node [name graph-struct]
  (as-> graph-struct $
   (assoc $ :q-nodes (->> (:q-nodes $) (filter #(not= % name))))))



(defn sort-graph [graph-struct]
  (let [missing-nodes
        (->> (:graph graph-struct)
          (filter #(nil? ((set (:q-nodes graph-struct)) (:name %)))))

        present-nodes
        (->> (:graph graph-struct)
             (filter #(not (nil? ((set (:q-nodes graph-struct)) (:name %))))))

        res
        (sort-by :dist #(and (and (not (nil? %1)) (not (nil? %2)))
                             (< %1 %2)) present-nodes)]


    (println "==== sort ====" (concat res missing-nodes))
    (assoc graph-struct :graph (concat res missing-nodes))))




(defn array-to-hash [graph]
  (let [res
        (->> graph
             (reduce
               (fn [acc item]
                 (assoc acc (-> item :name keyword) {:from (:from item) :dist (:dist item)}))
               {}))]
    res))

(defn hash-to-array [hash-path graph]
  (let [res
        (->> graph
             (map
               (fn [item]
                 (let [node-name (as-> (:name item) $ (keyword $))]
                   (assoc item :from (-> hash-path node-name :from)
                               :dist (-> hash-path node-name :dist))))))]

    res))



(defn modify-graph [from-node children graph-struct min-dist]
  (let [hash-path
        (array-to-hash (:graph graph-struct))
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
    (assoc graph-struct :graph (hash-to-array res (:graph graph-struct)))))


(defn get-node-name [node]
  (-> node first name))

(defn walk-final-path [end-node find-path graph]
  (let [node-item
        (->> graph
          (filter
            #(= (:name %) end-node))
          first)
        node-name (:name node-item)
        from-node (:from node-item)
        dist-node (:dist node-item)
        find-path (assoc find-path (keyword node-name) {(keyword from-node) dist-node})]

    (if-not (nil? from-node)
      (walk-final-path from-node find-path graph)
      find-path)))


(defn short-path [from-node end-node graph-struct beg-dist]
  (println "=== from-node ====" from-node)
  (let [children (get-children from-node graph-struct) ;get min-node
        beg-dist (-> (:graph graph-struct) first :dist)
        q-nodes (:q-nodes graph-struct)
        find-path (:find-path graph-struct)
        new-struct (as-> graph-struct $
                     (remove-node from-node $)
                     (modify-graph from-node children $ beg-dist)
                     (sort-graph $) ;-> find min distance
                     (if (not= (-> $ :graph first :name) end-node)
                       (let [
                             min-node (-> $ :graph first :name) ; get first node from graph -> min dist
                             min-from (-> $ :graph first :from)
                             min-dist (-> $ :graph first :dist)
                             {graph :graph  q-nodes  :q-nodes find-path :find-path}
                             (short-path min-node end-node
                                         $ ;recur with min-node
                                         min-dist)]
                         {:graph graph :q-nodes q-nodes :find-path find-path})
                       (do
                         (do (println "+++++ BEFORE WALK +++" (:graph $)) $)
                         (let [find-path
                               (walk-final-path (-> (:graph $) first :name) {} (:graph $))]
                           (assoc $ :find-path find-path)))))]

    new-struct))


(defn sort-test []
  (let [arr [{:node "a1" :from "a0" :dist 5} {:node "a2" :from "a0" :dist 7}]
        sb
        (sort-by
          (fn [i1 i2]
            (< (:dist i1) (:dist i2)))
          arr)]

    sb))