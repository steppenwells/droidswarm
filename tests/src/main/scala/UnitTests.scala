package com.steppenwells.droidswarm.tests

import junit.framework.Assert._
import _root_.android.test.AndroidTestCase

class UnitTests extends AndroidTestCase {
  def testPackageIsCorrect {
    assertEquals("com.steppenwells.droidswarm", getContext.getPackageName)
  }
}