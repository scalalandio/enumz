package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum

import scala.quoted.*

object EnumeratumMacros {

  def `enum`[E <: enumeratum.EnumEntry: Type](using q: Quotes): Expr[Enum[E]] = {
    import q.*
    import q.reflect.*

    val E = TypeRepr.of[E]

    val enumExpr: Expr[enumeratum.Enum[E]] = Expr.summon[enumeratum.Enum[E]].getOrElse {
      report.errorAndAbort(s"Could not find enumeratum.Enum[${E.show}] instance")
    }

    '{
      import scala.language.dynamics
      new Enum[E] {
        lazy val values: Vector[E] = $enumExpr.values.toVector
        def getName(`enum`: E): String = `enum`.toString
      }
    }
  }
}
