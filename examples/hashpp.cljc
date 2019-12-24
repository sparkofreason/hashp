(ns hashpp
  #?(:clj (:require [hashpp-tags])
     :cljs (:require-macros [hashpp-tags])))

#?(:cljs (enable-console-print!))

(defn get-wacky
  [x]
  (println "Wackadoo")
  (println x))

(defn foogle
  [x]
  (let [a #pp/locals (inc x)
        b #pp/locals (* 2 a)]
    (dec b)))

(defn gooble
  [x]
  (* 3 #pp (foogle x)))

(gooble 5)
