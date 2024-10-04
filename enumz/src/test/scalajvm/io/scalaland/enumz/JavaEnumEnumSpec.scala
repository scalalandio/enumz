package io.scalaland.enumz

class JavaEnumEnumSpec extends munit.FunSuite {

  test("Enum should derive instance for Java enum") {
    val `enum` = Enum[TestJavaEnum]

    assertEquals(`enum`.values, Vector(TestJavaEnum.A, TestJavaEnum.B, TestJavaEnum.C))
    assertEquals(`enum`.values.map(`enum`.getName), Vector("A", "B", "C"))
    assertEquals(`enum`.values.map(`enum`.getIndex), Vector(0, 1, 2))
  }
}
