package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum
import scala.reflect.macros.blackbox.*

object EnumeratumMacros {

  def `enum`[E <: enumeratum.EnumEntry: c.WeakTypeTag](c: Context): c.Expr[Enum[E]] = {
    import c.universe.*

    val E = weakTypeOf[E].dealias
    val enumExpr = c.Expr[enumeratum.Enum[E]](
      c.inferImplicitValue(c.weakTypeOf[enumeratum.Enum[E]], silent = false, withMacrosDisabled = false)
    )

    c.Expr[Enum[E]](
      q"""
      new io.scalaland.enumz.Enum[$E] {
        lazy val values: Vector[$E] = $enumExpr.values.toVector
        def getName(`enum`: $E): String = `enum`.entryName
      }
      """
    )
  }
}
