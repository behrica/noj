(ns scicloj.noj.v1.initrepl)

(println "Require namespaces")
(in-ns 'user)

(require 'refactor-nrepl.middleware
         '[nrepl.server :refer [start-server stop-server default-handler]]
         'cider.nrepl
         '[scicloj.clay.v2.api])





(require '[tablecloth.api :as tc]
         '[scicloj.metamorph.ml :as ml]
         '[tech.v3.dataset :as ds]
         '[scicloj.ml.smile.classification]
         '[scicloj.ml.smile.regression]
         '[scicloj.metamorph.ml.classification]
         '[libpython-clj2.python :as py]
         '[nextjournal.clerk :as clerk])
         ;; '[clojisr.v1.r :as r]

(defn start-nrepl-server! []
   (start-server :port 12345 :handler (apply default-handler (conj  cider.nrepl/cider-middleware 'refactor-nrepl.middleware/wrap-refactor))))


(defn start-clay! []
  (scicloj.clay.v2.api/start!))

(defn start-clerk! []
  (clerk/serve! {:browse? true})
  :ready)

(println "available noj commands:")
(println
 (keys
  (ns-publics 'user)))

(print "available aliased ns:")
(println
 (ns-aliases 'user))
