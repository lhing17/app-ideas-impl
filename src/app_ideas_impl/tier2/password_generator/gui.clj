(ns app-ideas-impl.tier2.password-generator.gui
  (:require [app-ideas-impl.tier2.password-generator.core :as core]
            [seesaw.core :as gui]
            [seesaw.mig :as mig]
            [seesaw.font :as font])
  (:import (java.awt Toolkit)
           (java.awt.datatransfer StringSelection)))

(def rand-result (atom ""))

(defn numeric-text? [^String s]
  (if (nil? s) false (every? #(Character/isDigit %) s))
  )



(defn make-panel []
  (let [panel (mig/mig-panel
                :items [[(gui/label "length: ") ""]
                        [(gui/text :text "10" :id :len) "width 100:100:160, wrap 20"]
                        [(gui/label "type: ") ""]
                        [(gui/checkbox :text "uppercase") "width 100:100:160"]
                        [(gui/checkbox :text "lowercase") "width 100:100:160"]
                        [(gui/checkbox :text "number") "width 100:100:160"]
                        [(gui/checkbox :text "symbol") "width 100:100:160, wrap 20"]
                        [(gui/button :id :generate :text "Generate password") "span 2"]
                        [(gui/button :id :copy :text "Copy to clipboard") "span 2, wrap 20"]
                        [(gui/label :font (font/font "ARIAL-BOLD-20") :text "Generated password is: ") "span 3"]
                        [(gui/label :font (font/font "ARIAL-BOLD-20") :id :result :text "") "span 2"]
                        ]
                )
        checkboxes (gui/select panel [:<javax.swing.JCheckBox>])]

    (gui/listen (gui/select panel [:#generate])
                :action (fn [_]
                          (let [text (.getText (gui/select panel [:#len]))
                                len (if (numeric-text? text) (Integer/parseInt text) 10)
                                result (gui/select panel [:#result])]
                            (reset! rand-result
                                    (core/random-password (core/select-password-chars
                                                            (map (comp keyword second)
                                                                 (filter #(first %)
                                                                         (map vector
                                                                              (map gui/selection checkboxes)
                                                                              (map #(.getText %) checkboxes)))))
                                                          len))
                            (.setText result @rand-result)
                            )
                          ))
    (gui/listen (gui/select panel [:#copy])
                :action (fn [_]
                          (let [clip (.getSystemClipboard (Toolkit/getDefaultToolkit))]
                            (.setContents clip (StringSelection. @rand-result) nil)
                            (gui/show! (gui/dialog :size [200 :by 100] :parent panel :content "Password copied!"))
                            )))
    panel
    )
  )

(defn make-frame []
  (let [frame (gui/frame :title "Random Generator"
                         :width 600
                         :height 400
                         :on-close :exit
                         :content (make-panel)
                         )]
    (.setLocationRelativeTo frame nil)
    (-> frame gui/show!))
  )

(defn -main [& args]
  (make-frame)
  )


