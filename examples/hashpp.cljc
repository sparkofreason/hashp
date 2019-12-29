(ns hashpp
  (:require #?(:clj [hashtag.core :refer [defhashtag]])
            [clojure.pprint :refer [pprint]]))

(defn mypprint [x] (pprint x))

#?(:clj
    (defhashtag pp-all hashpp/mypprint :stacktrace? true :locals? true))
