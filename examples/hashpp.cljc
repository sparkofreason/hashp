(ns hashpp
  (:require #?(:clj [hashtag.core :refer [defhashtag]])
            [clojure.pprint :refer [pprint]]
            #?(:clj [net.cgrand.macrovich :as macros])))

#?(:clj
    (defhashtag pp-all println #_{:cljs println
                                  :clj #(clojure.pprint/pprint %)}
      :stacktrace? false :locals? true))
