package io.scalaland.enumz.chimney

import io.scalaland.chimney.dsl.*
import io.scalaland.enumz.Enum

class EnumPartialTransformerSpec extends munit.FunSuite {

  test("Convert between enums") {

    assertEquals(
      Enum[TestEnumeration.TestEnumeration].values.transformIntoPartial[Vector[TestEnumeratum]].asOption,
      Some(Enum[TestEnumeratum].values)
    )

    assertEquals(
      Enum[TestEnumeratum].values.transformIntoPartial[Vector[TestSumType]].asOption,
      Some(Enum[TestSumType].values)
    )

    assertEquals(
      Enum[TestSumType].values.transformIntoPartial[Vector[TestEnumeration.TestEnumeration]].asOption,
      Some(Enum[TestEnumeration.TestEnumeration].values)
    )
  }
}
