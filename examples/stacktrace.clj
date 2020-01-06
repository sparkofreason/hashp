(ns stacktrace
  (:require [sparkofreason.hashtag.core :as ht :refer [defhashtag]]
            [clj-stacktrace.core :as stacktrace]
            [clojure.pprint :refer [pprint]]))

(defn current-stacktrace []
  (->> (.getStackTrace (Thread/currentThread))
       (drop 5)
       (stacktrace/parse-trace-elems)))

(defn ppst
  [x]
  (let [st (->> (current-stacktrace)
                (filter :clojure)
                (filter #(= (:ns x) (:ns %))))]
    (pprint (assoc x :stacktrace st))))

(defhashtag pp ppst)

(defn f
  [x]
  (let [a #pp (inc x)
        b #pp (* 2 a)]
    (dec b)))

(defn g
  [x]
  (* 3 #pp (f x)))

(g 5)
