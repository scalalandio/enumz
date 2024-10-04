package io.scalaland.enumz

import io.scalaland.chimney.{partial, PartialTransformer}

package object chimney {

  implicit def enumPartialTransformer[From: Enum, To: Enum](implicit
      namesComparison: EnumNamesComparison
  ): PartialTransformer[From, To] = (src: From, _: Boolean) => {
    val fromName = Enum[From].getName(src)
    Enum[To].values.filter(to => namesComparison.namesMatch(fromName, Enum[To].getName(to))) match {
      case Vector(value) => partial.Result.fromValue(value)
      case Vector()      => partial.Result.fromEmpty
      case values =>
        partial.Result.fromErrorString(
          s"Multiple enum values matched for `$fromName` name: ${values.map(Enum[To].getName).mkString(", ")}"
        )
    }
  }
}
