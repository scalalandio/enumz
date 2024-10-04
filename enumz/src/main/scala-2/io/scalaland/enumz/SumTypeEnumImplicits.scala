package io.scalaland.enumz

import scala.language.experimental.macros

private[enumz] trait SumTypeEnumImplicits {

  implicit def sumTypeEnum[E]: Enum[E] = macro internal.SumTypeEnumMacros.`enum`[E]
}
