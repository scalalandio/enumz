package io.scalaland.enumz

import scala.language.experimental.macros

private[enumz] trait EnumerationImplicits {

  implicit def enumerationEnum[E <: Enumeration#Value]: Enum[E] = macro internal.EnumerationMacros.`enum`[E]
}
