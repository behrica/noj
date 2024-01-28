(ns lift)

(require '[clojure.java.shell])
(require '[scicloj.noj.v1.tablecloth :as tc]
         '[scicloj.noj.v1.datasets :as datasets]
         '[scicloj.metamorph.core :as mm])


           
(println
 (tc/head datasets/iris))


(println
 (mm/pipe-it
  datasets/iris
  tc/head))
