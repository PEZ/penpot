(ns app.main.ui.browser-hooks
  "A collection of general purpose react hooks."
  (:require
   [app.main.broadcast :as mbc]
   [app.util.storage :refer [storage]]
   [beicon.core :as rx]
   [rumext.v2 :as mf]))

(defn use-shared-state
  "A specialized hook that adds persistence and inter-context reactivity
  to the default mf/use-state hook.

  The state is automatically persisted under the provided key on
  localStorage. And it will keep watching events with type equals to
  `key` for new values."
  [key default]
  (let [id     (mf/use-id)
        state  (mf/use-state (get @storage key default))
        stream (mf/with-memo [id]
                 (->> mbc/stream
                      (rx/filter #(not= (:id %) id))
                      (rx/filter #(= (:type %) key))
                      (rx/map deref)))]

    (mf/with-effect [@state key id]
      (mbc/emit! id key @state)
      (swap! storage assoc key @state))

    (use-stream stream (partial reset! state))

    state))