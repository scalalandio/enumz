package io.scalaland.enumz

import scala.language.experimental.macros

trait JavaEnumImplicits {

  implicit def javaEnumEnum[E <: java.lang.Enum[E]]: Enum[E] = macro internal.JavaEnumMacros.`enum`[E]
}
