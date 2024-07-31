package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum
import scala.reflect.macros.blackbox.*

object EnumeratumMacros {

  def `enum`[E <: enumeratum.EnumEntry: c.WeakTypeTag](c: Context): c.Expr[Enum[E]] = {
    import c.universe.*
    val valueType = weakTypeOf[E].dealias
    val objectName = valueType.typeSymbol.companion.name.toTermName
    c.Expr[Enum[E]](
      q"""new io.scalaland.enumz.Enum[$valueType] {
            lazy val values: Vector[$valueType] = $objectName.values.toVector
            def getName(enum: $valueType): String = enum.entryName
          }"""
    )
  }
}
