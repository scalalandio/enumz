package io.scalaland.enumz

import scala.language.experimental.macros

private[enumz] trait EnumeratumImplicits {

  implicit def enumeratumEnum[E <: enumeratum.EnumEntry]: Enum[E] = macro internal.EnumeratumMacros.`enum`[E]
}
