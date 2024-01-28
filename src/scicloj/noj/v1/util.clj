(ns scicloj.noj.v1.util
  (:require [clojure.string :as string]))

(defn concat-keywords [& kws]
  (->> kws
       (map name)
       (string/join "-")
       keyword))

(defn lift
  "Create context aware version of the given `op` function.

  Creates fns which work either with ds or ctx in first parameter.

  `:metamorph/data` will be used on a first parameter, if input was ctx.
    Result of the `op` function will be stored under `:metamorph/data` in case of ctx"
  [op & params]
  (fn [ds-or-ctx]
    (if (contains? ds-or-ctx :metamorph/data)
      (assoc ds-or-ctx :metamorph/data (apply op (:metamorph/data ds-or-ctx) params))
      (apply op ds-or-ctx params))))


(defn intern-tc-symbol [tc-symbol]

  (let  [var-meta
         (->
          (ns-publics 'tablecloth.api)
          (get tc-symbol)
          meta)

         renamed-argslists
         (mapv
          #(mapv (fn [arg] (if (= arg 'ds)
                            'ds-or-ctx
                            arg))
                 %)

          (:arglists var-meta))

         renamed-var-meta
         (assoc var-meta
                ;; :ns (namespace 'scicloj.noj.v1.lifted-tc)
                :arglists renamed-argslists)]



    (intern 'scicloj.noj.v1.lifted-tc
            tc-symbol
            (lift (var-get (find-var (symbol "tablecloth.api" (name tc-symbol))))))
    (alter-meta! (find-var (symbol "scicloj.noj.v1.lifted-tc" (name tc-symbol)))
             (fn [m] (merge m renamed-var-meta)))))
