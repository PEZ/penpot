; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) KALEIDOS INC

(ns app.main.ui.workspace.viewport.top-bar
  (:require-macros [app.main.style :as stl])
  (:require
   [app.common.files.helpers :as cfh]
   [app.common.types.shape.layout :as ctl]
   [app.main.data.workspace :as dw]
   [app.main.refs :as refs]
   [app.main.store :as st]
   [app.main.ui.context :as ctx]
   [app.main.ui.workspace.viewport.grid-layout-editor :refer [grid-edition-actions]]
   [app.main.ui.workspace.viewport.path-actions :refer [path-actions]]
   [app.util.i18n :as i18n :refer [tr]]
   [rumext.v2 :as mf]))

(mf/defc view-only-actions
  []
  (let [handle-close-view-mode
        (mf/use-callback
         (fn []
           (st/emit! :interrupt
                     (dw/set-options-mode :design)
                     (dw/set-workspace-read-only false))))]
    [:div {:class (stl/css :viewport-actions)}
     [:div {:class (stl/css :viewport-actions-container)}
      [:div {:class (stl/css :viewport-actions-title)}
       [:& i18n/tr-html {:tag-name "span"
                         :label "workspace.top-bar.read-only"}]]
      [:button {:class (stl/css :done-btn)
                :on-click handle-close-view-mode} (tr "workspace.top-bar.read-only.done")]]]))

(mf/defc top-bar
  {::mf/wrap [mf/memo]}
  []
  (let [edition     (mf/deref refs/selected-edition)
        selected    (mf/deref refs/selected-objects)
        drawing     (mf/deref refs/workspace-drawing)
        drawing-obj (:object drawing)
        shape       (or drawing-obj (-> selected first))

        single? (= (count selected) 1)
        editing? (= (:id shape) edition)
        draw-path? (and (some? drawing-obj)
                        (cfh/path-shape? drawing-obj)
                        (not= :curve (:tool drawing)))

        workspace-read-only? (mf/use-ctx ctx/workspace-read-only?)

        path-edition? (or (and single? editing?
                               (and (not (cfh/text-shape? shape))
                                    (not (cfh/frame-shape? shape))))
                          draw-path?)

        grid-edition? (and single? editing? (ctl/grid-layout? shape))]

    (cond
      workspace-read-only?
      [:& view-only-actions]

      path-edition?
      [:div.viewport-actions
       [:& path-actions {:shape shape}]]

      grid-edition?
      [:& grid-edition-actions {:shape shape}])))
