(ns ext.core-test
  (:refer-clojure :exclude [nth])
  (:require [clojure.test :refer :all]
            [ext.core :refer :all]))

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
    (is (= :default (nth [] 0 :default)))))

(deftest second-to-last-test
  (is (= 4 (second-to-last '(1 2 3 4 5))))
  (is (= 4 (second-to-last [1 2 3 4 5])))
  (is (= 1 (second-to-last '(1 2))))
  (is (= 1 (second-to-last [1 2])))
  (is (thrown? IndexOutOfBoundsException (second-to-last '(1))))
  (is (thrown? IndexOutOfBoundsException (second-to-last [1])))
  (is (thrown? IndexOutOfBoundsException (second-to-last '())))
  (is (thrown? IndexOutOfBoundsException (second-to-last []))))
