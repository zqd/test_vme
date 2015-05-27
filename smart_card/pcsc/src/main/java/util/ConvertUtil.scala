package util

import scala.collection.mutable.ListBuffer

object ConvertUtil {

  def bytes2Hex(bytes: Array[Byte], sep: String = " "): String = {
    bytes2Hex(bytes, 0, bytes.length, sep)
  }

  def bytes2Hex(bytes: Array[Byte], startIndex: Int, len: Int, sep: String): String = {
    var i = 0
    val sb = new StringBuilder
    while (i < len) {
      sb.append("%02X" format bytes(startIndex + i)).append(sep)
      i += 1
    }
    if (sb.nonEmpty && sep.length > 0) sb.delete(sb.length - sep.length, sb.length)
    sb.toString()
  }

  def hex2Bytes(hexStr: String): Array[Byte] = {
    val hex = hexStr.replaceAll("\\s+", "")
    val listBuffer = new ListBuffer[Byte]
    for (g <- hex.grouped(2)) {
      listBuffer += java.lang.Integer.valueOf(g, 16).toByte
    }
    listBuffer.toArray
  }

  def getUnsignedValue(b: Byte) = b & 0xFF
}
