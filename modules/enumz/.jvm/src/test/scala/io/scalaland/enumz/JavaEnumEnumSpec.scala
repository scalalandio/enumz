package io.scalaland.enumz

import org.specs2.mutable.Specification

class JavaEnumEnumSpec extends Specification {

  "Enum" should {

    "derive instance for Java enum" in {
      val enum = Enum[TestJavaEnum]

      enum.values must_== Vector(TestJavaEnum.A, TestJavaEnum.B, TestJavaEnum.C)
      enum.values.map(enum.getName) must_== Vector("A", "B", "C")
      enum.values.map(enum.getIndex) must_== Vector(0, 1, 2)
    }
  }
}
