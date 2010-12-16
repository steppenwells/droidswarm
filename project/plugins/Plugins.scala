import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {

  val guRepo = "guardian.github.com" at "http://guardian.github.com/maven/repo-releases"

  val android = "org.scala-tools.sbt" % "sbt-android-plugin" % "0.5.0"
  val ideaPlugin = "com.github.mpeltonen" % "sbt-idea-plugin" % "0.1-GUPATCH-1"
}
