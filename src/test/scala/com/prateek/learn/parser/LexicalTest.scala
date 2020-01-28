package com.prateek.learn.parser

import org.scalatest.funsuite.AnyFunSuite

class LexicalTest extends AnyFunSuite {

  test("_erty1[assa]") {
    assert(Set.empty.size == 1)
  }

  /*
  private def verify(input: String): Unit = {
 @scala.annotation.tailrec
def loop(scanner: Scanner): Unit = {
println(s" ${scanner.first} ${scanner.pos} ${scanner.rest.pos}")
scanner.first match {
 case _: StringLiteral => loop(scanner.rest)
 case _                =>
}
   */
//new LexicalTokenizer.Scanner(new CharSequenceReader(input))
}
