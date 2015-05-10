(ns io.homegrown.sim-ephemeral-test
  (:require [clojure.test :refer :all]
            [io.homegrown.sim-ephemeral :refer :all]
            [simulant.sim :as sim]))


(defn dummy-process-state
  "Return a dummy *services* implementation that acts like simulant.sim/*services*"
  []
  {:simulant.sim/processState (doto (sim/construct-process-state {} {})
                                (sim/start-service {}))})

(def agent-a {:db/id 123456789})

(deftest round-trip-storage
  (binding [simulant.sim/*services* (dummy-process-state)]
    (store agent-a :thing :db.cardinality/one :db.type/string "ABC")
    (store agent-a :things :db.cardinality/many :db.type/string ["ABC" "123"])
    (is (= "ABC" (retrieve agent-a :thing)))
    (is (= #{"ABC" "123"} (set (retrieve agent-a :things))))
    (clear agent-a)
    (is (nil? (retrieve agent-a :thing)))
    (is (nil? (retrieve agent-a :things)))))
