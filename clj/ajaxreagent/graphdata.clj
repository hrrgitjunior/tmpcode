(ns ajaxreagent.graphdata
  (require [clojure.data.json :as json]))

(def graph-data
  { :nodes
      [
       {:id "bm-sample-1" :group 1 :id2 "bm-sample"}
       {:id "bm-sample-2" :group 1 :id2 "bm-sample"}

       {:id "subj-selection-1" :group 2 :id2 "subj-selection"}
       {:id "subj-selection-2" :group 2 :id2 "subj-selection"}

       {:id "bm-definition-1" :group 3 :id2 "bm-definition"}
       {:id "bm-definition-2" :group 3 :id2 "bm-definition"}

       {:id "derived-bm-1" :group 4 :id2 "derived-bm"}
       {:id "derived-bm-2" :group 4 :id2 "derived-bm"}]


    :links
      [
       {:source "bm-definition-1" :target "subj-selection-1" :value 1}
       {:source "subj-selection-1" :target "bm-sample-1" :value 2}
       {:source "bm-definition-2" :target "subj-selection-2" :value 1}
       {:source "subj-selection-1" :target "bm-sample-2" :value 2}
       {:source "derived-bm-1" :target "bm-sample-1" :value 2}
       {:source "derived-bm-1" :target "bm-definition-2" :value 2}]})
