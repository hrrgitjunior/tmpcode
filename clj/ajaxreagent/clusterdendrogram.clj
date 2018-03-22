(ns ajaxreagent.clusterdendrogram
  (require [clojure.data.json :as json]))

(defn cluster-dendrogram []
  (let [plot-data
        (json/read-str (slurp "resources/public/test.json")
                       :key-fn keyword)]
    {:plot-data plot-data}))
