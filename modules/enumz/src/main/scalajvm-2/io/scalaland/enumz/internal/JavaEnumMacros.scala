package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum
import scala.reflect.macros.blackbox.*

private[enumz] object JavaEnumMacros {

  def `enum`[E <: java.lang.Enum[E]: c.WeakTypeTag](c: Context): c.Expr[Enum[E]] = {
    import c.universe.*
    val valueType = implicitly[c.WeakTypeTag[E]].tpe.dealias
    val objectName = valueType.typeSymbol.companion.name.toTermName
    c.Expr[Enum[E]](
      q"""
      new io.scalaland.enumz.Enum[$valueType] {
        lazy val values: Vector[$valueType] = $objectName.values.toVector
        def getName(enum: $valueType): String = enum.name
      }
      """
    )
  }
}
