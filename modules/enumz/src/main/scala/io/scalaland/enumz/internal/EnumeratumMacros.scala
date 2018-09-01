package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum
import scala.reflect.macros.blackbox._

object EnumeratumMacros {

  def enum[E <: enumeratum.EnumEntry: c.WeakTypeTag](c: Context): c.Expr[Enum[E]] = {
    import c.universe._
    val valueType = implicitly[c.WeakTypeTag[E]].tpe.dealias
    val objectName = valueType.typeSymbol.companion.name.toTermName
    println(objectName)
    c.Expr[Enum[E]](
      q"""new io.scalaland.enumz.Enum[$valueType] {
            lazy val values: Vector[$valueType] = $objectName.values.toVector
            def getName(enum: $valueType): String = enum.entryName
          }"""
    )
  }
}
