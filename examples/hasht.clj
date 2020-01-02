(ns hasht
  (:require [hashtag.core :as ht :refer [defhashtag]]
            [clojure.pprint :refer [pprint]]))

(defhashtag t tap>)

(defn mean [xs]
  (/ (double #t ^{:t :foo} (reduce + xs)) #t ^{:t :bar} (count xs)))

(defn tap-fn
  [x]
  (case (get-in x [:metadata :t])
    :foo (println "FOOOOOOOOOO!")
    :bar (println "BAAAAAAAAAR!"))
  (pprint (select-keys x [:form :result])))

(add-tap tap-fn)

(mean [1 4 5 2])

(Thread/sleep 1000)
(remove-tap tap-fn)
