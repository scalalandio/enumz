package io.scalaland.enumz

import scala.language.dynamics

trait Enum[E] extends Dynamic {

  def values: Vector[E]
  lazy val indices: Map[E, Int] = values.zipWithIndex.toMap

  def getName(enum:  E): String
  def getIndex(enum: E): Int = indices(enum)

  def withIndexOption(index: Int): Option[E] = values.lift(index)
  def withIndex(index:       Int): E         = withIndexOption(index).get

  def withNameOption(name: String): Option[E] = values.find(getName(_) == name)
  def withName(name:       String): E         = withNameOption(name).get

  def withNameInsensitiveOption(name: String): Option[E] = values.find(getName(_) equalsIgnoreCase name)
  def withNameInsensitive(name:       String): E = withNameInsensitiveOption(name).get

  def selectDynamic(name: String): E = withName(name)
}

object Enum extends Implicits {

  @inline def apply[E](implicit enum: Enum[E]): Enum[E] = enum
}

trait Implicits extends JavaEnumImplicits with EnumerationImplicits with EnumeratumImplicits with LowPriorityImplicits

trait LowPriorityImplicits extends SumTypeEnumImplicits
