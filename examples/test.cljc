(ns testf
  (:require [hashpp])
  #?(:cljs (:require-macros [hashpp])))

#?(:cljs (enable-console-print!))

(defn f
  [x]
  (let [a #pp-all (inc x)
        b #pp-all (* 2 a)]
    (dec b)))

(defn g
  [x]
  (* 3 #pp-all (f x)))

(g 5)
