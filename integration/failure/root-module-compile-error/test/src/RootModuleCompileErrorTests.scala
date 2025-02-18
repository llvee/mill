package mill.integration

import mill.testkit.IntegrationTestSuite

import utest._

object RootModuleCompileErrorTests extends IntegrationTestSuite {
  val tests: Tests = Tests {
    initWorkspace()

    test {
      val res = eval("foo.scalaVersion")

      assert(res.isSuccess == false)
      // For now these error messages still show generated/mangled code; not ideal, but it'll do
      assert(res.err.contains("""build.mill:6:42: not found: type UnknownRootModule"""))
      assert(res.err.contains("""class  package_  extends RootModule with UnknownRootModule {"""))
      assert(res.err.replace('\\', '/').contains(
        """foo/package.mill:6:59: not found: type UnknownFooModule"""
      ))
      assert(res.err.contains(
        """class  package_  extends RootModule.Subfolder("foo") with UnknownFooModule {"""
      ))

      assert(res.err.contains("""build.mill:7:22: not found: value unknownRootInternalDef"""))
      assert(res.err.contains("""def scalaVersion = unknownRootInternalDef"""))
      assert(res.err.contains("""build.mill:4:23: not found: type UnknownBeforeModule"""))
      assert(res.err.contains("""object before extends UnknownBeforeModule"""))
      assert(res.err.contains("""build.mill:10:22: not found: type UnknownAfterModule"""))
      assert(res.err.contains("""object after extends UnknownAfterModule"""))

      assert(res.err.replace('\\', '/').contains(
        """foo/package.mill:7:22: not found: value unknownFooInternalDef"""
      ))
      assert(res.err.contains("""def scalaVersion = unknownFooInternalDef"""))
      assert(res.err.replace('\\', '/').contains(
        """foo/package.mill:4:23: not found: type UnknownBeforeFooModule"""
      ))
      assert(res.err.contains("""object before extends UnknownBeforeFooModule"""))
      assert(res.err.replace('\\', '/').contains(
        """foo/package.mill:10:22: not found: type UnknownAfterFooModule"""
      ))
      assert(res.err.contains("""object after extends UnknownAfterFooModule"""))
    }
  }
}
