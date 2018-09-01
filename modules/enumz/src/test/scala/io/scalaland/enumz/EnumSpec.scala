package io.scalaland.enumz

import org.specs2.matcher.MatchResult
import org.specs2.mutable.Specification

import scala.util.Try

class EnumSpec extends Specification {

  "Enum[E].values" should {

    "return all values in order" in {
      def containsAllValues[E: Enum](es: E*): MatchResult[Vector[E]] =
        Enum[E].values must ===(es.toVector)

      containsAllValues[TestEnumeratum](TestEnumeratum.A, TestEnumeratum.B, TestEnumeratum.C)
      containsAllValues[TestEnumeration.TestEnumeration](TestEnumeration.A, TestEnumeration.B, TestEnumeration.C)
      containsAllValues[TestSumType](TestSumType.A, TestSumType.B, TestSumType.C)
    }
  }

  "Enum[E].getName(enum)" should {

    "return right name" in {
      def returnsRightNames[E: Enum]: MatchResult[Vector[String]] =
        Enum[E].values.map(Enum[E].getName) must ===(Vector("A", "B", "C"))

      returnsRightNames[TestEnumeratum]
      returnsRightNames[TestEnumeration.TestEnumeration]
      returnsRightNames[TestSumType]
    }
  }

  "Enum[E].getIndex(enum)" should {

    "return right index" in {
      def returnsRightIndices[E: Enum]: MatchResult[Vector[Int]] =
        Enum[E].values.map(Enum[E].getIndex) must ===(Vector(0, 1, 2))

      returnsRightIndices[TestEnumeratum]
      returnsRightIndices[TestEnumeration.TestEnumeration]
      returnsRightIndices[TestSumType]
    }
  }

  "Enum[E].withIndexOption(index)" should {

    "return Some enum for right index" in {
      def returnsEnums[E: Enum]: MatchResult[Vector[Option[E]]] =
        Vector(0, 1, 2).map(Enum[E].withIndexOption) must ===(Enum[E].values.map(Some(_)))

      returnsEnums[TestEnumeratum]
      returnsEnums[TestEnumeration.TestEnumeration]
      returnsEnums[TestSumType]
    }

    "return None for invalid index" in {
      def returnsNone[E: Enum]: MatchResult[Vector[Option[E]]] =
        Vector(-1, 4).map(Enum[E].withIndexOption) must ===(Vector(None, None))

      returnsNone[TestEnumeratum]
      returnsNone[TestEnumeration.TestEnumeration]
      returnsNone[TestSumType]
    }
  }

  "Enum[E].withIndex(index)" should {

    "return enum for right index" in {
      def returnsEnums[E: Enum]: MatchResult[Vector[E]] =
        Vector(0, 1, 2).map(Enum[E].withIndex) must ===(Enum[E].values)

      returnsEnums[TestEnumeratum]
      returnsEnums[TestEnumeration.TestEnumeration]
      returnsEnums[TestSumType]
    }

    "throw for invalid index" in {
      def throws[E: Enum]: MatchResult[Vector[E]] =
        Vector(-1, 4).flatMap(i => Try(Enum[E].withIndex(i)).toOption.toVector) must ===(Vector())

      throws[TestEnumeratum]
      throws[TestEnumeration.TestEnumeration]
      throws[TestSumType]
    }
  }

  "Enum[E].withNameOption(name)" should {

    "return Some enum for right index" in {
      def returnsEnums[E: Enum]: MatchResult[Vector[Option[E]]] =
        Vector("A", "B", "C").map(Enum[E].withNameOption) must ===(Enum[E].values.map(Some(_)))

      returnsEnums[TestEnumeratum]
      returnsEnums[TestEnumeration.TestEnumeration]
      returnsEnums[TestSumType]
    }

    "return None for invalid index" in {
      def returnsNone[E: Enum]: MatchResult[Vector[Option[E]]] =
        Vector("", "D").map(Enum[E].withNameOption) must ===(Vector(None, None))

      returnsNone[TestEnumeratum]
      returnsNone[TestEnumeration.TestEnumeration]
      returnsNone[TestSumType]
    }
  }

  "Enum[E].withName(name)" should {

    "return enum for right index" in {
      def returnsEnums[E: Enum]: MatchResult[Vector[E]] =
        Vector("A", "B", "C").map(Enum[E].withName) must ===(Enum[E].values)

      returnsEnums[TestEnumeratum]
      returnsEnums[TestEnumeration.TestEnumeration]
      returnsEnums[TestSumType]
    }

    "throw for invalid index" in {
      def throws[E: Enum]: MatchResult[Vector[E]] =
        Vector("", "D").flatMap(i => Try(Enum[E].withName(i)).toOption.toVector) must ===(Vector())

      throws[TestEnumeratum]
      throws[TestEnumeration.TestEnumeration]
      throws[TestSumType]
    }
  }

  "Enum[E].withNameInsensitiveOption(name)" should {

    "return Some enum for right index" in {
      def returnsEnums[E: Enum]: MatchResult[Vector[Option[E]]] =
        Vector("a", "b", "c").map(Enum[E].withNameInsensitiveOption) must ===(Enum[E].values.map(Some(_)))

      returnsEnums[TestEnumeratum]
      returnsEnums[TestEnumeration.TestEnumeration]
      returnsEnums[TestSumType]
    }

    "return None for invalid index" in {
      def returnsNone[E: Enum]: MatchResult[Vector[Option[E]]] =
        Vector("", "d").map(Enum[E].withNameInsensitiveOption) must ===(Vector(None, None))

      returnsNone[TestEnumeratum]
      returnsNone[TestEnumeration.TestEnumeration]
      returnsNone[TestSumType]
    }
  }

  "Enum[E].withNameInsensitive(name)" should {

    "return enum for right index" in {
      def returnsEnums[E: Enum]: MatchResult[Vector[E]] =
        Vector("a", "b", "c").map(Enum[E].withNameInsensitive) must ===(Enum[E].values)

      returnsEnums[TestEnumeratum]
      returnsEnums[TestEnumeration.TestEnumeration]
      returnsEnums[TestSumType]
    }

    "throws for invalid index" in {
      def throws[E: Enum]: MatchResult[Vector[E]] =
        Vector("", "d").flatMap(i => Try(Enum[E].withNameInsensitive(i)).toOption.toVector) must ===(Vector())

      throws[TestEnumeratum]
      throws[TestEnumeration.TestEnumeration]
      throws[TestSumType]
    }
  }

  "Enum[E].`name`" should {

    "return enum for the name" in {
      def returnsEnums[E: Enum]: MatchResult[Vector[E]] =
        Vector(Enum[E].`A`, Enum[E].`B`, Enum[E].`C`) must ===(Enum[E].values)

      returnsEnums[TestEnumeratum]
      returnsEnums[TestEnumeration.TestEnumeration]
      returnsEnums[TestSumType]
    }

    "throw for invalid index" in {
      def throws[E: Enum]: MatchResult[Vector[E]] =
        Vector(Try(Enum[E].` `).toOption.toVector, Try(Enum[E].`D`).toOption.toVector).flatten must ===(Vector())

      throws[TestEnumeratum]
      throws[TestEnumeration.TestEnumeration]
      throws[TestSumType]
    }
  }
}
