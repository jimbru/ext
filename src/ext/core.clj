(ns ext.core
  (:refer-clojure :rename {nth nth-builtin}))

(defn- nth-impl [coll index]
  (let [coll-count (count coll)]
    (when (or (= coll-count 0)
              (not (<= (- coll-count) index (dec coll-count))))
      (throw (IndexOutOfBoundsException.)))
    (nth-builtin coll (mod index coll-count))))

(defn nth
  "Just like the built-in clojure.core/nth, except this also understands
  how to wrap negative indices.

  For example:

      (nth [1 2 3 4 5] 2)  => 3
      (nth [1 2 3 4 5] -2) => 4
  "
  ([coll index]
    (nth-impl coll index))
  ([coll index not-found]
    (try
      (nth-impl coll index)
      (catch IndexOutOfBoundsException e
        not-found))))

(defn second-to-last
  "Return the second-to-last item in coll."
  [coll]
  (nth coll -2))
