package io.scalaland.enumz

class NoEnumSpec extends munit.FunSuite {

  test("Enum should not derive a type class for non-enum type") {
    assert(
      compileErrors("""Enum[NotEnum.type]""").nonEmpty
    )
  }
}
