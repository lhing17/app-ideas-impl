(ns app-ideas-impl.tier1.random-number-generator.core)


(defn rand-num [minimum maximum allow-decimal]
  (let [r (if allow-decimal rand rand-int)]
    (+ minimum (r (- maximum minimum)))
    )

  )

