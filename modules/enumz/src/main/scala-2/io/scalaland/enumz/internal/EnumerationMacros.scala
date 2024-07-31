package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum
import scala.reflect.macros.blackbox.*

private[enumz] object EnumerationMacros {

  def `enum`[E <: Enumeration#Value: c.WeakTypeTag](c: Context): c.Expr[Enum[E]] = {
    import c.universe.*
    val E = weakTypeOf[E].dealias
    val objectStr = E.toString.replaceFirst(".Value$", "")
    val valsetExpr = c.Expr[Set[E]](c.typecheck(c.parse(s"($objectStr: $objectStr.type).values")))
    c.Expr[Enum[E]](
      q"""
      new io.scalaland.enumz.Enum[$E] {
        lazy val values: Vector[$E] = $valsetExpr.toVector.sorted
        def getName(`enum`: $E): String = `enum`.toString
      }
      """
    )
  }
}
