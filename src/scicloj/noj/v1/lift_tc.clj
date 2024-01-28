(ns scicloj.noj.v1.lift-tc
  (:require
            [tech.v3.datatype.export-symbols :as export-symbols]))
            

;;  code for creating the lifted tablecloth ns





(comment

 (create-ns 'scicloj.noj.v1.lifted-tc)
 (require '[scicloj.noj.v1.util :as util])
;; intern functions
 (->>
  (ns-publics 'tablecloth.api)
  keys
  (run! util/intern-tc-symbol))

   ;; create lifted functions on disk

 (export-symbols/write-api!
  'scicloj.noj.v1.lifted-tc
  'scicloj.noj.v1.tablecloth
  "src/scicloj/noj/v1/tablecloth.clj"
  []))




(comment
  ;; test the lifted functions
  (require '[scicloj.noj.v1.tablecloth]
           '[scicloj.noj.v1.datasets :as datasets])

  (scicloj.noj.v1.tablecloth/head datasets/iris)                  ;; => 150
  (scicloj.noj.v1.tablecloth/head {:metamorph/data datasets/iris})) ;; => #:metamorph{:data 150}
