(ns sparkofreason.hashtag.core-test
  (:require [sparkofreason.hashtag.core :as ht :refer [defhashtag]]
            [cognitect.transcriptor :as xr :refer (check!)]))

(defhashtag h tap> :locals? true)

(defn foo
  [x]
  (let [y (* 2 x)
        z #h (+ 1 x)]
    z))

(defn baz
  [x]
  #h (foo x))

(def ts (atom []))
(defn tap-fn
  [t]
  (println "t" t)
  (swap! ts conj t))
(add-tap tap-fn)
(baz 4)
(Thread/sleep 1000)
(remove-tap tap-fn)

(check! #(= % [{:ns "sparkofreason.hashtag.core-test",
                :result 5,
                :form '(+ 1 x),
                :metadata {:line 10, :column 14},
                :locals {:x 4, :y 8}}
               {:ns "sparkofreason.hashtag.core-test",
                :result 5,
                :form '(foo x),
                :metadata {:line 15, :column 6},
                :locals {:x 4}}])
        @ts)
