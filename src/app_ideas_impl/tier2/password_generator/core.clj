;Password Generator
(ns app-ideas-impl.tier2.password-generator.core)

(def password-symbols "`~!@#$%^&*()_+-={}|[]\\:\";'<>?,./")

(defn- consecutive-chars [start len]
  (apply str (map char (take len (iterate inc start))))
  )

(def all-password-chars
  (str password-symbols (consecutive-chars 48 10) (consecutive-chars 65 26) (consecutive-chars 97 26))
  )

(defn- in-range? [c start end]
  (let [i (int c)]
    (and (>= i start) (< i end)))
  )


(defn select-password-chars [coll]
  "select password chars from all-password-chars.
  supported keys includes:
  :upper :lower :number :symbol

  eg.
  (select-password-chars [:upper :lower])
  "
  (let [upper-case-char? (fn [c] (in-range? c 65 91))
        lower-case-char? (fn [c] (in-range? c 97 (+ 97 26)))
        number-char? (fn [c] (in-range? c 48 58))
        symbol-char? (fn [c] (clojure.string/includes? password-symbols (str c)))
        m {:uppercase upper-case-char? :lowercase lower-case-char? :number number-char? :symbol symbol-char?}
        ]
    (filter (apply some-fn (vals (select-keys m coll))) all-password-chars)
    )

  )

(defn random-password [coll len]
  "Generate a random password from given characters with specified length."
  (apply str (take len (repeatedly #(rand-nth coll))))
  )


;(defn -main [& args]
;  (println (random-password (select-password-chars [:uppercase :number :symbol]) 10))
;
;  )