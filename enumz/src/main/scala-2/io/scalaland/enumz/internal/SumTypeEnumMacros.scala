package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum
import scala.reflect.macros.blackbox.*

object SumTypeEnumMacros {

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  def `enum`[E: c.WeakTypeTag](c: Context): c.Expr[Enum[E]] = {
    import c.universe.{Symbol as CSymbol, *}

    type ISymbol = scala.reflect.internal.Symbols#Symbol
    val symbol: ClassSymbol = weakTypeOf[E].typeSymbol.asClass
    if (!symbol.isClass || !symbol.isSealed) {
      c.abort(c.enclosingPosition, "Can only enumerate values of an enum which is implemented as sealed trait/class.")
    }

    val order = Ordering.fromLessThan[Position]((a, b) => a.line < b.line || (a.line == b.line && a.column < b.column))
    val children: List[CSymbol] = symbol.knownDirectSubclasses.toList.sortBy(_.pos)(order)
    if (!children.forall(_.isModuleClass)) {
      c.abort(c.enclosingPosition, "All children must be (case) objects.")
    }

    val sourceModuleRef: CSymbol => Ident = sym => Ident(sym.asInstanceOf[ISymbol].sourceModule.asInstanceOf[CSymbol])

    val E = weakTypeOf[E].dealias
    val valuesExpr =
      c.Expr[Vector[E]](Apply(Select(reify(Vector).tree, TermName("apply")), children.map(sourceModuleRef)))
    c.Expr[Enum[E]](
      q"""
      new io.scalaland.enumz.Enum[$E] {
        lazy val values: Vector[$E] = $valuesExpr
        def getName(`enum`: $E): String = `enum`.toString
      }
      """
    )
  }
}
