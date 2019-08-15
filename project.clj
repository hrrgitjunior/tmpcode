(defproject ajaxreagent "0.1.0-SNAPSHOT"
 :description "a simple app with ring+compojure+cljs"
 :url "http://rockyj.in"
 :min-lein-version "2.0.0"
 :source-paths ["src/clj"]
 :dependencies [;[org.clojure/clojure "1.9.0-beta4" :scope "provided"]; 1.7.0 1.9.0-beta4
                [org.clojure/clojure "1.7.0"]
                [org.clojure/clojure "1.8.0" :scope "provided"]
                [compojure "1.6.0"]
                [ring/ring-defaults "0.1.5"]
                [ring/ring-json "0.4.0"]
                ;;ClojureScript
                [org.clojure/clojurescript "1.9.946" :scope "provided"]; 1.7.122
                [reagent "0.5.1"]
                [hiccup "1.0.5"]
                [cljs-ajax "0.5.1"]
                [prismatic/dommy "1.1.0"]
                [net.sourceforge.dynamicreports/dynamicreports-core "5.0.0"
                  :exclusions
                  [ com.fasterxml.jackson.core/jackson-core
                    com.fasterxml.jackson.core/jackson-annotations]]
                [reagent "0.6.2" :exclusions [cljsjs/react cljsjs/react-dom]]]
                ;



 :plugins [[lein-ring "0.8.13"]
           [lein-figwheel "0.5.14"]]

 :ring {:handler ajaxreagent.handler/app}
 :cljsbuild {
             :builds [ { :id "ajaxreagent"
                        :source-paths ["src/cljs"]
                        :figwheel true
                        :compiler {:main "ajaxreagent.app"
                                   ; :npm-deps {:react "15.6.1" :react-dom "15.6.1" :left-pad "1.1.3" :react-querybuilder "1.4.0" :react-player "1.1.1"} ;; NEW
                                   ; :install-deps true
                                   :asset-path "js/out"
                                   :output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"

                                   :foreign-libs [{:file "public/js/bundle.js"
                                                   :provides ["cljsjs.react" "cljsjs.react.dom" "webpack.bundle"]}]}}]}

 ; :profiles
 ; {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
 ;                       [ring/ring-mock "0.3.0"]]}}


 :profiles {:dev {:dependencies [[binaryage/devtools "0.9.7"]
                                 [binaryage/dirac "1.3.8"]
                                 [figwheel-sidecar "0.5.16"]
                                 [proto-repl "0.3.1"]
                                 [cider/piggieback "0.4.1"]
                                 [nrepl "0.6.0"]]
                                 ; [org.clojure/tools.nrepl "0.2.13"]]
                  ;; need to add dev source path here to get user.clj loaded
                  :source-paths ["src" "dev" "test"]
                  ;; for CIDER
                  ;; :plugins [[cider/cider-nrepl "0.12.0"]]
                  ; :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
                  :repl-options {:port 8230
                                 :nrepl-middleware [cider.piggieback/wrap-cljs-repl dirac.nrepl/middleware]
                                 :init (do
                                         (require 'dirac.agent)
                                         (dirac.agent/boot!))}}})
