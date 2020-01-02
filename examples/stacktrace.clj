(ns stacktrace
  (:require [hashtag.core :as ht :refer [defhashtag]]
            [clj-stacktrace.core :as stacktrace]
            [clojure.pprint :refer [pprint]]))

;;; hashtag currently provides a mechanism to process and reutnr the stacktrace.
;;; However, unlike local bindings, stacktrace is available at runtime
;;; (vs. compile-time), and so can be grabbed by a custom handler function.
;;; Benefits:
;;; * Removes the only dependency (clj-tacktrace) from hashtag
;;; * Allows handler authors to process stacktraces as required, based
;;; on the environment, other tooling, whatever, rather than forcing
;;; default choices (e.g. dropping the first 3 frames by default.)
;;; * Simplifies extenstion of hashtag to ClojureScript.

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
