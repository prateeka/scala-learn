package com.prateek.learn.xml.compare

object XmlCompare extends App {
  import scala.xml.Elem
  def apply(): Boolean = {
    val book: Elem =
      <Member Hierarchy="[DateCustom].[Retail445]"> hello
      <UName>[DateCustom].[Retail445].[Reporting Day].&amp;[2001-04-07T00:00:00]</UName>
      <Caption>Saturday, April 07 2001</Caption>
      <LName>[DateCustom].[Retail445].[Reporting Day]</LName>
      <LNum>6</LNum>
      <DisplayInfo>2</DisplayInfo>
      <PARENT_UNIQUE_NAME>[DateCustom].[Retail445].[Reporting Year].&amp;[2001-01-07T00:00:00].&amp;[2001-01-07T00:00:00].&amp;[2001]&amp;[1].&amp;[2001-03-04T00:00:00].&amp;[2001-04-01T00:00:00]</PARENT_UNIQUE_NAME>
    </Member>

//    println(book.text)
    book.child.foreach(println)
    true
  }

  apply()
}
