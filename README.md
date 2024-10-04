# enumz

[![Chimney Scala version support](https://index.scala-lang.org/scalalandio/enumz/enumz/latest.svg)](https://index.scala-lang.org/scalalandio/enumz/enumz)

[![CI build](https://github.com/scalalandio/enumz/actions/workflows/ci.yml/badge.svg?branch=master)](https://github.com/scalalandio/enumz/actions)
[![License](http://img.shields.io/:license-Apache%202-green.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)

[![Documentation Status](https://readthedocs.org/projects/enumz/badge/?version=latest)](https://readthedocs.org/projects/enumz/builds/)
[![Scaladoc 2.11](https://javadoc.io/badge2/io.scalaland/enumz_2.11/scaladoc%202.11.svg)](https://javadoc.io/doc/io.scalaland/enumz_2.11)
[![Scaladoc 2.12](https://javadoc.io/badge2/io.scalaland/enumz_2.12/scaladoc%202.12.svg)](https://javadoc.io/doc/io.scalaland/enumz_2.12)
[![Scaladoc 2.13](https://javadoc.io/badge2/io.scalaland/enumz_2.13/scaladoc%202.13.svg)](https://javadoc.io/doc/io.scalaland/enumz_2.13)
[![Scaladoc 3](https://javadoc.io/badge2/io.scalaland/enumz_3/scaladoc%203.svg)](https://javadoc.io/doc/io.scalaland/enumz_3)

One enum type class to rule them all.

In Scala, you might meet many different implementations of enums:

 * build-in `scala.Enumeration`,
 * sum-type based sealed hierarchies,
 * `enumeratum` as the previous one on steroids,
 * Java's `enum` type which use static methods and has no companion object.

You are in control of what implementation *you* pick, but you have no control over
what *other people* use. So if you had to use APIs using many different
implementations how would you handle common code?

With a type class.

## Usage

Add to your sbt (2.12, 2.13 and 3.3+ supported)

```scala
libraryDependencies += "io.scalaland" %% "enumz" % enumzVersion // see Maven badge
```

or, if you use Scala.js

```scala
libraryDependencies += "io.scalaland" %%% "enumz" % enumzVersion // see Maven badge
```

From now on you can define enums whatever you want and use one common interface
for all of them.

```java
public enum TestJavaEnum {
    A, B, C
}
```

```scala
object TestEnumeration extends Enumeration {
  type TestEnumeration = Value
  val A, B, C = Value
}
```

```scala
sealed trait TestSumType extends Product with Serializable
object TestSumType {
  case object A extends TestSumType
  case object B extends TestSumType
  case object C extends TestSumType
}
```

```scala
import enumeratum.{Enum => EnumeratumEnum, _}

sealed trait TestEnumeratum extends EnumEntry
object TestEnumeratum extends EnumeratumEnum[TestEnumeratum] {
  val values = findValues
  case object A extends TestEnumeratum
  case object B extends TestEnumeratum
  case object C extends TestEnumeratum
}
```

```scala
import io.scalaland.enumz.Enum

Enum[TestJavaEnum].values
Enum[TestEnumeration.TestEnumeration].values
Enum[TestSumType].values
Enum[TestEnumeratum].values
```

You can also test it with ammonite like:

```scala
import $ivy.`io.scalaland::enumz:1.0.0`, io.scalaland.enumz.Enum

{
sealed trait TestSumType extends Product with Serializable
object TestSumType {
  case object A extends TestSumType
  case object B extends TestSumType
  case object C extends TestSumType
}
}

Enum[TestSumType].values
```

## Methods

```scala
Enum[TestSumType].values // Vector(TestSumType.A, TestSumType.B, TestSumType.C)
Enum[TestSumType].indices // Map(TestSumType.A -> 0, TestSumType.B -> 1, TestSumType.C -> 2)

Enum[TestSumType].getName(TestSumType.A) // "A"
Enum[TestSumType].getIndex(TestSumType.A) // 0

Enum[TestSumType].withIndexOption(0) // Some(TestSumType.A)
Enum[TestSumType].withIndexOption(-1) // None

Enum[TestSumType].withIndex(0) // TestSumType.A
Enum[TestSumType].withIndex(-1) // throws!

Enum[TestSumType].withNameOption("A") // Some(TestSumType.A)
Enum[TestSumType].withNameOption("") // None

Enum[TestSumType].withName("A") // TestSumType.A
Enum[TestSumType].withName("") // throws!

Enum[TestSumType].withNameInsensitiveOption("a") // Some(TestSumType.A)
Enum[TestSumType].withNameInsensitiveOption("") // None

Enum[TestSumType].withNameInsensitive("a") // TestSumType.A
Enum[TestSumType].withNameInsensitive("") // throws!

Enum[TestSumType].`A` // TestSumType.A
```
