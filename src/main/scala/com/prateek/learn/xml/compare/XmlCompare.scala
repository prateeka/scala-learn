package com.prateek.learn.xml.compare

import scala.xml.{Elem, MetaData, Node, Text}

object XmlCompare extends App {

  def apply(exp: Node, act: Node): Boolean = {
    (exp, act) match {
      case (e: Elem, a: Elem) =>
        labelMatch(e, a) &&
          attributesMatch(e, a) &&
          e.child.forall(e1 =>
            act.child.exists(a1 => {
              println(s"expected $e1 \n actual $a1")
              apply(e1, a1)
            })
          )
      case (e: Text, a: Text) => e.text.equalsIgnoreCase(a.text)
      case _                  => false
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
