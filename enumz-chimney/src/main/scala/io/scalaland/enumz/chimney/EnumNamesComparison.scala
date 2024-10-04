package io.scalaland.enumz.chimney

trait EnumNamesComparison {

  def namesMatch(fromName: String, toName: String): Boolean
}
object EnumNamesComparison {

  implicit val strictEquality: EnumNamesComparison = (fromName: String, toName: String) => fromName == toName
}
