(ns hashpp
  (:require #?(:clj [sparkofreason.hashtag.core :refer [defhashtag]])
            [clojure.pprint :refer [pprint]]
            #?(:clj [net.cgrand.macrovich :as macros])))

#?(:clj
    (defhashtag pp-all {:cljs (.log js/console %)
                        :clj clojure.pprint/pprint}
      :stacktrace? false :locals? true))
