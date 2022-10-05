;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.workspace.sidebar.options.menus.layout-container
  (:require
   [app.common.data :as d]
   [app.common.data.macros :as dm]
   [app.main.data.workspace.shape-layout :as dwsl]
   [app.main.store :as st]
   [app.main.ui.components.numeric-input :refer [numeric-input]]
   [app.main.ui.icons :as i]
   [app.util.dom :as dom]
   [app.util.i18n :as i18n :refer [tr]]
   [cuerdas.core :as str]
   [rumext.v2 :as mf]))

(def layout-container-attrs
  [:layout                 ;; :flex, :grid in the future
   :layout-dir             ;; :row, :reverse-row, :column, :reverse-column
   :layout-gap             ;; number could be negative
   :layout-type            ;; :packed, :space-between, :space-around
   :layout-wrap-style       ;; :wrap, :no-wrap
   :layout-padding-type    ;; :simple, :multiple
   :layout-padding         ;; {:p1 num :p2 num :p3 num :p4 num} number could be negative
   :layout-h-orientation   ;; :left, :center, :right
   :layout-v-orientation]) ;; :top, :center, :bottom

(def grid-pos  [[:top :left]
                [:top :center]
                [:top :right]
                [:center :left]
                [:center :center]
                [:center :right]
                [:bottom :left]
                [:bottom :center]
                [:bottom :right]])

(def grid-rows [:top :center :bottom])
(def grid-cols [:left :center :right])

(defn- get-layout-icon
  [dir layout-type v h]
  (let [row? (or (= dir :right) (= dir :left))
        manage-text-icon
        (if row?
          (case v
            :top    i/text-align-left
            :center i/text-align-center
            :bottom i/text-align-right
            i/text-align-center)
          (case h
            :left   i/text-align-left
            :center i/text-align-center
            :right  i/text-align-right
            i/text-align-center))]
    (case layout-type
      :packed        manage-text-icon
      :space-around  i/space-around
      :space-between i/space-between)))

(mf/defc direction-btn
  [{:keys [dir saved-dir set-direction] :as props}]
  (let [handle-on-click
        (mf/use-callback
         (mf/deps set-direction dir)
         (fn []
           (when (some? set-direction)
             (set-direction dir))))]

    [:button.dir.tooltip.tooltip-bottom
     {:class  (dom/classnames :active         (= saved-dir dir)
                              :row            (= :row dir)
                              :reverse-row    (= :reverse-row dir)
                              :reverse-column (= :reverse-column dir)
                              :column         (= :column dir))
      :key    (dm/str  "direction-" dir)
      :alt    (tr (dm/str "workspace.options.layout.direction." (d/name dir)))
      :on-click handle-on-click}
     i/auto-direction]))

(mf/defc wrap-row
  [{:keys [wrap-style set-wrap set-no-wrap] :as props}]
  [:*
   [:button.no-wrap.tooltip.tooltip-bottom
    {:class  (dom/classnames :active  (= wrap-style :no-wrap))
     :alt    (tr (dm/str "workspace.options.layout.no-wrap"))
     :on-click set-no-wrap}
    i/minus]
   [:button.wrap.tooltip.tooltip-bottom
    {:class  (dom/classnames :active  (= wrap-style :wrap))
     :alt    (tr (dm/str "workspace.options.layout.wrap"))
     :on-click set-wrap}
    i/auto-wrap]])

