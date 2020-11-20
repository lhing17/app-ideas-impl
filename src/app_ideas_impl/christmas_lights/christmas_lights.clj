(ns app-ideas-impl.christmas-lights.christmas-lights
  (:require [seesaw.core :as gui]
            [seesaw.graphics :as graph]
            [seesaw.bind :as bind])
  (:import (javax.swing JFrame JPanel)
           (java.awt Color Graphics Graphics2D)))

(def colors [Color/red Color/orange Color/yellow Color/green Color/cyan Color/blue (Color. 128 0 128)])
(def mem (atom nil))

(defn draw-lights [^Graphics2D g]
  (doseq [[x c] (map vector (take 7 (range 50 Integer/MAX_VALUE 60)) (repeatedly 7 #(rand-nth colors)))]
    (graph/draw g (graph/circle x 50 25) {:background c}))
  )

(defn make-light-panel []
  "create a light panel with specified colors"
  (let [c (gui/canvas :id :light :paint #(draw-lights %2))]
    (print (gui/visible? c))
    c)

  )

(defn start-shining [p]
  (reset! mem (future (loop []
                        (Thread/sleep 200)
                        (gui/replace! p (gui/select p [:#light]) (make-light-panel))
                        (recur)
                        )))

  )

(defn make-button [p]
  (gui/button :text "start"
              :id :start-button
              :listen [
                       :action (fn [_]
                                 (if (nil? @mem) (start-shining p) (do
                                                                     (future-cancel @mem)
                                                                     (reset! mem nil)))
                                 )
                       ])
  )

(defn make-panel []
  (let [panel (gui/border-panel
                :id "panel"
                :center (make-light-panel)
                :south (gui/vertical-panel
                         :id :v-panel
                         :items [
                                 (gui/horizontal-panel :items [(gui/label "refresh interval：")
                                                               (gui/text "100")])
                                 ]
                         ))]

    (gui/add! (gui/select panel [:#v-panel]) [(make-button panel) :south])
    panel
    )
  )

(defn -main [& args]
  (gui/invoke-later
    (let [frame (gui/frame :title "Christmas Lights"
                           :on-close :exit
                           :width 500
                           :height 200
                           :content (make-panel)
                           )]

      (-> frame gui/show!)
      )
    )
  )