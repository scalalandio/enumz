package io.scalaland.enumz

private[enumz] trait SumTypeEnumImplicits {

  transparent inline given sumTypeEnum[E]: Enum[E] = ${ internal.SumTypeEnumMacros.`enum`[E] }
}
