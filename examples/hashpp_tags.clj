(ns hashpp-tags
  (:require [hashtag.core :refer [defhashtag]]))


(defhashtag pp hashpp/get-wacky)

(defhashtag pp/locals println :locals? true)