(mf/defc align-row
  [{:keys [is-col? align-items set-align] :as props}]
         ;;TODO iterar y eliminar código repetido
  [:div.align-items-style
   [:button.align-start.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= align-items :align-items-start))
     :alt      (tr (dm/str "workspace.options.layout.align-items-start")) ;; TODO añadir lineas de textoa tradus
     :on-click #(set-align :align-items-start)}
    (if is-col?
      i/align-items-column-start
      i/align-items-row-start)]
   [:button.align-center.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= align-items :align-items-center))
     :alt      (tr (dm/str "workspace.options.layout.align-items-center"))
     :on-click #(set-align :align-items-center)}
    (if is-col?
      i/align-items-column-center
      i/align-items-row-center)]
   [:button.align-end.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= align-items :align-items-end))
     :alt      (tr (dm/str "workspace.options.layout.align-items-end"))
     :on-click #(set-align :align-items-end)}
    (if is-col?
      i/align-items-column-end
      i/align-items-row-end)]
   [:button.align-strech.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= align-items :align-items-strech))
     :alt      (tr (dm/str "workspace.options.layout.align-items-strech"))
     :on-click #(set-align :align-items-strech)}
    (if is-col?
      i/align-items-column-strech
      i/align-items-row-strech)]
   [:button.align-baseline.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= align-items :align-items-baseline))
     :alt      (tr (dm/str "workspace.options.layout.align-items-baseline"))
     :on-click #(set-align :align-items-baseline)}
    (if is-col?
      i/align-items-column-baseline
      i/align-items-row-baseline)]

   ])

(mf/defc align-content-row
  [{:keys [is-col? align-content set-align-content] :as props}]
         ;;TODO iterar y eliminar código repetido
  [:div.align-content-style
   [:button.align-start.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= align-content :align-content-start))
     :alt      (tr (dm/str "workspace.options.layout.align-content-start")) ;; TODO añadir lineas de textoa tradus
     :on-click #(set-align-content :align-content-start)}
    (if is-col?
      i/align-content-column-start
      i/align-content-row-start)]
   [:button.align-center.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= align-content :align-content-center))
     :alt      (tr (dm/str "workspace.options.layout.align-content-center"))
     :on-click #(set-align-content :align-content-center)}
    (if is-col?
      i/align-content-column-center
      i/align-content-row-center)]
   [:button.align-end.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= align-content :align-content-end))
     :alt      (tr (dm/str "workspace.options.layout.align-content-end"))
     :on-click #(set-align-content :align-content-end)}
    (if is-col?
      i/align-content-column-end
      i/align-content-row-end)]
   [:button.align-around.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= align-content :align-content-around))
     :alt      (tr (dm/str "workspace.options.layout.align-content-around"))
     :on-click #(set-align-content :align-content-around)}
    (if is-col?
      i/align-content-column-around
      i/align-content-row-around)]
   [:button.align-between.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= align-content :align-content-between))
     :alt      (tr (dm/str "workspace.options.layout.align-content-between"))
     :on-click #(set-align-content :align-content-between)}
    (if is-col?
      i/align-content-column-between
      i/align-content-row-between)]])

(mf/defc justify-content-row
  [{:keys [is-col? justify-content set-justify] :as props}]
         ;;TODO iterar y eliminar código repetido
  [:div.justify-content-style
   [:button.justify-start.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= justify-content :justify-content-start))
     :alt      (tr (dm/str "workspace.options.layout.justify-content-start")) ;; TODO añadir lineas de textoa tradus
     :on-click #(set-justify :justify-content-start)}
    (if is-col?
      i/justify-content-column-start
      i/justify-content-row-start)]
   [:button.justify-center.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= justify-content :justify-content-center))
     :alt      (tr (dm/str "workspace.options.layout.justify-content-center"))
     :on-click #(set-justify :justify-content-center)}
    (if is-col?
      i/justify-content-column-center
      i/justify-content-row-center)]
   [:button.justify-end.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= justify-content :justify-content-end))
     :alt      (tr (dm/str "workspace.options.layout.justify-content-end"))
     :on-click #(set-justify :justify-content-end)}
    (if is-col?
      i/justify-content-column-end
      i/justify-content-row-end)]
   [:button.justify-space-between.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= justify-content :justify-content-space-between))
     :alt      (tr (dm/str "workspace.options.layout.justify-content-space-between"))
     :on-click #(set-justify :justify-content-space-between)}
    (if is-col?
      i/justify-content-column-between
      i/justify-content-row-between)]
   [:button.justify-space-around.tooltip.tooltip-bottom
    {:class    (dom/classnames :active  (= justify-content :justify-content-space-around))
     :alt      (tr (dm/str "workspace.options.layout.justify-content-space-around"))
     :on-click #(set-justify :justify-content-space-around)}
    (if is-col?
      i/justify-content-column-around
      i/justify-content-row-around)]
   ])

