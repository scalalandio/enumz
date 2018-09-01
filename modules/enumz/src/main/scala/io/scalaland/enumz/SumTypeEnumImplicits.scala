package io.scalaland.enumz

import scala.language.experimental.macros

trait SumTypeEnumImplicits {

  implicit def sumTypeEnum[E]: Enum[E] = macro internal.SumTypeEnumMacros.enum[E]
}
