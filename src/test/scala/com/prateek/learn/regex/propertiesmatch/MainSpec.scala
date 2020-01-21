package com.prateek.learn.regex.propertiesmatch

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import com.prateek.learn.regex.propertiesmatch.Main.{regEx, regexMatch}

class MainSpec extends AnyFreeSpec with Matchers {

  "regex match" - {
    "input string involving new line chars" - {
      "for MEMBER_VALUE" in {
        val content =
          " PROPERTIES MEMBER_CAPTION, MEMBER_UNIQUE_NAME, [DateCustom].[Reporting Day2].[Reporting\n                    Day2].[KEY0], [DateCustom].[Reporting Day2].[Reporting Day2].[NAME], [DateCustom].[Reporting\n                    Day2].[Reporting Day2].[MEMBER_VALUE] "
        regexMatch(regEx("MEMBER_VALUE"), content) should be(true)
      }
    }
  }
}
