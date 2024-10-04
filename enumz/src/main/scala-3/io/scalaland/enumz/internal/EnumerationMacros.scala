package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum

import scala.quoted.*

private[enumz] object EnumerationMacros {

  def `enum`[E <: Enumeration#Value: Type](using q: Quotes): Expr[Enum[E]] = {
    import q.*
    import q.reflect.*

    val E = TypeRepr.of[E]

    val valsetExpr: Expr[Set[E]] = E match {
      case TypeRef(outer, "Value") =>
        Ident(outer.typeSymbol.companionModule.termRef) // E =:= Something.Value -> with .Value dropped
          .select(outer.typeSymbol.methodMember("values").head) // .values
          .asExprOf[Set[E]]
      case _ => report.errorAndAbort(s"Type ${E.show} is not scala.Enumeration")
    }
    val orderingExpr: Expr[Ordering[E]] = Expr.summon[Ordering[E]].getOrElse {
      report.errorAndAbort(s"Could not find Ordering[${E.show}] instance")
    }

    '{
      import scala.language.dynamics
      new Enum[E] {
        lazy val values: Vector[E] = ${ valsetExpr }.toVector.sorted(${ orderingExpr })
        def getName(`enum`: E): String = `enum`.toString
      }
    }
  }
}
