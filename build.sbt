name := "schemaxpect"

organization := "com.freevariable"

version := "0.0.1"

val SPARK_VERSION = "2.0.2"

scalaVersion := "2.11.8"

scalaVersion in ThisBuild := "2.11.8"

def commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % SPARK_VERSION % "provided",
    "org.apache.spark" %% "spark-sql" % SPARK_VERSION % "provided",
    "org.apache.spark" %% "spark-mllib" % SPARK_VERSION % "provided",
    "org.slf4j" % "slf4j-nop" % "1.7.6" % Test,
    "com.redhat.et" %% "silex" % "0.1.0",
    "org.json4s" %% "json4s-jackson" % "3.2.10" % "provided"
  )
)

seq(commonSettings:_*)

licenses += ("Apache-2.0", url("http://opensource.org/licenses/Apache-2.0"))

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

scalacOptions in (Compile, doc) ++= Seq("-doc-root-content", baseDirectory.value+"/root-doc.txt")

(dependencyClasspath in Test) <<= (dependencyClasspath in Test).map(
  _.filterNot(_.data.name.contains("slf4j-log4j12"))
)

lazy val schemaxpect = project in file(".")

lazy val spark = project.dependsOn(schemaxpect)
  .settings(commonSettings:_*)
  .settings(
    name := "schemaxpect",
    publishArtifact := false,
    publish := {},
    initialCommands in console := """
      |import org.apache.spark.SparkConf
      |import org.apache.spark.SparkContext
      |import org.apache.spark.SparkContext._
      |import org.apache.spark.rdd.RDD
      |val app = new com.redhat.et.silex.app.ConsoleApp()
      |val spark = app.context
    """.stripMargin,
    cleanupCommands in console := "spark.stop")
