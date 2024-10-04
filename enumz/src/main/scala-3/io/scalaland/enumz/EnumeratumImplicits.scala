package io.scalaland.enumz

private[enumz] trait EnumeratumImplicits {

  inline given enumeratumEnum[E <: enumeratum.EnumEntry]: Enum[E] = ${ internal.EnumeratumMacros.`enum`[E] }
}
