import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "symfluence"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "com.mongodb.casbah" %% "casbah" % "2.1.5-1",
      "joda-time" % "joda-time" % "2.0",
      "org.joda" % "joda-convert" % "1.2",
      "net.sf.ehcache" %  "ehcache-core" %   "2.5.0",
      "mysql" % "mysql-connector-java" % "5.1.10"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

    testOptions in Test := Nil 

}
