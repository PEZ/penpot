;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.viewer.handoff.attributes.layout-flex
  (:require
   [app.common.types.shape.radius :as ctsr]
   [app.main.ui.components.copy-button :refer [copy-button]]
   [app.main.ui.formats :as fmt]
   [app.util.code-gen :as cg]
   [app.util.i18n :refer [tr]]
   [cuerdas.core :as str]
   [rumext.v2 :as mf]))

(def properties [:width :height :x :y :radius :rx :r1])

(def params
  {:to-prop {:x "left"
             :y "top"
             :rotation "transform"
             :rx "border-radius"
             :r1 "border-radius"}
   :format  {:rotation #(str/fmt "rotate(%sdeg)" %)
             :r1 #(apply str/fmt "%spx, %spx, %spx, %spx" %)}
   :multi   {:r1 [:r1 :r2 :r3 :r4]}})

(defn copy-data
  ([shape]
   (apply copy-data shape properties))
  ([shape & properties]
   (cg/generate-css-props shape properties params)))

(mf/defc layout-block
  [{:keys [shape]}]
  (let [_ (.log js/console (clj->js shape))
        flex-dir   (case (:layout-dir shape)
                     :right   :row
                     :left    :reverse-row
                     :top     :column
                     :bottom  :reverse-column
                     :default :row)
        _ (prn flex-dir)
        ]
    [:*
     [:div.attributes-unit-row
      [:div.attributes-label "Display"] ;; TODO traduc
      [:div.attributes-value "Flex"] ;; TODO change data to separate flex-grid
      [:& copy-button {:data (copy-data shape :layout-dir)}]]
     [:div.attributes-unit-row
      [:div.attributes-label "Direction"] ;; TODO traduc
      [:div.attributes-value (str flex-dir)]
      [:& copy-button {:data (copy-data shape :layout-dir)}]]
     [:div.attributes-unit-row
      [:div.attributes-label "Wrap"] ;; TODO traduc
      [:div.attributes-value (str (:layout-wrap-type shape))]
      [:& copy-button {:data (copy-data shape :layout-dir)}]]
     [:div.attributes-unit-row
      [:div.attributes-label "Align-items"] ;; TODO traduc
      [:div.attributes-value "todo"]
      [:& copy-button {:data (copy-data shape :layout-dir)}]]
     [:div.attributes-unit-row
      [:div.attributes-label "Justify-content"] ;; TODO traduc
      [:div.attributes-value "todo"]
      [:& copy-button {:data (copy-data shape :layout-dir)}]]
     [:div.attributes-unit-row
      [:div.attributes-label "Gap"] ;; TODO traduc
      [:div.attributes-value (:layout-gap shape)]
      [:& copy-button {:data (copy-data shape :layout-dir)}]]]))


(mf/defc layout-flex-panel
  [{:keys [shapes]}]
  [:div.attributes-block
   [:div.attributes-block-title
    [:div.attributes-block-title-text (tr "handoff.attributes.layout")]
    (when (= (count shapes) 1)
      [:& copy-button {:data (copy-data (first shapes))}])]

(for [shape shapes]
  [:& layout-block {:shape shape}])])
