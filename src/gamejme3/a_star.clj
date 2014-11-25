(ns gamejme3.a-star
  (:require [clojure.data.priority-map :refer :all])
  (:use clojure.pprint)
  )
(def maze1 [[0 1 0 0 0 0 0]
            [0 1 0 1 0 1 0]
            [0 1 0 1 0 1 0]
            [0 1 0 1 0 1 0]
            [0 0 0 0 0 1 0]])
(def start [0 1])
(def end [6 4])

(defn get-cell-state [map x y] (nth (nth map y) x))

(defn get-dim-y [map] (count map))

(defn get-dim-x [map] (-> map first count))

(defn manhattan-distance [[x1 y1] [x2 y2]]
  (+ (Math/abs ^Integer (- x2 x1)) (Math/abs ^Integer (- y2 y1))))

(defn calc-h [map node target]
  (manhattan-distance node target)
  )

(defn calc-delta-g [m node target]
  (cond
      (or (= (first node)
             (first target))
          (= (last node)
             (last target))
          ) 10
      :default 14)
  )

(defn get-edges [m closed-list [x y]]
  (for [tx (range (- x 1) (+ x 2)) 
        ty (range (- y 1) (+ y 2))
        :when (and (>= tx 0)
                   (>= ty 0)
                   (<= tx (-> m get-dim-x dec))
                   (<= ty (-> m get-dim-y dec))
                   (not= [x y] [tx ty])
                   (not= (get-cell-state m tx ty) 1)
                   (not (get closed-list [tx ty])))]
    [tx ty])
  )

(defn search-node [m open-list closed-list node target parent]
  (let [
        closed-list* (assoc closed-list node true)
        edges (get-edges m closed-list* node)
        edge-list (apply hash-map (mapcat (fn [n]
                              (let [
                                    df (+ (calc-h m n target)
                                          (calc-delta-g m n target))
                                    f (if parent
                                        (+ df (:f parent))
                                        df)

                                    ]
                                [n {:f f :parent node}] 
                                )
                              ) edges))

        open-list* (merge-with (fn [old new] 
                                 (if (< (:f new) (:f old))
                                   new
                                   old))
                               open-list edge-list)
        ]
    [closed-list* edge-list open-list*]
    ))

(defn get-path [m start end]

  )


(comment 


  (defn cost [curr start end]
    (let [g (manhattan-distance start curr)
          h (manhattan-distance curr end)
          f (+ g h)] 
      [f g h]))

  (defn edges [m width height closed [x y]]
    (for [tx (range (- x 1) (+ x 2)) 
          ty (range (- y 1) (+ y 2))
          :when (and (>= tx 0)
                     (>= ty 0)
                     (<= tx width)
                     (<= ty height)
                     (not= [x y] [tx ty])
                     (not= (nth (nth m ty) tx) 1)
                     (not (contains? closed [tx ty])))]
      [tx ty]))

  (defn path [end parent closed]
    (reverse
     (loop [path [end parent]
            node (closed parent)]
       (if (nil? node)
         path
         (recur (conj path node) (closed node))))))

  (defn search 
    ([m start end]
       (let [[sx sy] start
             [ex ey] end
             open (priority-map-by
                   (fn [x y]
                     (if (= x y)
                       0
                       (let [[f1 _ h1] x
                             [f2 _ h2] y]
                         (if (= f1 f2)
                           (if (< h1 h2) -1 1)
                           (if (< f1 f2) -1 1)))))
                   start (cost start start end))
             closed {}
             width (-> map first count dec)
             height (-> map count dec)]
         (when (and (not= (nth (nth map sy) sx) 1)
                    (not= (nth (nth map ey) ex) 1))
           (search map width height open closed start end))))

    ([map width height open closed start end]
       (if-let [[coord [_ _ _ parent]] (peek open)]
         (if-not (= coord end)
           (let [closed (assoc closed coord parent)
                 edges (edges map width height closed coord)
                 open (reduce
                       (fn [open edge]
                         (if (not (contains? open edge))
                           (assoc open edge (conj (cost edge start end) coord))
                           (let [[_ pg] (open edge)
                                 [nf ng nh] (cost edge start end)]
                             (if (< ng pg)
                               (assoc open edge (conj [nf ng nh] coord))
                               open))))
                       (pop open) edges)]
             (recur map width height open closed start end))
           (path end parent closed)))))

  (defn draw-map [area start end]
    (let [path (into #{} (time (search area start end)))
          area (map-indexed
                (fn [idx-row row]
                  (map-indexed
                   (fn [idx-col col]
                     (cond 
                      (and (= (first start) idx-col)
                           (= (last start) idx-row)
                           ) \S
                             (and (= (first end) idx-col)
                                  (= (last end) idx-row)
                                  ) \E
                                    (contains? path [idx-col idx-row]) \X
                                    (= 1 col) \#
                                    :default \space))
                   row))
                area)]

      (doseq [line area]
        (println line))))

  )
