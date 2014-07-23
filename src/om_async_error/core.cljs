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
      {:text ""
       :mounted true})
    om/IWillMount
    (will-mount [_]
      (go (while (om/get-state owner :mounted)
            (let [v (<! dump-chan)]
              (.log js/console "dumping state:")
              (.log js/console (pr-str (om/get-state owner)))))))
    om/IWillUnmount
    (will-unmount [_]
      (om/set-state! owner :mounted false))
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
        ;; Warning: you might think that putting (chan) directly in the opts
        ;; would work, however, it doesn't. It's possible for base to rerender
        ;; which creates a new chan, but my-form is already mounted and so
        ;; isn't called and doesn't get the new channel.
        (if visible (om/build my-form app {:opts {:dump-chan dump-chan}}))))))

(om/root
  base
  app-state
  {:target (. js/document (getElementById "app"))})