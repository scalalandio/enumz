package io.scalaland.enumz

import scala.language.experimental.macros

trait EnumerationImplicits {

  implicit def enumerationEnum[E <: Enumeration#Value]: Enum[E] = macro internal.EnumerationMacros.`enum`[E]
}
