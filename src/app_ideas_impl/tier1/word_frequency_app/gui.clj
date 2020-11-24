(ns app-ideas-impl.tier1.word-frequency-app.gui
  (:require [app-ideas-impl.tier1.word-frequency-app.core :as core]
            [seesaw.core :as gui]
            [seesaw.mig :as mig]
            [seesaw.table :refer [table-model]]))

(defn string-empty? [s]
  (every? #(Character/isWhitespace %) s)
  )

(defn frequency-data-model [data]
  (table-model :columns [{:key :word :text "Word"}
                         {:key :frequency :text "Frequency"}]
               :rows data)
  )

(defn- make-panel []
  (let [panel (mig/mig-panel
                :items [
                        [(gui/text :id :text :multi-line? true) "width 300:300:500, height 200:200:300, growx, wrap 20"]
                        [(gui/button :id :translate :text "Translate") "wrap 20"]
                        [(gui/scrollable (gui/table :id :table :model (frequency-data-model [])))]
                        ])
        text (gui/select panel [:#text])
        button (gui/select panel [:#translate])
        table (gui/select panel [:#table])]
    (gui/listen button
                :action (fn [_]
                          (let [t (.getText text)]
                            (if (string-empty? t)
                              (gui/show! (gui/dialog :content "Empty text!" :parent panel :size [200 :by 100]))
                              (gui/config! table :model (frequency-data-model (vec (seq (core/word-frequency t)))))
                              ))))
    panel
    )
  )

(defn- make-frame []
  (let [frame (gui/frame :title "Word frequency app"
                         :on-close :exit
                         :content (make-panel)
                         )]
    (-> frame gui/pack! gui/show!))

  )

(defn -main [& args]
  (make-frame)
  )
