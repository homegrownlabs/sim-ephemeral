(ns io.homegrown.sim-ephemeral
  "Ephemeral storage for running agents.

  Background:

  Agents have access to a temporary in-memory Datomic database during a sim
  run (via the process-state service). Certain information (like current auth
  credentials) needs to be remembered during a sim run, but is irrelevant to
  validation.

  Usage:

  1. During sim setup invoke `simulant.sim/create-process-state`.
  2. Inside an agent action, invoke `retrieve`, `store`, or `clear` as
  necessary. These functions will automatically gain access to a process-local,
  in-memory Datomic database. See individual function docs for more information
  on usage.

  Caveats:

  Because process-state stores are local to a Simulant process, agents running
  on separate processes *will not* be able to access each other's keys/values."
  (:require [simulant.sim :as sim]
            [simulant.util :refer [e]]
            [datomic.api :as d]
            [io.rkn.conformity :as c]))

(defn- ensure-attribute
  "Ensure a database connection has an attribute installed. If not, install it."
  [conn attr-ident attrs]
  (when-not (c/has-attribute? (d/db conn) attr-ident)
    @(d/transact conn [(merge {:db/id (d/tempid :db.part/db)
                               :db/ident attr-ident
                               :db.install/_attribute :db.part/db}
                              attrs)])) )

(defn- init-ephemeral-store [conn]
  (ensure-attribute conn :ephemeral/agent {:db/cardinality :db.cardinality/one
                                           :db/valueType :db.type/ref
                                           :db/unique :db.unique/identity}))

(defn store
  "Store ephemeral information for an agent.

  attr, cardinality, and type arguments correspond to Datomic :db/ident,
  :db/valueType and :db/cardinality schema attributes, respectively."
  [agent attr cardinality type val]
  (let [conn (sim/connect (get sim/*services* :simulant.sim/processState))]
    (init-ephemeral-store conn)
    (ensure-attribute conn attr {:db/cardinality cardinality
                                 :db/valueType type})
    @(d/transact conn [{:db/id (d/tempid :db.part/user)
                        :ephemeral/agent (e agent)
                        attr val}])))

(defn retrieve
  "Retrieve ephemeral information about an agent."
  [agent attr]
  (let [conn (sim/connect (get sim/*services* :simulant.sim/processState))]
    (init-ephemeral-store conn)
    (when (c/has-attribute? (d/db conn) attr)
      (-> (d/pull (d/db conn) [attr] [:ephemeral/agent (e agent)])
          (get attr)))))

(defn clear
  "Clear an agent's ephemeral information"
  [agent]
  (let [conn (sim/connect (get sim/*services* :simulant.sim/processState))]
    (init-ephemeral-store conn)
    (d/transact conn [[:db.fn/retractEntity [:ephemeral/agent (e agent)]]])))
