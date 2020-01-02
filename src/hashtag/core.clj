(ns hashtag.core
  (:require [hash-meta.core :as hash-meta]
            [clj-stacktrace.core :as stacktrace]
            [net.cgrand.macrovich :as macros]
            [clojure.walk :as walk]
            [clojure.spec.alpha :as s]))


(s/def ::file string?)
(s/def ::line pos-int?)
(s/def ::java boolean?)
(s/def ::clojure boolean?)
(s/def ::class string?)
(s/def ::method string?)
(s/def ::ns string?)
(s/def ::fn string?)
(s/def ::anon-fn boolean?)
(s/def ::java-frame (s/keys :opt-un [::file ::line ::java ::class ::method]))
(s/def ::clojure-frame (s/keys :opt-un [::file ::line ::clojure ::ns ::fn ::anon-fn]))
(s/def ::stack-frame (s/or :java ::java-frame :clojure ::clojure-frame))
(s/def ::stacktrace (s/coll-of ::stack-frame))

(s/def ::locals (s/map-of keyword? any?))

(s/def ::form any?)
(s/def ::result any?)

(s/def ::debug-data (s/keys :req-un [::form ::result] :opt-un [::locals ::stacktrace]))

(defn current-stacktrace []
  (->> (.getStackTrace (Thread/currentThread))
       (drop 3)
       (stacktrace/parse-trace-elems)))

(defmacro locals
  []
  (->> (macros/case :clj &env :cljs (:locals &env))
       (map (fn [[name _]] `[~(keyword name) ~name]))
       (into {})))

(def all-frames (map identity))
(def clojure-frames (filter :clojure))
(def current-frame (comp clojure-frames (take 1)))

(def default-opts {:locals? false
                   :stacktrace-tx nil})

(defn form->map
  [handler-form form orig-form metadata opts]
  (let [stacktrace? (:stacktrace? opts)
        locals? (:locals? opts)
        clj-form (:clj handler-form)
        cljs-form (:cljs handler-form)]
    `(let [locals# (when ~locals? (locals))
           result-sym# ~form
           handler-form# (or (macros/case :clj ~(:clj handler-form) :cljs ~(:cljs handler-form)) '~handler-form)
           debug-data# (cond-> {:form '~orig-form :result result-sym# :metadata ~metadata}
                               ~locals?
                               (assoc :locals locals#)

                               ~stacktrace?
                               (assoc :stacktrace
                                      (macros/case
                                       :clj  (->> (.getStackTrace (Thread/currentThread))
                                                  (drop 1)
                                                  (stacktrace/parse-trace-elems))
                                       :cljs (->> (ex-info "" {})
                                                  .-stack
                                                  (clojure.string/split-lines)
                                                  (drop 7)
                                                  (drop-while #(clojure.string/includes? % "ex_info"))
                                                  (map #(clojure.string/replace % "    at " ""))))))]
       (handler-form# debug-data#)
       result-sym#)))

(defn make-hashtag
  [handler-form opts]
  (let [opts (merge default-opts opts)]
    `(fn [form# orig-form# metadata#]
       (form->map '~handler-form form# orig-form# metadata# '~opts))))


(defmacro defhashtag
  "Defines and registers a \"tagged literal\" reader macro which calls hander-fn
   with data for debugging the tagged form.
      * id - the name of the tag, e.g. p -> #p, foo/bar -> #foo/bar.
      * handler-fn - a function of one argument with spec :hashtag.core/debug-data.
      * opts - option key/value pairs.
         ** :locals? (false) - Default false. includes local bindings as a map.
         ** :stacktrace-tx (nil) - a transducer to process stackframes as defined in clj-stacktrace"
  [id handler-form & {:as opts}]
  (hash-meta/make-reader id (make-hashtag handler-form opts) true))
