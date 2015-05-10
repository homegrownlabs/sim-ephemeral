(set-env!
  :source-paths #{"src" "test"}
  :dependencies '[[org.clojure/clojure "1.6.0" :scope "provided"]
                  [com.datomic/simulant "0.1.7"]
                  [com.datomic/datomic-free "0.9.5130"]
                  [io.rkn/conformity "0.3.2" :exclusions [com.datomic/datomic-free]]
                  [adzerk/bootlaces "0.1.11" :scope "test"]
                  [adzerk/boot-test "1.0.4" :scope "test"]])

(require '[adzerk.bootlaces :refer :all]
         '[adzerk.boot-test :refer :all])

;; Load Datomic data readers
(#'clojure.core/load-data-readers)
(set! *data-readers* (.getRawRoot #'*data-readers*))

(def +version+ "1.0.0")
(bootlaces! +version+)

(task-options! pom {:project 'io.homegrown/sim-ephemeral
                    :version +version+
                    :url "https://github.com/homegrownlabs/sim-ephemeral"
                    :scm {:url "https://github.com/homegrownlabs/sim-ephemeral"}
                    :description "Helpers for Simulant's process state service"
                    :license {"The MIT License"
                              "http://opensource.org/licenses/MIT"}})
