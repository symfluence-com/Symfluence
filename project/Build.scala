import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "symfluence"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "com.mongodb.casbah" %% "casbah" % "2.1.5-1",
      "joda-time" % "joda-time" % "2.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
