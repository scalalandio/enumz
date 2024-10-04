package io.scalaland.enumz

class SumTypeEnumSpec extends munit.FunSuite {

  test("Enum should derive instance for scala.Enumeration enum") {
    val `enum` = Enum[TestSumType]

    assertEquals(`enum`.values, Vector(TestSumType.A, TestSumType.B, TestSumType.C))
    assertEquals(`enum`.values.map(`enum`.getName), Vector("A", "B", "C"))
    assertEquals(`enum`.values.map(`enum`.getIndex), Vector(0, 1, 2))
  }
}
