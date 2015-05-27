package test

import java.util

import _root_.util.ConvertUtil
import ber.TLV

object MyApp extends App {
  val str = "70 58 5F 30 02 02 20 8C 1B 9F 02 06 9F 03 06 9F 1A 02 95 05 5F 2A 02 9A 03 9C 01 9F 37 04 9F 21 03 9F 4E 14 8D 1D 8A 02 9F 02 06 9F 03 06 9F 1A 02 95 05 5F 2A 02 9A 03 9C 01 9F 37 04 9F 21 03 9F 4E 14 9F 08 02 00 20 9F 49 03 9F 37 04 5F 34 01 01 9F 14 01 00 9F 23 01 00 90 00"
  val tlv = TLV(ConvertUtil.hex2Bytes(str))
    println(tlv.toString)

  val l = Array[Byte](1, 2, 3, 4, 5, 6)
  println(util.Arrays.copyOfRange(l, 3, 6).length)
}
