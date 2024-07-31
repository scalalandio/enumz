package io.scalaland.enumz

class EnumeratumEnumSpec extends munit.FunSuite {

  test("Enum should derive instance for scala.Enumeration enum") {
    val `enum` = Enum[TestEnumeratum]

    assertEquals(`enum`.values, Vector(TestEnumeratum.A, TestEnumeratum.B, TestEnumeratum.C))
    assertEquals(`enum`.values.map(`enum`.getName), Vector("A", "B", "C"))
    assertEquals(`enum`.values.map(`enum`.getIndex), Vector(0, 1, 2))
  }
}
