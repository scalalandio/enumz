package io.scalaland.enumz.chimney

import enumeratum.{Enum as EEnum, *}

sealed trait TestEnumeratum extends EnumEntry
object TestEnumeratum extends EEnum[TestEnumeratum] {
  val values = findValues
  case object A extends TestEnumeratum
  case object B extends TestEnumeratum
  case object C extends TestEnumeratum
}
