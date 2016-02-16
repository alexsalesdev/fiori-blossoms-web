name := """fiori-blossoms"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  filters,
"com.typesafe.play" %% "play-slick-evolutions" % "1.1.1",
  "com.h2database" % "h2" % "1.4.190",
  "org.scoverage" %% "scalac-scoverage-runtime" % "1.1.1",
  "org.scalatest" %% "scalatest" % "2.2.4" % Test,
  "org.scalatestplus" %% "play" % "1.4.0-M3" % Test,
  "net.ceedubs" %% "ficus" % "1.1.2",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.mohiva" %% "play-silhouette" % "3.0.0",
  "org.apache.commons" % "commons-email" % "1.3.1"
)

resolvers ++= Seq("scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  "Apache" at "http://repo1.maven.org/maven2/",
  "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/",
  "Atlassian Releases" at "https://maven.atlassian.com/public/"
)

resolvers += Resolver.url("Typesafe Ivy releases", url("https://repo.typesafe.com/typesafe/ivy-releases"))(Resolver.ivyStylePatterns)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

fork in run := false

coverageEnabled := true

coverageExcludedPackages := "<empty>;controllers.javascript.*;router.*;"

coverageMinimum := 85

coverageFailOnMinimum := false
