package io.scalaland.enumz.chimney

import io.scalaland.chimney.dsl.*

class EnumToStringTransformerSpec extends munit.FunSuite {

  test("Convert from enum to String") {
    assertEquals(
      List[TestEnumeratum](TestEnumeratum.A, TestEnumeratum.B, TestEnumeratum.C).transformInto[Vector[String]],
      Vector("A", "B", "C")
    )

    assertEquals(
      List(TestEnumeration.A, TestEnumeration.B, TestEnumeration.C).transformInto[Vector[String]],
      Vector("A", "B", "C")
    )

    assertEquals(
      List(TestSumType.A, TestSumType.B, TestSumType.C).transformInto[Vector[String]],
      Vector("A", "B", "C")
    )
  }
}
