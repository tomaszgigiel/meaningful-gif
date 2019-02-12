(ns pl.tomaszgigiel.chunk.chunk-test
  (:require [clojure.test :as tst])
  (:require [pl.tomaszgigiel.chunk.chunk :as chunk])
  (:require [pl.tomaszgigiel.chunk.test-config :as test-config]))

(tst/use-fixtures :once test-config/once-fixture)
(tst/use-fixtures :each test-config/each-fixture)

(let [chunk-size 20
      cargo-size (* chunk-size 1024 1024)
      cargo (byte-array cargo-size (byte \a))
      empty-chunk (chunk/new chunk-size)
      full-chunk (first (chunk/series (chunk/new chunk-size) cargo 0 chunk-size))
      some-chunk (first (chunk/series (chunk/new chunk-size) cargo 0 1))
      chunks (chunk/series (chunk/new chunk-size) cargo 0 cargo-size)]

  (tst/deftest full?-test
    (tst/is (false? (chunk/full? empty-chunk)))
    (tst/is (true? (chunk/full? full-chunk)))
    (tst/is (false? (chunk/full? some-chunk))))

  (tst/deftest some?-test
    (tst/is (false? (chunk/chunk-some? empty-chunk)))
    (tst/is (true? (chunk/chunk-some? full-chunk)))
    (tst/is (true? (chunk/chunk-some? some-chunk))))

  (tst/deftest successor-test
    (tst/is (= (-> empty-chunk chunk/chunk-str) "1z"))
    (tst/is (= (-> empty-chunk chunk/successor chunk/chunk-str) "2z"))
    (tst/is (= (-> empty-chunk chunk/successor chunk/successor chunk/chunk-str) "3z")))

  (tst/deftest series-test
    (tst/is (= (count chunks) (/ cargo-size chunk-size)))
    (tst/is (= (chunk/cmp (nth chunks 0) (nth chunks 0)) 0))
    (tst/is (= (chunk/cmp (nth chunks 1) (nth chunks 0)) 1))
    (tst/is (= (chunk/cmp (nth chunks 0) (nth chunks 1)) -1))
    (tst/is (= (chunk/chunk-str (nth chunks 0) 3) "1za"))
    (tst/is (= (chunk/chunk-str (nth chunks 1) 3) "2za"))
    (tst/is (= (chunk/chunk-str (nth chunks 2) 3) "3za"))
    (tst/is (= (chunk/chunk-str (nth chunks 3) 3) "4za"))
    (tst/is (= (chunk/chunk-str (nth chunks 4) 3) "5za"))
    (tst/is (= (chunk/chunk-str (nth chunks 5) 3) "6za"))
    (tst/is (= (chunk/chunk-str (nth chunks 6) 3) "7za"))
    (tst/is (= (chunk/chunk-str (nth chunks 7) 3) "8za"))
    (tst/is (= (chunk/chunk-str (nth chunks 8) 3) "9za"))
    (tst/is (= (chunk/chunk-str (nth chunks 9) 3) "aza"))))
