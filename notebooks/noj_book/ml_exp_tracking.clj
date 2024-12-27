(ns noj-book.ml-exp-tracking
  (:require [scicloj.metamorph.ml :as ml]
            [scicloj.metamorph.ml.evaluation-handler :as eval-handler]))

;; # Introduction - Model experimentation platforms
;; Machine Learning (ML) experimenting platforms have lots of features, of which one is
;; **Experiment Tracking: Log and track experiments systematically, capturing all relevant metadata**
;;
;;  Usually these are tools which come with a database, to which "ML experiment information is send" and persistently stored.
;;
;; `metamorph.ml` has some features which support moderate sized experiment tracking, without relying on
;; a persistent database.


;; # Search best model hyper parameters with `ml/evaluate-pipelines`

;; In the last chapter we saw `ml/evaluate-pipelines` as a way
;; to find the best hyperparameters from a given search-space of
;; different models and their hyper parameters.
;;
;; This can be seen as the simplest use case for a ML experiment tracking platform.
;; I can
;; * run serveral experiements (= model trainings) 
;; * log input and output in a database
;; * can see which run obtained the best model
;;
;; In this the only interesting output to keep are:
;; * the (one) model metric we are interested in optimizing
;; * the (one) hyper parameters (or pipeline configuration) which produced this best model
;;   on the training data
;;
;;
;; We assume that the metric we obtained is a not biased representation of model 
;; accuracy on unknown data and we we assume that the found hyperparameter are the best 
;; performing as well on the unknown data. 
;; (so a model trained with these hyperparameters on the full training data becomes the best possible model)
;;
;; For this use case all other "information" obtained during the evaluation of 
;; the models could be discarded.
;;
;; `ml/evaluate-pipelines` allows / can be configured for other use cases, such as:

;;* keep metrics across all models and cross validations in order to obtain more accurate
;; estimations on model accuracy, and not only a single point estimate 
;; * keep obtained (trained) models during a run of `ml/evaluate-pipelines`
;; * store evaluation results externally
;;
;; When we look at this we need to keep in mind that hyper-parameter searches can become 
;; very quickly very compute / memory intensive as they do a brute-force search 
;; over the full space of model/parameter options, and each result might be large, 
;; as containing ev. the trained model as binary data and the datasets used to train the model.

;; `ml/evaluate-pipelines` is designed to allow to adjust for each use case
;; * data kept in memory
;; * data kept outside memory

;; So in the following we will see how the memory consumption is tunable.
;; by passing tuning related options to `ml/evaluate-pipelines`

;; ## Control if to return best only

;; `ml/evaluate-pipelines` has 2 boolean options which control 
;; if information for 'all' or only 'the best' performing model is returned
;; Both are "true"  be default, so the functions returns information on the
;; 'one best' model only.

;;* :return-best-pipeline-only - Only return information of the best performing pipeline. Default is true.
;;* :return-best-crossvalidation-only - Only return information of the best crossvalidation (per pipeline returned) . Default is true.

;; If we want information about "more then only the best model", we need to set them to false
;; accordingly. To either get information on all model evaluations, or the best cross validation per pipeline.

;; ## Control how much ionformation to reatain on heap for each evaluation

;; When we for example call `ml/evaluate-pipelines` with 10000 pipeline functions and a k-cross-fold (K = 5)
;; 10000 * 5 = 50000 models get trained. This happens in 2 nested loops, and `some` information is
;; kept in each iteration. `ml/evaluate-pipelines` allows precise control on which information is kept.

;; :evaluation-handler-fn - This option of the fn can control how much information from model training is retained.
;; Each evaluation result is a nested map containing lots of information.

;; The default removes the typically "large" information from the evaluation result, namely these keys :

[[:fit-ctx :metamorph/data]

 [:train-transform :ctx :metamorph/data]

 [:train-transform :ctx :model :scicloj.metamorph.ml/target-ds]
 [:train-transform :ctx :model :scicloj.metamorph.ml/feature-ds]

 [:test-transform :ctx :metamorph/data]

 [:test-transform :ctx :model :scicloj.metamorph.ml/target-ds]
 [:test-transform :ctx :model :scicloj.metamorph.ml/feature-ds]
 [:train-transform :ctx :model :model-data :model-as-bytes]
 [:train-transform :ctx :model :model-data :smile-df-used]
 [:test-transform :ctx :model :model-data :model-as-bytes]
 [:test-transform :ctx :model :model-data :smile-df-used]]

;; which are mainly the train/test data sets and the serialized model objects. 
;; This fn (which removes above) is used as the default :evaluation-handler-fn.
;; It is 'scicloj.metamorph.ml.evaluation-handler/default-result-dissoc-in-fn'
;;  when not changed by user.

;; Several other fns are pre-defined:
;; * result-dissoc-in-seq-ctx-fn -- evaluation-handler-fn which removes all :ctx
;; * metrics-and-model-keep-fn -- evaluation-handler-fn which keeps only train-metric, test-metric and the fitted model map, which contains as well the model object as byte array (among other things)
;; * metrics-and-options-keep-fn -- evaluation-handler-fn which keeps only train-metric, test-metric and and the options
;; * metrics-keep-fn -- evaluation-handler-fn which keeps only train-metric, test-metric"

;; The precise choice of the functions to use, depends what precise information "later" is needed.
;; A custom fn, with takes the "evaluation result map" as input can be specified as well. It should at least keep
[:train-transform :metric]
[:test-transform :metric]
;; which is required to sort the result. All other keys can be removed, 
;;when not needed by the the caller afterwards

;; A custom :evaluation-handler-fn can as well implement to "persistently store" (parts of) 
;; the evaluation result somewhere else, like a Atom, local file system or a database
;; metamorph.ml does not contain so far solutions for this as they are so many, 
;; and they are use case dependent
;;
;; This would allow to use `ml/evaluate-pipelines` over very large grid searches, 
;; writing intermediate results to disk, so they don't get lost in case of JVM crash for example.
;; (This would require as well custom coding for remembering persistently which parts of the 
;; search space was already searched for and avoid re-doing it, which is a non trivial problem)

;; # Caching of train/predict

;; Theoretically we only need to evaluate each of the hyper parameter combinations exactly ones, 
;; (assuming constant training data)
;; but in the reality of developing the training code, the same pipelines with the same datasets 
;; and the same model parameters get execute several times, either in the same JVM run or across
;; JVM restarts.  If we would have "caching" for 
;; this, it could save a lot of time. (for the price of using more memory)
;;
;; more to come