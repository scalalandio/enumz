package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum
import scala.reflect.macros.blackbox.*

private[enumz] object EnumerationMacros {

  def `enum`[E <: Enumeration#Value: c.WeakTypeTag](c: Context): c.Expr[Enum[E]] = {
    import c.universe.*
    val valueType = weakTypeOf[E].dealias
    val objectStr = valueType.toString.replaceFirst(".Value$", "")
    val objectName = c.typecheck(c.parse(s"$objectStr: $objectStr.type"))
    c.Expr[Enum[E]](
      q"""new io.scalaland.enumz.Enum[$valueType] {
            lazy val values: Vector[$valueType] = ($objectName).values.toVector.sorted
            def getName(enum: $valueType): String = enum.toString
          }"""
    )
  }
}
