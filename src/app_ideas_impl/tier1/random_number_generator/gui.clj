(ns app-ideas-impl.tier1.random-number-generator.gui
  (:require
    [app-ideas-impl.tier1.random-number-generator.core :as core]
    [seesaw.core :as gui]
    [seesaw.mig :as mig]
    ))

(def random-result (atom ""))

(defn make-panel []
  (let [panel (mig/mig-panel
                :items [
                        [(gui/label :text "Minimum")]
                        [(gui/text :id :min :text "0") "width 50:50:80, wrap 20"]
                        [(gui/label :text "Maximum")]
                        [(gui/text :id :max :text "100") "width 50:50:80, wrap 20"]
                        [(gui/checkbox :id :neg :text "negative")]
                        [(gui/checkbox :id :dec :text "decimal") "wrap 20"]
                        [(gui/button :id :btn :text "Generate") "growx, span 2, wrap 20"]
                        [(gui/label :text "Generated: ")]
                        [(gui/label :id :rand :text "")]
                        ])]
    (gui/listen (gui/select panel [:#btn])
                :action (fn [_]
                          (let [minimum (Integer/parseInt (.getText (gui/select panel [:#min])))
                                maximum (Integer/parseInt (.getText (gui/select panel [:#max])))
                                allow-negative (gui/selection (gui/select panel [:#neg]))
                                allow-decimal (gui/selection (gui/select panel [:#dec]))
                                ]
                            (if (and (not allow-negative) (or (neg? minimum) (neg? maximum)))
                              (gui/show! (gui/dialog :size [250 :by 100] :parent panel :content "Negatives are not allowed!"))
                              (.setText (gui/select panel [:#rand]) (str (core/rand-num minimum maximum allow-decimal)))
                              )
                            )

                          ))
    panel)
  )

(defn make-frame []
  (let [frame (gui/frame :on-close :exit
                         :width 300
                         :height 250
                         :title "Random number generator"
                         :content (make-panel)
                         )]
    (-> frame gui/show!)
    ))

(defn -main [& args]
  (make-frame)
  )