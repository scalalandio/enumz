package io.scalaland.enumz

trait Enum[E] {

  def values: Vector[E]
  lazy val indices: Map[E, Int] = values.zipWithIndex.toMap

  def getName(enum: E): String
  def getIndex(enum: E): Int = indices(enum)

  def withIndexOption(index: Int): Option[E] = values.lift(index)
  def withIndex(name: String): E = withNameOption(name).get

  def withNameOption(name: String): Option[E] = values.find(getName(_) == name)
  def withName(name: String): E = withNameOption(name).get

  def withNameInsensitiveOption(name: String): Option[E] = values.find(getName(_) equalsIgnoreCase name)
  def withNameInsensitive(name: String): Option[E] = withNameInsensitiveOption(name)
}

object Enum extends JavaEnumImplicits with  EnumerationImplicits with EnumeratumImplicits {

  @inline def apply[E](implicit enum: Enum[E]): Enum[E] = enum
}
