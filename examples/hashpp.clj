(ns hashpp
  (:require [hashtag.core :as ht :refer [defhashtag]]
            [clojure.pprint :refer [pprint]]))

(defhashtag pp pprint)
(defhashtag pp/locals pprint :locals? true)

(defn f
  [x]
  (let [a #pp/locals (inc x)
        b #pp/locals (* 2 a)]
    (dec b)))

(defn g
  [x]
  (* 3 #pp (f x)))

(g 5)
