package test

import util.ConvertUtil

/**
 * Created by tao on 2015/5/27.
 */
object MyApp extends App {
  val list = List(1, 2, 3, 4, 5, 6)
  val l = list.grouped(2)

  //list.grouped()
  for(a <- l) {
    println(a)
  }
  println(l)

  val b: Byte = 0xFF.toByte
  println(b.formatted("%02X"))
  println(list.mkString("-"))

  val bs = Array[Byte](1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

  println(bytesToHex(bs))
  def bytesToHex(bytes: Array[Byte]): String = {
    bytes.map(_.formatted("%02X")).mkString(" ")
  }

  println(ConvertUtil.hex2Bytes("01 02 03 04 05 06 07 08 09 0A 0B 0C")(11))

  println((b & 0xFF).getClass)
}
