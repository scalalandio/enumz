package io.scalaland.enumz

class NoEnumSpec extends munit.FunSuite {

  test("Enum should not derive a type class for non-enum type") {
    assert(
      compileErrors("""Enum[NotEnum.type]""").contains(
        """Can only enumerate values of an enum which is implemented as sealed trait/class"""
      )
    )
  }
}
