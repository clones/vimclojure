;-
; Copyright 2008 (c) Meikel Brandmeyer.
; All rights reserved.
;
; Permission is hereby granted, free of charge, to any person obtaining a copy
; of this software and associated documentation files (the "Software"), to deal
; in the Software without restriction, including without limitation the rights
; to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
; copies of the Software, and to permit persons to whom the Software is
; furnished to do so, subject to the following conditions:
;
; The above copyright notice and this permission notice shall be included in
; all copies or substantial portions of the Software.
;
; THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
; IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
; FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
; AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
; LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
; OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
; THE SOFTWARE.

(clojure.core/ns de.kotka.gorilla
  (:gen-class)
  (:require
     clojure.contrib.repl-ln)
  (:import
     (java.io PushbackReader StringReader)
     (java.net InetAddress ServerSocket Socket)
     (java.lang.reflect Modifier Method Constructor)))

;; From: http://groups.google.com/group/clojure/msg/96ed91f823305f02
;; by: Chris Houser
;; usage:
;; (show Object)   ; give it a class
;; (show Object 1) ; a class and a method number to see details
;; (show {})       ; or give it an instance

(defn show
  ([x] (show x nil))
  ([x i]
   (let [c (if (class? x) x (class x))
         items (sort
                 (for [m (concat (.getFields c)
                                 (.getMethods c)
                                 (.getConstructors c))]
                   (let [static? (bit-and Modifier/STATIC
                                          (.getModifiers m))
                         method? (instance? Method m)
                         ctor?   (instance? Constructor m)
                         text (if ctor?
                                (str "(" (apply str (interpose ", " (.getParameterTypes m))) ")")
                                (str
                                  (if (pos? static?) "static ")
                                  (.getName m) " : "
                                  (if method?
                                    (str (.getReturnType m) " ("
                                         (count (.getParameterTypes m)) ")")
                                    (str (.getType m)))))]
                     [(- static?) method? text (str m) m])))]
     (if i
       (last (nth items i))
       (do (println "=== " c " ===")
         (doseq [[e i] (map list items (iterate inc 0))]
           (printf "[%2d] %s%n" i (nth e 2))))))))

(defn get-javadoc-path
  [x]
  (-> x .getName (.replace \. \/) (.replace \$ \.) (.concat ".html")))

(defn go-word-position
  [v]
  (let [m      (meta v)
        vns    (name (ns-name (m :ns)))
        nsbase (.substring vns (inc (.lastIndexOf vns (int \.))))
        nspath (.replace vns \. \/)
        file   (m :file)
        fbase  (.substring file 0 (.lastIndexOf file (int \.)))
        line   (m :line)]
    (if (= fbase nsbase)
      (str nspath ".clj " line)
      (str nspath "/" file " " line))))

(defn check-completeness
  [input]
  (let [irdr (PushbackReader. (StringReader. input))
        eof  (Object.)]
    (loop []
      (try
        (if (= (read irdr false eof) eof)
          true
          (recur))
        (catch Exception _
          false)))))

(defn handle-connection
  [conn]
  (-> (fn []
        (binding [*ns* *ns*]
          (in-ns 'user)
          (let [in  (.getInputStream conn)
                out (.getOutputStream conn)]
            (clojure.contrib.repl-ln/repl :in in :out out :err out
                                          :prompt-fmt "Gorilla=> ")))
        (.close conn)
        (println "Connection closed."))
    Thread.
    .start))

(defn server-loop
  [server]
  (let [conn (.accept server)]
    (println "Connection started.")
    (handle-connection conn)
    (recur server)))

(defn -main
  [& _]
  (let [address (InetAddress/getByName "localhost")
        server  (new ServerSocket 10123 0 address)]
    (println "Listening...")
    (server-loop server)))
