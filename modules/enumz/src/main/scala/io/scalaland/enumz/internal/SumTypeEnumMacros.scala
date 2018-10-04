package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum
import scala.reflect.macros.blackbox._

object SumTypeEnumMacros {

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  def enum[E: c.WeakTypeTag](c: Context): c.Expr[Enum[E]] = {
    import c.universe.{ Symbol => CSymbol, _ }

    type ISymbol = scala.reflect.internal.Symbols#Symbol
    val order = Ordering.fromLessThan[Position]((a, b) => a.line < b.line || (a.line == b.line && a.column < b.column))
    val symbol:          ClassSymbol      = weakTypeOf[E].typeSymbol.asClass
    val children:        List[CSymbol]    = symbol.knownDirectSubclasses.toList.sortBy(_.pos)(order)
    val sourceModuleRef: CSymbol => Ident = sym => Ident(sym.asInstanceOf[ISymbol].sourceModule.asInstanceOf[CSymbol])

    if (!symbol.isClass || !symbol.isSealed) {
      c.abort(c.enclosingPosition, "Can only enumerate values of an enum which is implemented as sealed trait/class.")
    } else if (!children.forall(_.isModuleClass)) {
      c.abort(c.enclosingPosition, "All children must be (case) objects.")
    } else {
      val valueType = implicitly[c.WeakTypeTag[E]].tpe.dealias
      val values =
        c.Expr[Vector[E]](Apply(Select(reify(Vector).tree, TermName("apply")), children.map(sourceModuleRef)))
      c.Expr[Enum[E]](
        q"""new io.scalaland.enumz.Enum[$valueType] {
            lazy val values: Vector[$valueType] = $values
            def getName(enum: $valueType): String = enum.toString
          }"""
      )
    }
  }
}
