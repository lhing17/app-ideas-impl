(ns app-ideas-impl.tier1.word-frequency-app.core)

(defn word-frequency [s]
  (let [results (frequencies (clojure.string/split s #"\W+"))]
    (into (sorted-map-by
            (fn [key1 key2]
              (compare [(get results key2) key2] [(get results key1) key1])
              ))
          results)
    )

  )
