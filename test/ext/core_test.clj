(ns ext.core-test
  (:refer-clojure :exclude [nth])
  (:require [clojure.test :refer :all]
            [ext.core :refer :all]))

(deftest assoc-default-test
  (testing "map exists, single assoc"
    (is (= {:foo :bar} (assoc-default {} sorted-map :foo :bar)))
    (is (= {:foo :x, :bar :y} (assoc-default {:foo :x} sorted-map :bar :y))))
  (testing "map exists, multi assoc"
    (is (= {:foo :x, :bar :y} (assoc-default {} sorted-map :foo :x :bar :y)))
    (is (= {:foo :x, :bar :y, :baz :z}
           (assoc-default {:foo :x} sorted-map :bar :y :baz :z))))
  (testing "map doesn't exist, single assoc"
    (let [result (assoc-default nil sorted-map :foo :bar)]
      (is (= {:foo :bar} result))
      (is (sorted? result))))
  (testing "map doesn't exist, multi assoc"
    (let [result (assoc-default nil sorted-map :foo :x :bar :y)]
      (is (= {:foo :x, :bar :y} result))
      (is (sorted? result)))))

(deftest assoc-in-default-test
  (testing "one-level assoc"
    (is (= {:foo :bar} (assoc-in-default {} sorted-map [:foo] :bar))))
  (testing "two-level assoc"
    (let [result (assoc-in-default {} sorted-map [:foo :bar] :baz)]
      (is (= {:foo {:bar :baz}} result))
      (is (sorted? (:foo result)))))
  (testing "three-level assoc"
    (let [result (assoc-in-default {} sorted-map [:foo :bar :baz] :quux)]
      (is (= {:foo {:bar {:baz :quux}}} result))
      (is (sorted? (:foo result)))
      (is (sorted? (get-in result [:foo :bar]))))))

(deftest nth-test
  (let [v [1 2 3 4 5]]
    (testing "valid positive indices"
      (is (= 1 (nth v 0)))
      (is (= 2 (nth v 1)))
      (is (= 5 (nth v 4)))
      (is (= 5 (nth v 4 :default))))
    (testing "invalid positive indices"
      (is (thrown? IndexOutOfBoundsException (nth v 5)))
      (is (= :default (nth v 5 :default))))
    (testing "valid negative indices"
      (is (= 5 (nth v -1)))
      (is (= 4 (nth v -2)))
      (is (= 1 (nth v -5)))
      (is (= 1 (nth v -5 :default))))
    (testing "invalid negative indices"
      (is (thrown? IndexOutOfBoundsException (nth v -6)))
      (is (= :default (nth v -6 :default)))))
  (testing "empty collection"
    (is (thrown? IndexOutOfBoundsException (nth [] 0)))
    (is (= :default (nth [] 0 :default))))
  (testing "avoid lazy realization for positive indices"
    ;; nth will never return if it attempts to realize an unbounded range
    (let [fv (future (nth (range) 10))]
      (is (= 10 (deref fv 100 :timed-out))))))

(deftest second-to-last-test
  (is (= 4 (second-to-last '(1 2 3 4 5))))
  (is (= 4 (second-to-last [1 2 3 4 5])))
  (is (= 1 (second-to-last '(1 2))))
  (is (= 1 (second-to-last [1 2])))
  (is (thrown? IndexOutOfBoundsException (second-to-last '(1))))
  (is (thrown? IndexOutOfBoundsException (second-to-last [1])))
  (is (thrown? IndexOutOfBoundsException (second-to-last '())))
  (is (thrown? IndexOutOfBoundsException (second-to-last []))))

(deftest update-in-default-test
  (testing "one-level update"
    (is (= {:foo [1]} (update-in-default {} sorted-map [:foo] concat [1])))
    (is (= {:foo [1 2]} (update-in-default {:foo [1]} sorted-map [:foo] concat [2]))))
  (testing "two-level update"
    (let [result (update-in-default {} sorted-map [:foo :bar] concat [1])]
      (is (= {:foo {:bar [1]}} result))
      (is (sorted? (:foo result))))
    (let [result (update-in-default {:foo {:bar [1]}} sorted-map [:foo :bar] concat [2])]
      (is (= {:foo {:bar [1 2]}} result))
      (is (not (sorted? (:foo result))))))
  (testing "three-level assoc"
    (let [result (update-in-default {} sorted-map [:foo :bar :baz] concat [1])]
      (is (= {:foo {:bar {:baz [1]}}} result))
      (is (sorted? (:foo result)))
      (is (sorted? (get-in result [:foo :bar]))))
    (let [result (update-in-default {:foo {:bar nil}} sorted-map [:foo :bar :baz] concat [1])]
      (is (= {:foo {:bar {:baz [1]}}} result))
      (is (not (sorted? (:foo result))))
      (is (sorted? (get-in result [:foo :bar]))))
    (let [result (update-in-default {:foo {:bar {:baz [1]}}} sorted-map [:foo :bar :baz] concat [2])]
      (is (= {:foo {:bar {:baz [1 2]}}} result))
      (is (not (sorted? (:foo result))))
      (is (not (sorted? (get-in result [:foo :bar])))))))
