(ns ext.core
  (:refer-clojure :rename {nth nth-builtin}))

(defn- nth-impl [coll index & {:keys [throw? not-found]}]
  (if (>= index 0)
    (if throw?
      (nth-builtin coll index)
      (nth-builtin coll index not-found))
    (let [coll-count (count coll)]
      (if (> (- index) coll-count)
        (if throw?
          (throw (IndexOutOfBoundsException.))
          not-found)
        (let [mod-index (+ coll-count index)]
          (if throw?
            (nth-builtin coll mod-index)
            (nth-builtin coll mod-index not-found)))))))

(defn nth
  "Just like the built-in clojure.core/nth, except this also understands
  how to wrap negative indices.

  For example:

      (nth [1 2 3 4 5] 2)  => 3
      (nth [1 2 3 4 5] -2) => 4
  "
  ([coll index]
    (nth-impl coll index :throw? true))
  ([coll index not-found]
    (nth-impl coll index :throw? false :not-found not-found)))

(defn second-to-last
  "Return the second-to-last item in coll."
  [coll]
  (nth coll -2))
