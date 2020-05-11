package com.prateek.learn.xml.compare

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

class XmlCompareSpec extends AnyFreeSpec with Matchers {

  "Node comparison" - {
    "elem comparison" - {
      "label comparison" - {
        "match" in {
          val exp = <Caption>Saturday, April 07 2001</Caption>
          val act = <Caption>Saturday, April 07 2001</Caption>
          XmlCompare.apply(exp, act) should be(true)
        }

        "does not match" in {
          val exp = <Caption>Saturday, April 07 2001</Caption>
          val act = <ABV>Saturday, April 07 2001</ABV>
          XmlCompare.apply(exp, act) should be(false)
        }
      }

      "attributes comparison when labels match" - {
        "match" in {
          val exp = <Caption attr="123">Saturday, April 07 2001</Caption>
          val act = <Caption attr="123">Saturday, April 07 2001</Caption>
          XmlCompare.apply(exp, act) should be(true)
        }

        "key does not match" in {
          val exp = <Caption attr="123">Saturday, April 07 2001</Caption>
          val act = <Caption cat="123">Saturday, April 07 2001</Caption>
          XmlCompare.apply(exp, act) should be(false)
        }

        "value does not match" in {
          val exp = <Caption attr="1231">Saturday, April 07 2001</Caption>
          val act = <Caption attr="123">Saturday, April 07 2001</Caption>
          XmlCompare.apply(exp, act) should be(false)
        }
      }
    }

    "text comparison" - {
      "match" in {
        val exp = <Caption>first</Caption>
        val act = <Caption>first</Caption>
        XmlCompare.apply(exp.child.head, act.child.head) should be(true)
      }

      "does not match" in {
        val exp = <Caption>first</Caption>
        val act = <Caption>second</Caption>
        XmlCompare.apply(exp.child.head, act.child.head) should be(false)
      }

    }
  }
}
