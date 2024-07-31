package io.scalaland.enumz

trait EnumerationImplicits {

  inline given enumerationEnum[E <: Enumeration#Value]: Enum[E] = ${ internal.EnumerationMacros.`enum`[E] }
}
