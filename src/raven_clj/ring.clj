(ns raven-clj.ring
  (:require [raven-clj.core :refer [capture]]
            [raven-clj.interfaces :refer [http stacktrace]]))

(defn wrap-sentry [handler dsn & [extra]]
  (fn [req]
    (try
      (handler req)
      (catch Exception e
        (future (capture dsn (-> (merge extra
                                        {:message (.getMessage e)})
                                 (http req)
                                 (stacktrace e))))
        (throw e)))))