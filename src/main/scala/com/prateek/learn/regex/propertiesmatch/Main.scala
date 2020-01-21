package com.prateek.learn.regex.propertiesmatch

import scala.io.Source
import scala.jdk.CollectionConverters._
import scala.util.Using
import scala.util.matching.Regex

import java.io.File
import java.nio.file.{Files, Path}
import java.nio.file.Files.newDirectoryStream
import java.util

object Main extends App {

  // list of all MDX properties
  private val allProperties: Seq[String] = PropertiesReader.apply()
  // list of all queries inside multiple "queries" folder
  private val queries: Seq[Query] = QueryReader.apply(args(0))

  /**
    * Identifies MDX properties used by going through all query files until a single usage of a MDX property is found. This process is repeated by looping through all MDX properties.
    */
  private val usedProperties: Seq[String] = allProperties.filter(prop => {
    val regex = regEx(prop)
    queries.find(query => regexMatch(regex, query.content)) match {
      case Some(query) =>
        println(s"$prop found in ${query.path}")
        true
      case None => false
    }
  })

  def regEx(prop: String) = {
    // i -> [case insensitive] (https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#CASE_INSENSITIVE)
    // s -> [necessary if search string consists of newline characters] (https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#DOTALL)
    raw"(?is)(properties|measures).+$prop".r
  }

  println
  allProperties
    .diff(usedProperties)
    .foreach(p => println(s"unused $p"))
  println
  allProperties
    .intersect(usedProperties)
    .foreach(p => println(s"used $p"))

  def regexMatch(regex: Regex, str: String): Boolean = {
    regex.findFirstIn(str).nonEmpty
  }

  case class Query(path: Path) {

    /**
      * Reads the contents of the query files lazily.
      */
    lazy val content: String =
      Reader.apply(path.toFile, (source: Source) => source.mkString)
  }

  /**
    * Loaner pattern usage for file IO.
    */
  object Reader {
    def apply[T](file: File, read: Source => T): T =
      Using(Source.fromFile(file)) { read }.get
  }

  /**
    * Reads list of MDX properties.
    */
  object PropertiesReader {

    /**
      * This is the hard coded location of all MDX properties.
      */
    private val propertiesSource =
      "/Users/prateek/code/pattu/scala-learn/src/main/resources/mdx-properties"
    def apply(): Seq[String] = {
      Reader.apply(
        new File(propertiesSource),
        (source: Source) => source.getLines.toList
      )
    }
  }

  /**
    * Reads queries
    */
  object QueryReader {

    implicit def toLazyList(iterator: util.Iterator[Path]): List[Path] =
      iterator.asScala.to(List)

    /**
      * Searches for "queries" folder recursively starting from user specified folder. "quueries" folder can exist within multiple sub-folders.
      *  It stores the [[Path] of query files in [[Query]] and returns it.
      *
      * @param startDir: user specified parent folder to start the search recursively for "queries" folder
      * @return [[Seq]] of query files [[Path]]
      */
    def apply(startDir: String): Seq[Query] =
      findQueries(new File(startDir).toPath)

    /**
      * Returns [[Path]] for all queries(files) in "queries" folder by recursively searching inside provided directory
      *
      * @param start directory to search
      * @return [[Path]] for files containing queries
      */
    private def findQueries(start: Path): Seq[Query] = {
      if (start.endsWith("queries"))
        Files
          .list(start)
          .iterator()
          .map(Query)
      else {
        newDirectoryStream(start, (entry: Path) => isDirectory(entry))
          .iterator()
          .flatMap(dir => findQueries(dir))
      }
    }

    private def isDirectory(path: Path) = {
      path.toFile.isDirectory
    }
  }
}
