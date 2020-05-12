package com.prateek.learn.xml.compare

import scala.collection.mutable.ListBuffer
import scala.xml.{Elem, MetaData, Node, Text}

object XmlCompare extends App {

  def apply(exp: Node, act: Node)(implicit lb: ListBuffer[String]): Boolean = {

    def join = {
      lb.toList.mkString(":")
    }

    (exp, act) match {
      case (e1: Elem, a1: Elem) =>
        lb.append(e1.label)
        val bool = labelMatch(e1, a1) &&
          attributesMatch(e1, a1) &&
          e1.child.forall(e1 =>
            a1.child.exists(a1 => {
              println(s"elem $join: expected $e1 \n actual $a1")
              apply(e1, a1)
            })
          )
        lb.remove(lb.size - 1)
        bool
      case (Text(t1), Text(t2)) =>
        println(s"text $join: expected $t1 \n actual $t2")
        t1.trim == t2.trim
      case _ => false
    }
  }

  def labelMatch(e1: Elem, e2: Elem) = {
    e1.label == e2.label &&
    e1.scope.getURI(e1.prefix) == e2.scope.getURI(e2.prefix)
  }

  /** Returns 'true' if the attributes in e1 are included in e2.  */
  private def attributesMatch(e1: Elem, e2: Elem): Boolean = {

    def contains(a: MetaData) = {
      val e2AttributeValue =
        if (a.isPrefixed)
          e2.attributes(a.getNamespace(e1), e2.scope, a.key)
        else
          e2.attributes(a.key)
      e2AttributeValue != null && e2AttributeValue == a.value
    }

    e1.attributes.filter(_.value != null).forall(contains)
  }
}
