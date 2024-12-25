(ns scicloj.noj
  (:require
   [clojure.repl.deps :as deps]
   [nrepl.server :as nrepl-server]))

(defn add-libs-noj-ml []
  (deps/add-libs
   {'org.scicloj/scicloj.ml.smile {:mvn/version "7.4.3"}
    'org.scicloj/metamorph.ml {:mvn/version "0.10.4"}
    'org.scicloj/sklearn-clj {:mvn/version "0.4.1"}
    'org.scicloj/scicloj.ml.xgboost {:mvn/version "6.2.0"}
    'org.scicloj/scicloj.ml.tribuo {:mvn/version "0.1.4"}
    'org.tribuo/tribuo-regression-liblinear {:mvn/version "4.3.1"}
    'org.tribuo/tribuo-regression-libsvm {:mvn/version "4.3.1"}
    'org.tribuo/tribuo-regression-sgd {:mvn/version "4.3.1"}
    'org.tribuo/tribuo-regression-tree {:mvn/version "4.3.1"}
    'org.tribuo/tribuo-regression-xgboost {:mvn/version "4.3.1"}
    'org.tribuo/tribuo-classification-liblinear {:mvn/version "4.3.1"}
    'org.tribuo/tribuo-classification-libsvm {:mvn/version "4.3.1"}
    'org.tribuo/tribuo-classification-sgd {:mvn/version "4.3.1"}
    'org.tribuo/tribuo-classification-tree {:mvn/version "4.3.1"}
    'org.tribuo/tribuo-classification-xgboost {:mvn/version "4.3.1"}}))

(defn start-nrepl []
  (nrepl-server/start-server :port 12345))