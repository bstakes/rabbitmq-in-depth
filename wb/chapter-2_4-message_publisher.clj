;; gorilla-repl.fileformat = 1

;; **
;;; # 2.4 Writing a message publisher in Clojure
;;; 
;;; ## Namespace and requires
;; **

;; @@
(ns chapter-2_4-examples
  (:require [langohr.core     :as lc]
            [langohr.channel  :as lch]
            [langohr.exchange :as le]
            [langohr.queue    :as lq]
            [langohr.basic    :as lb]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; ## Connection and Channel
;;; Basic Connection and channels. Assumes RabbitMQ is running locally with all of the defaults.
;; **

;; @@
(def connection (lc/connect))
(def channel (lch/open connection))

;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;chapter-2_4-examples/channel</span>","value":"#'chapter-2_4-examples/channel"}
;; <=

;; **
;;; ## Exchanges
;;; 
;;; Exchanges are where messages are sent. Queues are bound to exchanges. If a queue is bound to an exchange, when the exchange receives a message and the queue is configured to receive the message (i.e. via routing keys), then the message will be sent to the queue. The `rabbitpy` library in the book defaults to a direct exchange when no type is specified.
;;; 
;;; [rabbitpy](https://github.com/gmr/rabbitpy/blob/109e44392785a6b0de5994cc2162352f73576756/rabbitpy/exchange.py#L35)
;;; * direct exchange
;;; * auto delete: false
;;; * durable: false
;;; * passive: true
;;; 
;;; 
;;; [langhor exchange](https://github.com/michaelklishin/langohr/blob/2e80a6df0063f25d6c0f484dd87e5786e2028c0f/src/clojure/langohr/exchange.clj#L72)
;;; * no default - must declare exchange type explicitly
;;; * auto delete: false
;;; * durable: false
;;; * passive: false? - there is a declare-passive symbol which may handle passive declaration and might be used here for parity with the book
;; **

;; @@
(def exchange (le/declare channel "chapter2-example" "direct"))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;chapter-2_4-examples/exchange</span>","value":"#'chapter-2_4-examples/exchange"}
;; <=

;; **
;;; ## Queues
;;; 
;;; Queues are where messages are delivered from the exchanges. [rabbitpy](https://github.com/gmr/rabbitpy/blob/109e44392785a6b0de5994cc2162352f73576756/rabbitpy/amqp_queue.py#L68) defaults auto-delete to false, [langohr](https://github.com/michaelklishin/langohr/blob/2e80a6df0063f25d6c0f484dd87e5786e2028c0f/src/clojure/langohr/queue.clj#L65) is true.
;; **

;; @@
(def queue (lq/declare channel "example" {:auto-delete false}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;chapter-2_4-examples/queue</span>","value":"#'chapter-2_4-examples/queue"}
;; <=

;; **
;;; Bind the queue to the exchange
;; **

;; @@
(lq/bind channel (:queue queue) "chapter2-example" {:routing-key "example-routing-key"})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[],"value":"{}"}
;; <=

;; **
;;; ## Publish
;;; 
;;; Publish some messages to the exchange.
;;; 
;;; rabbitpy has an `opinionated` flag that will automatically set a message id and a timestamp. Langohr does not do this by default, so you must publish messages with these properties if you want to see it with the output.
;; **

;; @@
(dotimes [n 10]
  (lb/publish 
    channel 
    "chapter2-example" 
    "example-routing-key" 
    (str "Test message " n) 
    {:content-type "text/plain" 
     :message-id (str (java.util.UUID/randomUUID))
     :timestamp (java.util.Date.)}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; ## Full Example
;; **

;; @@
(let [ex-name "chapter2-example-redux"
      ex-type "direct"
      q-name  "example-redux"
      r-key   "chapter2-example-redux"
      c       (lc/connect)
      ch      (lch/open c)
      ex      (le/declare ch ex-name ex-type)
      q       (lq/declare ch q-name {:auto-delete false})]
  (lq/bind ch q-name ex-name {:routing-key r-key})
  (dotimes [n 10]
    (lb/publish ch ex-name r-key (str "Test message: " n) {:content-type "text/plain"})))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=
