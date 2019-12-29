(ns test
  #?(:cljs (:require [clojure.pprint :refer [pprint]]))
  #?(:clj (:require [hashpp])
     :cljs (:require-macros [hashpp])))

(defn f
  [x]
  (let [a #pp-all (inc x)
        b #pp-all (* 2 a)]
    (dec b)))

(defn g
  [x]
  (* 3 #pp-all (f x)))

(g 5)
