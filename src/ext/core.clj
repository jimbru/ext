(ns ext.core
  (:refer-clojure :rename {nth nth-builtin}))

(defn assoc-default
  "Just like the built-in 'assoc', except that a default type can be passed.
  If the passed-in map doesn't exist (is falsey), 'default' is called and its
  return value is used as the map on which assoc is then called."
  [m default k v & kvs]
  (apply assoc (or m (default)) k v kvs))

(defn assoc-in-default
  "Just like the built-in 'assoc-in', except that a default type can be passed.
  If any of the nested levels do not exist, 'default' is called and its return
  value is used as the map which is then assoc'd."
  [m default [k & ks] v]
  (if ks
    (assoc-default m default k (assoc-in-default (get m k) default ks v))
    (assoc-default m default k v)))

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

(defn update-in-default
  "Just like the built-in 'update-in', except that a default type can be passed.
  If any of the nested levels do not exist, 'default' is called and its return
  value is used as the map which is then updated."
  [m default [k & ks] f & args]
  (if ks
    (assoc-default m default k (apply update-in-default (get m k) default ks f args))
    (assoc-default m default k (apply f (get m k) args))))
