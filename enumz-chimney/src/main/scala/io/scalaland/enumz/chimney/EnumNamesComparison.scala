package io.scalaland.enumz.chimney

trait EnumNamesComparison {

  def namesMatch(fromName: String, toName: String): Boolean
}
object EnumNamesComparison {

  implicit val default: EnumNamesComparison = Implicits.strictEquality

  object Implicits {

    implicit val strictEquality: EnumNamesComparison = (fromName: String, toName: String) => fromName == toName

    implicit val caseInsensitiveEquality: EnumNamesComparison = (fromName: String, toName: String) =>
      fromName.equalsIgnoreCase(toName)
  }
}
