package com.prateek.learn.xml.compare

import scala.xml.{Elem, MetaData, Node}

object XmlCompare extends App {

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

    e1.attributes.filter(_.value != null).forall(contains(_))
  }

  def apply(exp: Node, act: Node): Boolean = {
    (exp, act) match {
      case (e: Elem, a: Elem) => labelMatch(e, a) && attributesMatch(e, a)
      case _                  => false
    }
  }

  def labelMatch(e1: Elem, e2: Elem) =
    e1.label == e2.label &&
      e1.scope.getURI(e1.prefix) == e2.scope.getURI(e2.prefix)
}
