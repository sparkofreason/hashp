(ns hashpp
  (:require [hashtag.core :as ht :refer [defhashtag]]
            [clojure.pprint :refer [pprint]]))

(defhashtag pp pprint)
(defhashtag pp-locals pprint :locals? true)
(defhashtag pp-fn pprint :stacktrace-tx ht/current-frame)
(defhashtag pp-clojure pprint :stacktrace-tx ht/clojure-frames)
(defhashtag pp-all pprint :stacktrace? true)

(def my-stacktrace (comp (filter :clojure)
                         (filter #(= "hashpp" (:ns %)))
                         (map #(select-keys % [:fn :line]))))
(defhashtag pp-myst pprint :locals? true :stacktrace-tx my-stacktrace)
