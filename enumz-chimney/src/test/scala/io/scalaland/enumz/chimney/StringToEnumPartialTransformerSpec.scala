package io.scalaland.enumz.chimney

import io.scalaland.chimney.dsl.*

class StringToEnumPartialTransformerSpec extends munit.FunSuite {

  test("Convert from String to enum") {
    assertEquals(
      List("A", "B", "C").transformIntoPartial[Vector[TestEnumeratum]].asOption,
      Some(Vector(TestEnumeratum.A, TestEnumeratum.B, TestEnumeratum.C))
    )

    assertEquals(
      List("A", "B", "C").transformIntoPartial[Vector[TestEnumeration.TestEnumeration]].asOption,
      Some(Vector(TestEnumeration.A, TestEnumeration.B, TestEnumeration.C))
    )

    assertEquals(
      List("A", "B", "C").transformIntoPartial[Vector[TestSumType]].asOption,
      Some(Vector(TestSumType.A, TestSumType.B, TestSumType.C))
    )
  }
}
