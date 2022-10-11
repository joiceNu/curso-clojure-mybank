(ns mybank-web-api.core
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.test :as test-http]
            [io.pedestal.interceptor :as i]
            [io.pedestal.interceptor.chain :as chain]
            [clojure.pprint :as pp])
  (:gen-class))

<<<<<<< HEAD
(defonce contas (atom {:1 {:saldo 100}
                   :2 {:saldo 200}
                   :3 {:saldo 300}}))
=======
(defn quadrado [x] (* x x))

(defonce server (atom nil))

(defonce contas- (atom {:1 {:saldo 100}
                       :2 {:saldo 200}
                       :3 {:saldo 300}}))

(defn hello [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello!"})

(defn add-contas-atom [context]
  (assoc context :contas contas-))

(def contas-interceptor
  {:name  :contas-interceptor
   :enter add-contas-atom})

;(def widget-finder
;  (i/interceptor
;    {:enter (fn [ctx]
;              (assoc ctx :widget {:id 1 :title "foobar"} ))}))

(def widget-finder
  (i/interceptor
    {:enter (fn [ctx]
              (assoc ctx :widget {:id 1 :title (:title "foobar")} ))}))

(def widget-renderer
  (i/interceptor
    {:leave (fn [ctx]
              (if-let [widget (:widget ctx)]
                (assoc ctx :response {:status 200
                                      :body   (format "Widget ID %d, Title '%s'"
                                                      (:id widget)
                                                      (:title widget))})
                (assoc ctx :response {:status 404 :body "Not Found"})))}))
>>>>>>> upstream/Unit-Tests

(defn get-saldo [request]
  (let [contas (request :contas)
        id-conta (-> request :path-params :id keyword)]
    (println (str "CONTAS=>=>" @contas ))
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body (id-conta @contas "conta inválida!")}))

(defn make-deposit [request]
  (let [contas (request :contas)
        id-conta (-> request :path-params :id keyword)
        valor-deposito (-> request :body slurp parse-double)
        SIDE-EFFECT! (swap! contas (fn [m] (update-in m [id-conta :saldo] #(+ % valor-deposito))))]
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body {:id-conta   id-conta
                        :novo-saldo (id-conta @contas)}}))

(def routes
  (route/expand-routes
    #{
      ["/hello" :get hello :route-name :hello]
      ["/saldo/:id" :get get-saldo :route-name :saldo]
      ["/deposito/:id" :post make-deposit :route-name :deposito]}))


(def service-map-simple {::http/routes routes
                         ::http/port   9999
                         ::http/type   :jetty
                         ::http/join?  false})

(def service-map (-> service-map-simple
                     (http/default-interceptors)
                     (update ::http/interceptors conj (i/interceptor contas-interceptor))))

(defn create-server []
  (http/create-server
<<<<<<< HEAD
    {::http/routes routes
     ::http/type   :jetty
     ::http/port   8890
     ::http/join?  false}))

(defonce server (atom nil))
=======
    service-map))
>>>>>>> upstream/Unit-Tests

(defn start []
  (reset! server (http/start (create-server))))

<<<<<<< HEAD
(defn test-request [server verb url]
  (test-http/response-for (::http/service-fn @server) verb url))
(defn test-post [server verb url body]
  (test-http/response-for (::http/service-fn @server) verb url :body body))
(comment
  (start)
  (http/stop @server)

  (test-request server :get "/saldo/1")
  (test-request server :get "/saldo/2")
  (test-request server :get "/saldo/3")
  (test-request server :get "/saldo/4")

  (test-post server :post "/deposito/2" "863.99")
)
=======

>>>>>>> upstream/Unit-Tests
