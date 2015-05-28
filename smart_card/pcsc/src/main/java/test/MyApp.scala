package test

import java.util

import _root_.util.ConvertUtil
import ber.{Tag, TLV}
import pboc.AIP

import scala.xml.XML

object MyApp extends App {

  Tag.xml = XML.load(MyApp.getClass.getResource("/tag_des.xml"))
  val str = "70 81 B8 9F 46 81 B0 62 30 E9 E6 9F 18 23 98 D6 82 5C F6 E3 4D E0 C6 F6 16 64 52 36 F3 45 16 BC 0A 3F 7B E4 6E 5A 70 3D C5 EE EE A8 40 E9 E0 03 48 75 AE 2F 9E 8B 0E 0E E2 F8 DB 8D E8 37 93 F3 C4 B9 44 5F B5 9D 63 C3 42 2A 70 6A C6 5D D3 4D 0C BB 63 B4 CC E3 BA 7B 04 D7 E0 71 23 F7 4C D2 26 25 3A EE FB 94 5B DD 79 80 EE 28 4A E4 39 6B 6A A6 FE 2B DB 9E 98 31 79 E4 43 24 D0 32 43 96 EF 9F 6F 06 E1 85 90 76 37 C1 CB FD 6A B7 58 E1 C9 65 44 09 DC 15 AB 27 BE 24 9E 6D 49 A6 CF F6 95 0E DF 3B 6C EF 81 FA 9F 59 A9 06 2C 84 46 8A 29 8C 44 0B A2 93 EE 9F 47 01 03 90 00"
  val tlv = TLV(ConvertUtil.hex2Bytes(str))
  println(tlv.toString)

  val l = Array[Byte](1, 2, 3, 4, 5, 6)
  println(util.Arrays.copyOfRange(l, 3, 6).length)

  val i = 0xF0
  println(AIP(Array(i.toByte, i.toByte)))
  println(i & 0x1)

  println(Tag.getTagDesc("5F30"))
}
