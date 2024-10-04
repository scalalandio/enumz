package io.scalaland.enumz.internal

import io.scalaland.enumz.Enum
import scala.quoted.*

object SumTypeEnumMacros {

  def `enum`[E: Type](using q: Quotes): Expr[Enum[E]] = {
    import q.*, q.reflect.*

    val E = TypeRepr.of[E]

    if !E.typeSymbol.flags.is(Flags.Sealed) then {
      report.errorAndAbort("Can only enumerate values of an enum which is implemented as sealed trait/class.")
    }

    def extractRecursively(sym: Symbol): List[Symbol] =
      if sym.flags.is(Flags.Sealed) then sym.children.flatMap(extractRecursively)
      else if sym.flags.is(Flags.Enum) || sym.flags.is(Flags.Module) then List(sym)
      else report.errorAndAbort("All children must be (case) objects.")

    given Ordering[Option[Position]] = Ordering.Option(Ordering.fromLessThan[Position] { (a, b) =>
      a.startLine < b.startLine || (a.startLine == b.startLine && a.startColumn < b.startColumn)
    })

    val valuesExpr: Expr[Vector[E]] = '{
      Vector(${
        Varargs(
          extractRecursively(E.typeSymbol).distinct
            .sortBy(_.pos.filter(pos => scala.util.Try(pos.start).isSuccess))
            .map { sym =>
              if sym.flags.is(Flags.Module) then Ref(sym.companionModule).asExprOf[E]
              else Ref(sym).asExprOf[E]
            }
        )
      }*)
    }

    '{
      import scala.language.dynamics
      new Enum[E] {
        lazy val values: Vector[E] = ${ valuesExpr }
        def getName(`enum`: E): String = `enum`.toString
      }
    }
  }
}
