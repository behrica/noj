(ns scicloj.noj.v1.lifted-tc
  (:require
   [tablecloth.api]
   [scicloj.noj.v1.util :as util]))

;;  this is needed for the "generated code"" in tablecloth ns to work

(create-ns 'scicloj.noj.v1.lifted-tc)

;; intern the lifted functions in this ns
(->>
 (ns-publics 'tablecloth.api)
 keys
 (run! util/intern-tc-symbol))
