package io.scalaland.enumz

sealed trait TestSumType extends Product with Serializable
object TestSumType {
  case object A extends TestSumType
  case object B extends TestSumType
  case object C extends TestSumType
}
