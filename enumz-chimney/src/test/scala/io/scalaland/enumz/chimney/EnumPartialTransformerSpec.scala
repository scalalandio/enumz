package io.scalaland.enumz.chimney

import io.scalaland.chimney.dsl.*
import io.scalaland.enumz.Enum

class EnumPartialTransformerSpec extends munit.FunSuite {

  test("Convert between enums") {

    assertEquals(
      Enum[TestEnumeration.TestEnumeration].values.transformInto[Vector[TestEnumeratum]],
      Enum[TestEnumeratum].values
    )

    assertEquals(
      Enum[TestEnumeratum].values.transformInto[Vector[TestSumType]],
      Enum[TestSumType].values
    )

    assertEquals(
      Enum[TestSumType].values.transformInto[Vector[TestEnumeration.TestEnumeration]],
      Enum[TestEnumeration.TestEnumeration].values
    )
  }
}