(mf/defc orientation-grid
  [{:keys [on-change-orientation values] :as props}]
  (let [dir       (:layout-dir values)
        type      (:layout-type values)
        is-col?   (or (= dir :top)
                      (= dir :bottom))
        saved-pos [(:layout-v-orientation values)
                   (:layout-h-orientation values)]]

    (if (= type :packed)
      [:div.orientation-grid
       [:div.button-wrapper
        (for [[pv ph] grid-pos]
          [:button.orientation
           {:on-click (partial on-change-orientation pv ph type)
            :class  (dom/classnames
                     :active (= [pv ph] saved-pos)
                     :top    (= :top pv)
                     :center (= :center pv)
                     :bottom (= :bottom pv)
                     :left   (= :left ph)
                     :center (= :center ph)
                     :right  (= :right ph))
            :key    (dm/str pv ph)}
           [:span.icon
            {:class (dom/classnames
                     :rotated (not is-col?))}
            (get-layout-icon dir type pv ph)]])]]
      (if  (not is-col?)
        [:div.orientation-grid.row
         [:div.button-wrapper
          (for [row grid-rows]
            [:button.orientation
             {:on-click (partial on-change-orientation row :left type)
              :class    (dom/classnames
                         :active   (= row (first saved-pos))
                         :top      (= :top row)
                         :centered (= :center row)
                         :bottom   (= :bottom row))}
             [:span.icon
              {:class (dom/classnames :rotated is-col?)}
              (get-layout-icon dir type nil row)]
             [:span.icon
              {:class (dom/classnames :rotated is-col?)}
              (get-layout-icon dir type nil row)]
             [:span.icon
              {:class (dom/classnames :rotated is-col?)}
              (get-layout-icon dir type nil row)]])]]

        [:div.orientation-grid.col
         [:div.button-wrapper
          (for [[idx col] (d/enumerate grid-cols)]
            [:button.orientation
             {:key (dm/str idx col)
              :on-click (partial on-change-orientation :top col type)
              :class  (dom/classnames
                       :active   (= col (second saved-pos))
                       :top      (= :left col)
                       :centered (= :center col)
                       :bottom   (= :right col))}
             [:span.icon
              {:class (dom/classnames :rotated is-col?)}
              (get-layout-icon dir type col nil)]
             [:span.icon
              {:class (dom/classnames :rotated is-col?)}
              (get-layout-icon dir type col nil)]
             [:span.icon
              {:class (dom/classnames :rotated is-col?)}
              (get-layout-icon dir type col nil)]])]]))))

