(ns hashtag.core-test
  (:require [hashtag.core :as ht :refer [defhashtag]]
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
  (swap! ts conj t))
(add-tap tap-fn)
(baz 4)
(Thread/sleep 1000)
(remove-tap tap-fn)

(check! #(= % [{:result 5,
                :form '(+ 1 x),
                :locals {:x 4, :y 8}}
               {:result 5,
                :form '(foo x),
                :locals {:x 4}}])
        @ts)
