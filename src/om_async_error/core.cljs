(ns om-async-error.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.core.async :refer [ <! put! chan]]))

(enable-console-print!)

(def app-state (atom {}))

(defn my-form
  [app owner {:keys [dump-chan]}]
  (reify
    om/IInitState
    (init-state [_]
      {:text ""})
    om/IWillMount
    (will-mount [_]
      (go (while true
            (let [v (<! dump-chan)]
              (.log js/console "dumping state:")
              (.log js/console (pr-str (om/get-state owner)))))))
    om/IRenderState
    (render-state [_ {:keys [text]}]
      (dom/div nil
        (dom/input #js {:type "text"
                        :value text
                        :onChange #(om/set-state! owner :text (.. % -target -value))})
        (dom/button
          #js {:onClick #(put! dump-chan true)}
          "Dump State")))))

(defn base
  [app owner]
  (reify
    om/IInitState
    (init-state [_]
      {:visible false
       :dump-chan (chan)})
    om/IRenderState
    (render-state [_ {:keys [visible dump-chan]}]
      (dom/div nil
        (dom/button #js {:onClick #(om/update-state! owner :visible not)}
                    "Toggle Form")
        ;; If I create a new chan here, rather than passing in dump-chan,
        ;; the Dump State button stops working as soon as you type into the
        ;; text box.
        (if visible (om/build my-form app {:opts {:dump-chan dump-chan}}))))))

(om/root
  base
  app-state
  {:target (. js/document (getElementById "app"))})