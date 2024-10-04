package io.scalaland.enumz

import scala.util.Try

class EnumSpec extends munit.FunSuite {

  test("Enum[E].values should return all values in order") {
    def containsAllValues[E: Enum](es: E*): Unit =
      assertEquals(Enum[E].values, es.toVector)

    containsAllValues[TestEnumeratum](TestEnumeratum.A, TestEnumeratum.B, TestEnumeratum.C)
    containsAllValues[TestEnumeration.TestEnumeration](TestEnumeration.A, TestEnumeration.B, TestEnumeration.C)
    containsAllValues[TestSumType](TestSumType.A, TestSumType.B, TestSumType.C)
  }

  test("Enum[E].getName(enum) should return right name") {
    def returnsRightNames[E: Enum]: Unit =
      assertEquals(Enum[E].values.map(Enum[E].getName), Vector("A", "B", "C"))

    returnsRightNames[TestEnumeratum]
    returnsRightNames[TestEnumeration.TestEnumeration]
    returnsRightNames[TestSumType]
  }

  test("Enum[E].getIndex(enum) should return right index") {
    def returnsRightIndices[E: Enum]: Unit =
      assertEquals(Enum[E].values.map(Enum[E].getIndex), Vector(0, 1, 2))

    returnsRightIndices[TestEnumeratum]
    returnsRightIndices[TestEnumeration.TestEnumeration]
    returnsRightIndices[TestSumType]
  }

  test("Enum[E].withIndexOption(index) should return Some enum for right index") {
    def returnsEnums[E: Enum]: Unit =
      assertEquals(Vector(0, 1, 2).map(Enum[E].withIndexOption), Enum[E].values.map(Some(_)))

    returnsEnums[TestEnumeratum]
    returnsEnums[TestEnumeration.TestEnumeration]
    returnsEnums[TestSumType]
  }

  test("return None for invalid index") {
    def returnsNone[E: Enum]: Unit =
      assertEquals(Vector(-1, 4).map(Enum[E].withIndexOption), Vector(None, None))

    returnsNone[TestEnumeratum]
    returnsNone[TestEnumeration.TestEnumeration]
    returnsNone[TestSumType]
  }

  test("Enum[E].withIndex(index) should return enum for right index") {
    def returnsEnums[E: Enum]: Unit =
      assertEquals(Vector(0, 1, 2).map(Enum[E].withIndex), Enum[E].values)

    returnsEnums[TestEnumeratum]
    returnsEnums[TestEnumeration.TestEnumeration]
    returnsEnums[TestSumType]
  }

  test("Enum[E].withIndex(index) should throw for invalid index") {
    def throws[E: Enum]: Unit =
      assertEquals(Vector(-1, 4).flatMap(i => Try(Enum[E].withIndex(i)).toOption.toVector), Vector())

    throws[TestEnumeratum]
    throws[TestEnumeration.TestEnumeration]
    throws[TestSumType]
  }

  test("Enum[E].withNameOption(name) should return Some enum for right index") {
    def returnsEnums[E: Enum]: Unit =
      assertEquals(Vector("A", "B", "C").map(Enum[E].withNameOption), Enum[E].values.map(Some(_)))

    returnsEnums[TestEnumeratum]
    returnsEnums[TestEnumeration.TestEnumeration]
    returnsEnums[TestSumType]
  }

  test("Enum[E].withNameOption(name) should return None for invalid index") {
    def returnsNone[E: Enum]: Unit =
      assertEquals(Vector("", "D").map(Enum[E].withNameOption), Vector(None, None))

    returnsNone[TestEnumeratum]
    returnsNone[TestEnumeration.TestEnumeration]
    returnsNone[TestSumType]
  }

  test("Enum[E].withName(name) should return enum for right index") {
    def returnsEnums[E: Enum]: Unit =
      assertEquals(Vector("A", "B", "C").map(Enum[E].withName), Enum[E].values)

    returnsEnums[TestEnumeratum]
    returnsEnums[TestEnumeration.TestEnumeration]
    returnsEnums[TestSumType]
  }

  test("Enum[E].withName(name) should throw for invalid index") {
    def throws[E: Enum]: Unit =
      assertEquals(Vector("", "D").flatMap(i => Try(Enum[E].withName(i)).toOption.toVector), Vector())

    throws[TestEnumeratum]
    throws[TestEnumeration.TestEnumeration]
    throws[TestSumType]
  }

  test("Enum[E].withNameInsensitiveOption(name) should return Some enum for right index") {
    def returnsEnums[E: Enum]: Unit =
      assertEquals(Vector("a", "b", "c").map(Enum[E].withNameInsensitiveOption), Enum[E].values.map(Some(_)))

    returnsEnums[TestEnumeratum]
    returnsEnums[TestEnumeration.TestEnumeration]
    returnsEnums[TestSumType]
  }

  test("Enum[E].withNameInsensitiveOption(name) should return None for invalid index") {
    def returnsNone[E: Enum]: Unit =
      assertEquals(Vector("", "d").map(Enum[E].withNameInsensitiveOption), Vector(None, None))

    returnsNone[TestEnumeratum]
    returnsNone[TestEnumeration.TestEnumeration]
    returnsNone[TestSumType]
  }

  test("Enum[E].withNameInsensitive(name) should return enum for right index") {
    def returnsEnums[E: Enum]: Unit =
      assertEquals(Vector("a", "b", "c").map(Enum[E].withNameInsensitive), Enum[E].values)

    returnsEnums[TestEnumeratum]
    returnsEnums[TestEnumeration.TestEnumeration]
    returnsEnums[TestSumType]
  }

  test("Enum[E].withNameInsensitive(name) should throws for invalid index") {
    def throws[E: Enum]: Unit =
      assertEquals(Vector("", "d").flatMap(i => Try(Enum[E].withNameInsensitive(i)).toOption.toVector), Vector())

    throws[TestEnumeratum]
    throws[TestEnumeration.TestEnumeration]
    throws[TestSumType]
  }

  test("Enum[E].`name` should return enum for the name") {
    def returnsEnums[E: Enum]: Unit =
      assertEquals(Vector(Enum[E].`A`, Enum[E].`B`, Enum[E].`C`), Enum[E].values)

    returnsEnums[TestEnumeratum]
    returnsEnums[TestEnumeration.TestEnumeration]
    returnsEnums[TestSumType]
  }

  test("Enum[E].`name` should throw for invalid index") {
    def throws[E: Enum]: Unit =
      assertEquals(Vector(Try(Enum[E].` `).toOption.toVector, Try(Enum[E].`D`).toOption.toVector).flatten, Vector())

    throws[TestEnumeratum]
    throws[TestEnumeration.TestEnumeration]
    throws[TestSumType]
  }
}
