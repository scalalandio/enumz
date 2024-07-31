package io.scalaland.enumz

import org.specs2.mutable.Specification
import org.specs2.execute.Typecheck.*
import org.specs2.matcher.TypecheckMatchers.*

class NoEnumSpec extends Specification {

  final case object NotEnum

  "Enum" should {

    "not derive a type class for non-enum type" in {
      tc"""Enum[NotEnum]""" must not(succeed)
    }
  }
}
