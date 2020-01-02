(ns hashpp
  (:require #?(:clj [hashtag.core :refer [defhashtag]])
            [clojure.pprint :refer [pprint]]
            #?(:clj [net.cgrand.macrovich :as macros])))

#?(:clj
    (defhashtag pp-all {:cljs println
                        :clj #(clojure.pprint/pprint %)}
      :stacktrace? false :locals? true))
