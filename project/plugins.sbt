logLevel := Level.Warn

resolvers ++= Seq(
    DefaultMavenRepository,
    Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns),
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "ScalaTools" at "http://scala-tools.org/repo-releases"
)

addSbtPlugin("play" % "sbt-plugin" % "2.0-RC1-SNAPSHOT")
