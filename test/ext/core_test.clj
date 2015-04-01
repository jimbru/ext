(ns ext.core-test
  (:require [clojure.test :refer :all]
            [ext.core :refer :all]))

(deftest second-to-last-test
  (is (= 4 (second-to-last '(1 2 3 4 5))))
  (is (= 4 (second-to-last [1 2 3 4 5])))
  (is (= 1 (second-to-last '(1 2))))
  (is (= 1 (second-to-last [1 2])))
  (is (thrown? IndexOutOfBoundsException (second-to-last '(1))))
  (is (thrown? IndexOutOfBoundsException (second-to-last [1])))
  (is (thrown? IndexOutOfBoundsException (second-to-last '())))
  (is (thrown? IndexOutOfBoundsException (second-to-last []))))
