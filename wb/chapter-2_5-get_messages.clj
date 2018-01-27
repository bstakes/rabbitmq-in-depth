;; gorilla-repl.fileformat = 1

;; **
;;; # 2.5 Getting messages from RabbitMQ
;;; 
;;; Gets messages directly from a queue
;;; 
;;; ## Namespace and requires
;; **

;; @@
(ns chapter-2_5-examples
  (:require [langohr.core      :as lc]
            [langohr.basic     :as lb]
            [langohr.channel   :as lch]
            [langohr.queue     :as lq]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; ## Connection and channel
;; **

;; @@
(def conn (lc/connect))
(def ch (lch/open conn))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;chapter-2_5-examples/ch</span>","value":"#'chapter-2_5-examples/ch"}
;; <=

;; **
;;; ## Get messages from queue
;;; 
;;; Assumes messages have been [published](/worksheet.html?filename=wb/chapter-2_4-message_publisher.clj)
;;; 
;;; ### Message count
;; **

;; @@
(defn bytes->str 
  [^bytes bs]
  (apply str (map char bs)))

(lq/message-count ch "example")
(let [[meta payload](lb/get ch "example")] 
  (println meta)
  (bytes->str payload))
;; @@
;; ->
;;; {:cluster-id nil, :app-id nil, :message-id nil, :expiration nil, :type nil, :user-id nil, :headers nil, :delivery-tag 21, :delivery-mode 1, :priority nil, :redelivery? false, :routing-key example-routing-key, :content-type text/plain, :persistent? false, :reply-to nil, :content-encoding nil, :correlation-id nil, :exchange chapter2-example, :message-count 9, :timestamp nil}
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Test message 0&quot;</span>","value":"\"Test message 0\""}
;; <=

;; @@
(dotimes [n (lq/message-count ch "example")]
  (let [[{:keys [message-id timestamp]} payload] (lb/get ch "example")]
    (println "Message:")
    (println (str "  ID: " message-id))
    (println (str "      Time:      " timestamp))
    (println (str "      Body:      " (bytes->str payload)))))
;; @@
;; ->
;;; Message:
;;;   ID: 046010d4-63f5-4f2f-8b07-f41f831513e6
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 0
;;; Message:
;;;   ID: 34c230cd-3497-4a09-84c2-ad413d53eee4
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 1
;;; Message:
;;;   ID: 2e6995a6-3945-44db-8129-d33029454ee5
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 2
;;; Message:
;;;   ID: 3b61e830-6b8a-4992-8b55-423962262ddd
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 3
;;; Message:
;;;   ID: e331e3c6-3504-4e00-b490-e9c4305706ba
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 4
;;; Message:
;;;   ID: 803e8ff1-0534-4acf-92db-ba54e422b22f
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 5
;;; Message:
;;;   ID: 4f17d5a5-388e-421e-b80d-c91cd938fe04
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 6
;;; Message:
;;;   ID: fd9e48e4-f3ef-48fd-9c13-545c94b0d691
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 7
;;; Message:
;;;   ID: ddd27349-af1a-40e1-8144-aeffaab54533
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 8
;;; Message:
;;;   ID: b165f8fb-a876-402e-8f0c-10ccf4cbbb77
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 9
;;; Message:
;;;   ID: 18e46005-c927-4a1d-ac23-202695f850c0
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 10
;;; Message:
;;;   ID: 706a59f6-d5c4-4da6-86c8-a4bc7f2e1170
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 11
;;; Message:
;;;   ID: a223b6b2-3f7d-4a38-99de-75bdeb0b3d83
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 12
;;; Message:
;;;   ID: ef91c63e-6558-4e3e-ac9b-927c69a04711
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 13
;;; Message:
;;;   ID: 5c562076-64c3-40d6-94f6-3e3ce470d3b2
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 14
;;; Message:
;;;   ID: cab8261d-d4d8-4adf-8ceb-427a5fbd9194
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 15
;;; Message:
;;;   ID: 36618e98-9045-42a2-9cab-c6b75a9a7758
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 16
;;; Message:
;;;   ID: 2d324186-e1c1-40ea-937c-75659bbaacce
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 17
;;; Message:
;;;   ID: 2ceb0ec4-723b-484e-b2e6-67932658d411
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 18
;;; Message:
;;;   ID: ccbc5126-5463-4c2f-be01-fa2d58e88784
;;;       Time:      Thu Jan 04 12:34:25 EST 2018
;;;       Body:      Test message 19
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; 
;; **
