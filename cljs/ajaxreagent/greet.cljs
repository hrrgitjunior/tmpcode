(ns ajaxreagent.greet
  (:require [ajax.core :refer [GET POST]]
            [dommy.core :as dommy :refer-macros [sel sel1]]
            [reagent.core :as r]))


(defn handler [response]
  (.log js/console "server responded...")
  (let [resjs
        (-> (clj->js response)  (aget  "body" "greeting"))]
    ; (js/console.clear)
    ; (js/console.profileEnd())
    (r/render
        [:div.row
          [:div.col-lg-4]
          [:div.col-lg-8
           [:h3 {:style {:margin-top "300px" :color "#5af"}}
            (str "Hello From " resjs)]]]
        (.getElementById js/document "maincontainer-id"))))


(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn say-hello []
  (POST "/hello"
        {:params {:user "from Server!"}
         :handler handler
         :error-handler error-handler
         :format :json
         :response-format :json}))

;
(defn test2 []
  (.log js/console "Hello Cljs from test!"))
