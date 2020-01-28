package com.prateek.learn.parser

import scala.util.parsing.combinator.lexical.Lexical
import scala.util.parsing.combinator.token.Tokens
import scala.util.parsing.combinator.PackratParsers
import scala.util.parsing.combinator.syntactical.TokenParsers
import scala.util.{Try, Failure => TryFailure, Success => TrySuccess}

object Main extends App with PackratParsers with TokenParsers {

  type Tokens = LexicalTokens
  final val lexical = LexicalTokenizer
  test("_erty1[assa]")
  test("[[asas]")
  test("----")
  test("[]]]as]")
  test("asdf[a]")
  test("asdf[a][")
  test("asdf[")
  test("[")
  test("[fgfgh]")

  private def test(param: String): Try[Int] = {
    lazy val packratReader: Main.PackratReader[LexicalTokenizer.Token] =
      new PackratReader(new LexicalTokenizer.Scanner(param))
    lazy val packratParser: PackratParser[Int] = tokenToParser(
      LexicalTokenizer.StringLiteralObj
    ) ^^^ 1
    packratParser(packratReader) match {
      case Success(result, next) => TrySuccess(result)
      case Failure(msg, _) =>
        TryFailure(new Exception("failed with message $msg"))
    }
  }

  private def tokenToParser(token: Elem): Parser[token.type] =
    elem(token).asInstanceOf[Parser[token.type]]
}

trait LexicalTokens extends Tokens {
  sealed trait Literal extends Token
  case object StringLiteralObj extends Literal {
    override def chars: String = this.toString
  }
}

object LexicalTokenizer extends Lexical with LexicalTokens {

  override def token: Parser[Token] = {
    import scala.util.parsing.input.CharArrayReader.EofCh

    val identBody: Parser[List[Char]] = rep(']' ~> ']' | chrExcept(EofCh, ']'))

    (('[' ~> identBody <~ ']' ^^ { body =>
//      StringLiteral(body.mkString)
      StringLiteralObj
    })
      | '[' ~> failure("unclosed identifier"))
  }

  override def whitespace: Parser[Any] = rep(whitespaceChar)
}
