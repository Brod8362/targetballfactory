name := "tbf3s"

version := "0.1"

scalaVersion := "2.13.1"

resolvers += "jcenter-bintray" at "https://jcenter.bintray.com"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"       % "3.4.1",
  "com.h2database"  %  "h2"                % "1.4.200",
  "ch.qos.logback"  %  "logback-classic"   % "1.2.3",
  "org.xerial" % "sqlite-jdbc" % "3.7.2",
  "net.dv8tion" % "JDA" % "4.0.0_79",
  "org.yaml" % "snakeyaml" % "1.21",
  "org.jfree" % "jfreechart" % "1.5.0",
  "com.sedmelluq" % "lavaplayer" % "1.3.47"
)

