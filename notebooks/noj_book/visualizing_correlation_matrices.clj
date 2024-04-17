;; # Visualizing correlation matrices (experimental 🛠) - DRAFT

;; This tutorial explores various ways to visualize a correlation matrix
;; as a heatmap.
;; It is inspired by the discussion at the [Clojurians Zulip chat](https://scicloj.github.io/docs/community/chat/):
;; [#data-science > correlation matrix plot ?](https://clojurians.zulipchat.com/#narrow/stream/151924-data-science/topic/correlation.20matrix.20plot.20.3F)

;; author: Daniel Slutsky

(ns noj-book.visualizing-correlation-matrices
  (:require [fastmath.stats]
            [fastmath.core :as fastmath]
            [tablecloth.api :as tc]
            [noj-book.datasets]
            [scicloj.kindly.v4.kind :as kind]
            [clojure.math :as math]))

;; ## Auxiliary functions

;; Rounding numbers:

(defn round
  [n scale rm]
  (.setScale ^java.math.BigDecimal (bigdec n)
             (int scale)
             ^RoundingMode (if (instance? java.math.RoundingMode rm)
                             rm
                             (java.math.RoundingMode/valueOf
                              (str (if (ident? rm) (symbol rm) rm))))))

;; For example (see [RoundingMode](https://docs.oracle.com/javase/8/docs/api/java/math/RoundingMode.html))

(round (/ 2.0 3) 2 :DOWN)
(round (/ 2.0 3) 2 :UP)
(round (/ 2.0 3) 2 :HALF_EVEN)

;; ## Computing a correlation matrix and representing it as a dataset:

(defn correlations-dataset [data columns-to-use]
  (let [matrix (->> columns-to-use
                    (mapv #(get data %))
                    fastmath.stats/correlation-matrix)]
    (->> matrix
         (map-indexed
          (fn [i row]
            (prn [i])
            (let [coli (columns-to-use i)]
              (->> row
                   (map-indexed
                    (fn [j corr]
                      (prn [i j])
                      (let [colj (columns-to-use j)]
                        {:i i
                         :j j
                         :coli coli
                         :colj colj
                         :corr corr
                         :corr-str (format "%.02f" corr)})))))))
         (apply concat)
         tc/dataset)))

;; For example:
(-> noj-book.datasets/iris
    (correlations-dataset [:sepal-length :sepal-width :petal-length :petal-width]))

;; ## Drawing a heatmap using Echarts

;; The following function is inspired by
;; [an Apache Echarts heatmap tutorial](https://github.com/apache/echarts/blob/master/test/heatmap-large.html).

(defn echarts-heatmap [{:keys [xyz-data xs ys
                               min max
                               series-name]
                        :or {series-name ""}}]
  (kind/echarts
   {:tooltip {}
    :xAxis {:type :category
            :data xs}
    :yAxis {:type :category
            :data ys}
    :visualMap {:min min
                :max max
                :calculable true
                :splitNumber 8
                :inRange {:color
                          ["#313695" "#4575b4" "#74add1"
                           "#abd9e9" "#e0f3f8" "#ffffbf"
                           "#fee090" "#fdae61" "#f46d43"
                           "#d73027" "#a50026"]}}
    :series [{:name series-name
              :type :heatmap
              :data xyz-data
              :itemStyle {:emphasis {:borderColor "#333"
                                     :borderWidth 2}}
              :progressive 1000
              :animation false}]}))

;; Here is an example using synthetic data:

(let [n 30]
  (echarts-heatmap
   {:xyz-data (for [i (range n)
                    j (range n)]
                [i j (fastmath/logistic (*  (+ (- i j))
                                            (rand)
                                            (/ 2 (double n))))])
    :x-data (range n)
    :y-data (range n)
    :min 0
    :max 1}))

;; Note that this plot supports tooltips.
