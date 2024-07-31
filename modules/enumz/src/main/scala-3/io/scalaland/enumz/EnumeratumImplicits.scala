package io.scalaland.enumz

trait EnumeratumImplicits {

  implicit def enumeratumEnum[E <: enumeratum.EnumEntry]: Enum[E] = ???
}
