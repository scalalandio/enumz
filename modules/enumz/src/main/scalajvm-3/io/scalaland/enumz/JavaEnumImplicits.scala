package io.scalaland.enumz

trait JavaEnumImplicits {

  implicit def javaEnumEnum[E <: java.lang.Enum[E]]: Enum[E] = ??? // macro internal.JavaEnumMacros.`enum`[E]
}