(mf/defc padding-section
  [{:keys [values on-change-style on-change] :as props}]

  (let [padding-type (:layout-padding-type values)]

    [:div.padding-row
     [:div.padding-options
      [:div.padding-icon.tooltip.tooltip-bottom
       {:class (dom/classnames :selected (= padding-type :simple))
        :alt (tr "workspace.options.layout.padding-simple")
        :on-click #(on-change-style :simple)}
       i/auto-padding]
      [:div.padding-icon.tooltip.tooltip-bottom
       {:class (dom/classnames :selected (= padding-type :multiple))
        :alt (tr "workspace.options.layout.padding")
        :on-click #(on-change-style :multiple)}
       i/auto-padding-side]]
     [:div.wrapper
     (cond
       (= padding-type :simple)
       [:div.tooltip.tooltip-bottom
        {:alt (tr "workspace.options.layout.padding-all")}
        [:div.input-element.mini

         [:> numeric-input
          {:placeholder "--"
           :on-click #(dom/select-target %)
           :on-change (partial on-change :simple)
           :value (:p1 (:layout-padding values))}]]]

       (= padding-type :multiple)
       (for [num [:p1 :p2 :p3 :p4]]
         [:div.tooltip.tooltip-bottom
          {:key (dm/str "padding-" (d/name num))
           :alt (case num
                  :p1 (tr "workspace.options.layout.top")
                  :p2 (tr "workspace.options.layout.right")
                  :p3 (tr "workspace.options.layout.bottom")
                  :p4 (tr "workspace.options.layout.left"))}
          [:div.input-element.mini
           [:> numeric-input
            {:placeholder "--"
             :on-click #(dom/select-target %)
             :on-change (partial on-change num)
             :value (num (:layout-padding values))}]]])) 
      ]
     ]))

(mf/defc layout-container-menu
  {::mf/wrap [#(mf/memo' % (mf/check-props ["ids" "values" "type"]))]}
  [{:keys [ids _type values] :as props}]
  (let [open?               (mf/use-state false)
        layout-type         (mf/use-state :flex)
        gap-selected?       (mf/use-state false)
        wrap-style          (mf/use-state :no-wrap)
        toggle-open         (fn [] (swap! open? not))
        set-flex            (fn [] (reset! layout-type :flex))
        set-grid            (fn [] (reset! layout-type :grid))
        set-wrap            (fn [] (reset! wrap-style :wrap))
        set-no-wrap         (fn [] (reset! wrap-style :no-wrap))
        align-items         (mf/use-state :align-items-start)
        set-align-items     (fn [value] (reset! align-items value))
        justify-content     (mf/use-state :justify-content-start)
        set-justify-content (fn [value] (reset! justify-content value))
        gap-locked?         (mf/use-state true)
        toggle-gap-type     (fn [] (reset! gap-locked? not))
        gap-values          (mf/use-state {:row-gap 0 :column-gap 0})
        set-gap-value       (fn [gap-locked? gap-orientation value] 
                              (if gap-locked?
                                (reset! gap-values {:row-gap value :column-gap value})
                                (swap! gap-values assoc gap-orientation value)
                                )
                              (reset! justify-content value))
        on-add-layout
        (fn [_]
          (st/emit! (dwsl/create-layout ids)))

        on-remove-layout
        (fn [_]
          (st/emit! (dwsl/remove-layout ids))
          (reset! open? false))

        set-direction
        (fn [dir]
          (st/emit! (dwsl/update-layout ids {:layout-dir dir})))

        set-gap
        (fn [gap]
          (st/emit! (dwsl/update-layout ids {:layout-gap gap})))

        change-padding-style
        (fn [type]
          (st/emit! (dwsl/update-layout ids {:layout-padding-type type})))

        select-all-gap
        (fn [event]
          (reset! gap-selected? true)
          (dom/select-target event))

        on-padding-change
        (fn [type val]
          (if (= type :simple)
            (st/emit! (dwsl/update-layout ids {:layout-padding {:p1 val :p2 val :p3 val :p4 val}}))
            (st/emit! (dwsl/update-layout ids {:layout-padding {type val}}))))

        handle-change-type
        (fn [event]
          (let [target  (dom/get-target event)
                value   (dom/get-value target)
                value   (keyword value)]
            (st/emit! (dwsl/update-layout ids {:layout-type value}))))

        handle-wrap-style
        (mf/use-callback
         (fn [event]
           (let [target  (dom/get-target event)
                 value   (dom/get-value target)
                 value   (keyword value)]
             (st/emit! (dwsl/update-layout ids {:layout-wrap-style value})))))

        handle-change-orientation
        (fn [v-orientation h-orientation]
          (st/emit! (dwsl/update-layout ids {:layout-h-orientation h-orientation :layout-v-orientation v-orientation})))

        layout-info
        (fn []
          (let [type        (:layout-type values)
                dir         (:layout-dir values)
                is-col?     (or (= dir :column) (= dir :reverse-column))
                h           (:layout-h-orientation values)
                v           (:layout-v-orientation values)

                wrap        (:layout-wrap-style values)

                orientation
                (if (= type :packed)
                  (dm/str (tr (dm/str "workspace.options.layout.v." (d/name v))) ", "
                          (tr (dm/str "workspace.options.layout.h." (d/name h))) ", ")

                  (if is-col?
                    (dm/str (tr (dm/str "workspace.options.layout.h." (d/name h))) ", ")
                    (dm/str (tr (dm/str "workspace.options.layout.v." (d/name v)))  ", ")))]

            (dm/str orientation
                    (str/replace (tr (dm/str "workspace.options.layout." (d/name type))) "-" " ") ", "
                    (str/replace (tr (dm/str "workspace.options.layout." (d/name wrap))) "-" " "))))]

    [:div.element-set
     [:div.element-set-title
      [:*
       [:span (tr "workspace.options.layout.title")]
       (if (:layout values)
         [:div.title-actions
          [:div.layout-btns
           [:button {:on-click set-flex
                     :class (dom/classnames
                             :active (= :flex @layout-type))} "Flex"] ;; TODO tradus
           [:button {:on-click set-grid
                     :class (dom/classnames
                             :active (= :grid @layout-type))} "Grid"]] ;; TODO tradus
          [:button {:on-click on-remove-layout} i/minus]]

         [:button.add-page {:on-click on-add-layout} i/close])]]

     (when (:layout values)
       (if (= :flex @layout-type)
         [:div.element-set-content.layout-menu
          [:div.layout-row
           [:div.direction-wrap.row-title "Direction"] ;; TODO tradus
           [:div.btn-wrapper
            [:div.direction
             [:*
              (for [dir [:row :reverse-row :column :reverse-column]]
                [:& direction-btn {:key (d/name dir)
                                   :dir dir
                                   :saved-dir (:layout-dir values)
                                   :set-direction set-direction}])]]

            [:div.wrap-style
             [:& wrap-row {:wrap-style @wrap-style
                           :set-wrap set-wrap
                           :set-no-wrap set-no-wrap}]]]]

          (when (= :wrap @wrap-style)
           [:div.layout-row
           [:div.align-content.row-title "Content"] ;; TODO tradus
           [:div.btn-wrapper
            [:& align-content-row {:is-col? (or (= :column (:layout-dir values)) (= :column-reverse (:layout-dir values)))
                                   :align-items @align-items
                                   :set-align set-align-items}]]])
          
          [:div.layout-row
           [:div.align-items.row-title "Align"] ;; TODO tradus
           [:div.btn-wrapper
            [:& align-row {:is-col? (or (= :column (:layout-dir values)) (= :column-reverse (:layout-dir values)))
                           :align-items @align-items
                           :set-align set-align-items}]]]

          [:div.layout-row
           [:div.justify-content.row-title "Justify"] ;; TODO tradus
           [:div.btn-wrapper
            [:& justify-content-row {:is-col? (or (= :column (:layout-dir values)) (= :column-reverse (:layout-dir values)))
                                     :justify-content @justify-content
                                     :set-justify set-justify-content}]]]

          [:div.gap-group
           [:div.gap-row.tooltip.tooltip-bottom-left
            {:alt (tr "workspace.options.layout.gap-row")} ;;TODO Add tradu
            [:span.icon
             {:class (dom/classnames :activated (= @gap-selected? :gap-row))}
             i/auto-gap]
            [:> numeric-input {:no-validate true
                               :placeholder "--"
                               :on-click select-all-gap
                               :on-change set-gap
                               :on-blur #(reset! gap-selected? false)
                               :value (:layout-gap values)}]]

           [:div.gap-column.tooltip.tooltip-bottom-left
            {:alt (tr "workspace.options.layout.gap-column")}  ;;TODO Add tradu
            [:span.icon.rotated
             {:class (dom/classnames
                      :activated (= @gap-selected? true))}
             i/auto-gap]
            [:> numeric-input {:no-validate true
                               :placeholder "--"
                               :on-click select-all-gap
                               :on-change set-gap
                               :on-blur #(reset! gap-selected? false)
                               :value (:layout-gap values)}]]
           [:button {:on-click toggle-gap-type
                     :class (dom/classnames :active gap-locked?)} i/lock]]

          [:& padding-section {:values values
                               :on-change-style change-padding-style
                               :on-change on-padding-change}]]

         [:div "GRID TO COME"]))]))
