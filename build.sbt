name := "WordRank"
version := "1.0"
scalaVersion := "2.12.1"

crossPaths := false
autoScalaLibrary := false
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")


libraryDependencies ++= Seq(
  // commons
  "org.apache.commons" % "commons-lang3" % "3.4",
  "org.apache.commons" % "commons-math3" % "3.1.1",
  "commons-io" % "commons-io" % "2.5",

  // logging
  "org.apache.logging.log4j" % "log4j-api" % "2.8.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.8.1",

  "com.mashape.unirest" % "unirest-java" % "1.4.9",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.0",
  "io.reactivex.rxjava2" % "rxjava" % "2.0.7",
  "org.springframework.boot" % "spring-boot-starter" % "1.0.1.RELEASE",

  //test
  "org.springframework" % "spring-test" % "4.2.9.RELEASE",
  "org.mockito" % "mockito-core" % "2.6.0",
  "junit" % "junit" % "4.10" % "test"
)

