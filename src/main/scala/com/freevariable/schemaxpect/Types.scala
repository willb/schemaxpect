package com.freevariable.schemaxpect;

abstract class Width { def width: Int }

abstract class IntWidth extends Width {}
abstract class FPWidth extends Width {}

case object ByteW extends IntWidth { def width = 8 }
case object ShortW extends IntWidth { def width = 16 }

case object IntW extends IntWidth { def width = 32 }
case object LongW extends IntWidth { def width = 64 }
case object FloatW extends FPWidth { def width = 32 }
case object DoubleW extends FPWidth { def width = 64 }

abstract class TypeExpectation { 
  def widerThan(other: TypeExpectation) = TypeExpectation.widerThan(this, other) 
}

case class IntegralNum(w: Width) extends TypeExpectation {}
case class FloatingPointNum(w: FPWidth) extends TypeExpectation {}

case class Structure(elts: Map[String, TypeExpectation]) extends TypeExpectation{}

case class Array(elt: TypeExpectation) extends TypeExpectation {}

object TypeExpectation {
  def widerThan(expected: TypeExpectation, actual: TypeExpectation): Boolean = 
    (expected, actual) match {
      case (e1: TypeExpectation, e2: TypeExpectation) if e1 == e2 => true
      case (IntegralNum(w1), IntegralNum(w2)) if w1.width >= w2.width => true
      case (FloatingPointNum(w1), IntegralNum(w2)) if w1.width >= w2.width => true
      case (Array(e1), Array(e2)) => widerThan(e1, e2)
      case (Structure(es1), Structure(es2)) => {
	val es1Keys = es1.keySet
	val es2Keys = es2.keySet
	val hasKeys = (es1Keys intersect es2Keys) == es1Keys
	hasKeys && es1Keys.forall(k => widerThan(es1(k), es2(k)))
      }
      case _ => false
    }
}
