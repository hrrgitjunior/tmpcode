(ns ajaxreagent.htmltable
    (:require
       [reagent.core :as r]))

;

(defn simple-html-table [props]
  (let [{:keys [container plot]} props
        did-mount
        (fn [])
         ;(swap! query-str #(build-query @query-controls)))

        render
        (fn []
          (println "RENDER HTML TABLE")
          [:div.col-lg-12
           [:h3 "Simple Html Table"]
           [:table {:border (str "1px" "solid" "black")}
            [:thead {:style {:background-color "#eef"}}
              [:tr
                [:th "AAA"]
                [:th "BBB"]
                [:th "CCC"]]]
            (into [:tbody]
              (->> [1 2 3]
                (mapv (fn [row]
                        (into [:tr]
                          (->> ["aaa" "bbb" "ccc"]
                            (mapv (fn [item]
                                   [:td {:style {:border  "1px solid black"}} item]))))))))]])]




    (r/render [
               (r/create-class
                 { :component-did-mount did-mount
                   :reagent-render render})]
            container)))
;

(defn simple-html-table-2 [props]
  (let [{:keys [container plot]} props
        did-mount
        (fn [])
         ;(swap! query-str #(build-query @query-controls)))

        render
        (fn []
          (println "RENDER HTML TABLE")
          [:div.col-lg-12 {:style {:margin "0px"}}
           [:h3 "Simple Html Table"]
           [:table {:border (str "1px" "solid" "black")}
            [:thead {:style {:background-color "#eef"}}
              [:tr
                [:th "AAA"]
                [:th "BBB"]
                [:th "CCC"]]]
            [:tbody
             (into [:tr]
               (->> ["aaa" "bbb" "ccc"]
                 (mapv (fn [item]
                        [:td {:style {:border  "1px solid black"}}
                         (into [:div]
                           (->> ["Габрово" "Севлиево" "Русе"]
                             (map (fn [city]
                                    [:div city]))))]))))]]])]

    (r/render [
               (r/create-class
                 { :component-did-mount did-mount
                   :reagent-render render})]
            container)))

;
(defn simple-gallery [props]
  (let [{:keys [container plot]} props
        did-mount
        (fn [])
         ;(swap! query-str #(build-query @query-controls)))

        render
        (fn []
          [:div.row
           [:div.col-lg-12
            [:h3.text-center  "Simple Gallery 123"]
            [:div {:style {:width "70%" :margin "auto"}}
             [:div {:style {:width "70%" :margin "auto"}}
              [:p "Това е метод в машинната бродерия, при който с помоща на специални бодове, бродерията се доближав до оригинала"
               [:br]
               "Този метод е подходящ за машинна бродерия на:"
               [:br]]
              [:p.text-center "лица на хора"]]
             [:table {:border (str "1px" "solid" "black") :style {:margin "auto"}}
              (into [:tbody]
                 ;(->> [["flow_1.jpeg" "flow_2.jpeg" "flow_1.jpeg"] ["flow_1.jpeg" "flow_2.jpeg" "flow_1.jpeg"]]
                 (->> [["flow_1.jpeg" "flow_2.jpeg" "flow_1.jpeg"]]
                   (mapv (fn [row]
                           (into [:tr]
                             (->> row
                               (mapv (fn [item]
                                      [:td {:style {:border  "1px solid black"}}
                                        [:div {:style {:height 250}}
                                         [:img {:src item}]]
                                        [:div
                                         [:ul
                                          [:li {:style {:display "inline" :margin-right 10}}
                                           [:button "Button1"]]
                                          [:li {:style {:display "inline"}}
                                           [:button "Button2"]]]]]))))))))]


             [:div {:style {:width 150 :background-color "#0f0" :margin-left "auto" :margin-right "auto" :margin-top 10}}
              [:div {:style {:float "left" :margin-right 10}} "перспектива"]
              [:div  "цвят"]]]]])]

           ; [:div.row
           ;    [:div.col-lg-1 "AAA"]
           ;    [:div.col-lg-5 "BBB"]]
           ; [:div.row
           ;    [:div.col-lg-1
           ;     [:input {:type "text" :value "AAA"}]]
           ;
           ;    [:div.col-lg-5
           ;     [:input {:type "text" :value "BBB"}]]
           ;
           ;    [:div.col-lg-1
           ;     [:input {:type "text" :value "CCC"}]]]])]
           ;


    (r/render [
               (r/create-class
                 { :component-did-mount did-mount
                   :reagent-render render})]
            container)))
