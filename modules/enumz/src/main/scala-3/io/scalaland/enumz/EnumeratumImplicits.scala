package io.scalaland.enumz

trait EnumeratumImplicits {

  inline given enumeratumEnum[E <: enumeratum.EnumEntry]: Enum[E] = ${ internal.EnumeratumMacros.`enum`[E] }
}
