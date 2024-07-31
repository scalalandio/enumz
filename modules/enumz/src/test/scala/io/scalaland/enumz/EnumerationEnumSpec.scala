package io.scalaland.enumz

class EnumerationEnumSpec extends munit.FunSuite {

  test("Enum should derive instance for scala.Enumeration enum") {
    val `enum` = Enum[TestEnumeration.TestEnumeration]

    assertEquals(`enum`.values, Vector(TestEnumeration.A, TestEnumeration.B, TestEnumeration.C))
    assertEquals(`enum`.values.map(`enum`.getName), Vector("A", "B", "C"))
    assertEquals(`enum`.values.map(`enum`.getIndex), Vector(0, 1, 2))
  }
}
