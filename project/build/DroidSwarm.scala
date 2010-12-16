import sbt._

trait Defaults {
  def androidPlatformName = "android-8"
}
class DroidSwarm(info: ProjectInfo) extends ParentProject(info) with IdeaProject {
  override def shouldCheckOutputDirectories = false
  override def updateAction = task { None }

  lazy val main  = project(".", "DroidSwarm", new MainProject(_))
  lazy val tests = project("tests",  "tests", new TestProject(_), main)

  class MainProject(info: ProjectInfo) extends AndroidProject(info) with Defaults with MarketPublish with IdeaProject {
    val keyalias  = "change-me"
    val scalatest = "org.scalatest" % "scalatest" % "1.0" % "test"
  }

  class TestProject(info: ProjectInfo) extends AndroidTestProject(info) with Defaults with IdeaProject
}
