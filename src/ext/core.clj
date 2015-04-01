(ns ext.core)

(defn second-to-last
  "Return the second-to-last item in coll."
  [coll]
  (nth coll (dec (dec (count coll)))))
