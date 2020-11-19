(ns app-ideas-impl.bin2dec.bin2dec)


(defn binToDec [s]
  (reduce #(+ (* % 2) %2) (map #(Integer/parseInt (str %)) s)))

(defn valid-binary? [bin]
  (cond
    (= \0 bin) true
    (= \1 bin) true
    :else false))

(defrecord Either [left right])

(defn ^Either doBinToDec [line]
  (cond
    (> (count line) 8) (Either. "Error: Binary too long." nil)
    (not-every? valid-binary? line) (Either. "Error: Contains non-binary character." nil)
    :else (Either. nil (binToDec line))
    )
  )


(defn execBinToDec []
  (do
    (println "Please type up to 8 binary digits and press enter, its decimal equivalent will be displayed.")
    (let [line (read-line) either (doBinToDec line)]
      (if (nil? (:right either))
        (println (:left either))
        (println (str "The decimal equivalent is:" (:right either)))
        )
      )))


(defn -main [& args]
  (execBinToDec)
  )