package io.scalaland.enumz

trait SumTypeEnumImplicits {

  inline given sumTypeEnum[E]: Enum[E] = ${ internal.SumTypeEnumMacros.`enum`[E] }
}
