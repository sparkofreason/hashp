(ns hashpp
  (:require [hashtag.core :as ht :refer [defhashtag]]
            [clojure.pprint :refer [pprint]]))

(defhashtag pp pprint)
(defhashtag pp-locals pprint :locals? true)
(defhashtag pp-fn pprint :stacktrace-tx ht/current-frame)
(defhashtag pp-clojure pprint :stacktrace-tx ht/clojure-frames)
(defhashtag pp/all pprint :stacktrace-tx ht/all-frames)

(def my-stacktrace (comp (filter :clojure)
                         (filter #(= "hashpp" (:ns %)))
                         (map #(select-keys % [:fn :line]))))
(defhashtag pp/myst pprint :locals? true :stacktrace-tx my-stacktrace)

(pp-fn '(inc x))
(defn f
  [x]
  (let [a #pp-fn (inc x)
        b #pp-fn (* 2 a)]
    (dec b)))

(defn g
  [x]
  (* 3 #pp-fn (f x)))

(g 5)

(inc #pp (* 2 #pp (+ 3 #pp (* 4 5))))
