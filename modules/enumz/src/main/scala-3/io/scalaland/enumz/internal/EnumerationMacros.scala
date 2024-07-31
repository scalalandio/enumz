package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum
import scala.quoted.*

private[enumz] object EnumerationMacros {

  def `enum`[E <: Enumeration#Value: Type](using q: Quotes): Expr[Enum[E]] = {
    import q.*, q.reflect.*
    
    val E = TypeRepr.of[E]
    println(E.show)
    println(E.typeSymbol)
    println(E.typeSymbol.owner)
    println(E.typeSymbol.owner.fullName)
    println(E.typeSymbol.owner.declaredMethods)
    println("--------")
    
    val valsetExpr: Expr[Set[E]] = '{ ??? } // TODO
    val orderingExpr: Expr[Ordering[E]] = '{ ??? } // TODO
    
    '{
      import scala.language.dynamics
      new Enum[E] {
        lazy val values: Vector[E] = ${ valsetExpr }.toVector.sorted(${ orderingExpr })
        def getName(`enum`: E): String = `enum`.toString
      }
    }
  }
}
