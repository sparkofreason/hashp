(ns hashpp
  (:require #?(:clj [hashtag.core :as ht :refer [defhashtag]])
            [clojure.pprint :refer [pprint]]))

(defn mypprint [x] (pprint x))

#?(:clj
    (defhashtag pp-all mypprint :stacktrace? true))
