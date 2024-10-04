package io.scalaland.enumz

private[enumz] trait EnumerationImplicits {

  inline given enumerationEnum[E <: Enumeration#Value]: Enum[E] = ${ internal.EnumerationMacros.`enum`[E] }
}
