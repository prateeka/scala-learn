package com.prateek.learn.parser

import scala.util.parsing.combinator.lexical.Lexical
import scala.util.parsing.combinator.token.Tokens
import scala.util.parsing.input.CharSequenceReader

object Main extends Tokenizer with App {
  test("_erty1[assa]")
  test("[[asas]")
  test("----")
  test("[]]]as]")
  test("asdf[a]")
  test("asdf[a][")
  test("asdf[")
  test("[")
  test("[fgfgh")

  private def test(input: String): Unit = {
    @scala.annotation.tailrec
    def loop(scanner: Scanner): Unit = {
      println(s" ${scanner.first} ${scanner.pos} ${scanner.rest.pos}")
      scanner.first match {
        case _: BareIdentifier | _: QuotedIdentifier => loop(scanner.rest)
        case _                                       =>
      }
    }
    println(s"***for input: $input")
    loop(new Scanner(new CharSequenceReader(input)))
    println(s"")
  }
}

trait SampleTokenizer extends Tokens {
  sealed trait Identifier extends Token
  case class QuotedIdentifier(chars: String) extends Identifier
  case class BareIdentifier(chars: String) extends Identifier
}

class Tokenizer extends Lexical with SampleTokenizer {

  override def token: Parser[Token] = {
    import scala.util.parsing.input.CharArrayReader.EofCh

    val identBody: Parser[List[Char]] = rep(']' ~> ']' | chrExcept(EofCh, ']'))

    (('[' ~> identBody <~ ']' ^^ { body =>
      QuotedIdentifier(body.mkString)
    })
      | ((letter | '_') ~ rep(letter | digit | '_') ^^ {
      case h ~ t => BareIdentifier((h :: t).mkString)
    })
      | '[' ~> failure("unclosed identifier"))
  }

  override def whitespace: Parser[Any] = rep(whitespaceChar)
}
