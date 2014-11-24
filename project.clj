(defproject gamejme3 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :jvm-opts ["-Xmx512m"]
  :main gamejme3.core
  :repositories {"oss-sonatype" 
                 "https://oss.sonatype.org/content/repositories/snapshots/"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.jme3/jmonkeyengine3 "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-testdata "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-terrain "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-plugins "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-niftygui "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-plugins "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-lwjgl "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-lwjgl-natives "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-jogg "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-jbullet "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-desktop "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-core "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-blender "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-effects "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jME3-networking "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/j-ogg-oggd "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/j-ogg-vorbisd "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/eventbus "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jbullet "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/jinput "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/lwjgl "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/nifty "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/nifty-default-controls "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/nifty-style-black "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/nifty-examples "3.0.0.20140325-SNAPSHOT"]
                 ;[com.jme3/noise "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/stack-alloc "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/vecmath "3.0.0.20140325-SNAPSHOT"]
                 [com.jme3/xmlpull-xpp3 "3.0.0.20140325-SNAPSHOT"]]
  :profiles {:uberjar {:aot :all}
             :dev {
                   :dependencies [
                                  [midje "1.5.1"] 
                                  ]}}
  
  )
