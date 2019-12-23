(ns hashtag.core
  (:require [clojure.walk :as walk]
            [clojure.spec.alpha :as s]))

(s/def ::locals (s/map-of keyword? any?))

(s/def ::form any?)
(s/def ::result any?)

(s/def ::debug-data (s/keys :req-un [::form ::result] :opt-un [::locals]))

(s/def ::locals? boolean?)

(def result-sym (gensym "result"))

(defn- hide-p-form [form]
  (if (and (seq? form)
           (vector? (second form))
           (= (-> form second first) result-sym))
    (-> form second second)
    form))

(defmacro locals
  []
  (->> &env
       (map (fn [[name _]] `[~(keyword name) ~name]))
       (into {})))

(def default-opts {:locals? false})

(defn make-hashtag
  [handler-fn-sym opts]
  (let [opts (merge default-opts opts)]
    (fn [form]
      (let [orig-form (walk/postwalk hide-p-form form)
            locals? (:locals? opts)]
        `(let [locals# (when ~locals? (locals))
               ~result-sym ~form
               debug-data# (cond-> {:form '~orig-form :result ~result-sym}
                                   ~locals? (assoc :locals locals#))]
           (~handler-fn-sym debug-data#)
           ~result-sym)))))

(defmacro defhashtag
  "Defines and registers a \"tagged literal\" reader macro which calls hander-fn
   with data for debugging the tagged form.
      * id - the name of the tag, e.g. p -> #p, foo/bar -> #foo/bar.
      * handler-fn - a function of one argument with spec :hashtag.core/debug-data.
      * opts - option key/value pairs.
         ** :locals? (false) - Default false. includes local bindings as a map."
  [id handler-fn & {:as opts}]
  (let [id' (-> id str (clojure.string/replace #"/" "-") symbol)]
    `(do
       (def ~id'
         (make-hashtag ~handler-fn '~opts))
       (set! *data-readers* (assoc *data-readers* '~id ~id'))
       #'~id')))
