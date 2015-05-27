package util

import scala.collection.mutable.ListBuffer

object ConvertUtil {

  def bytes2Hex(bytes: Array[Byte], sep: String = " "): String = {
    bytes.map(_.formatted("%02X")).mkString(sep)
  }

  def hex2Bytes(hexStr: String): Array[Byte] = {
    val hex = hexStr.replaceAll("\\s+", "")
    val listBuffer = new ListBuffer[Byte]
    for(g <- hex.grouped(2)) {
      listBuffer += java.lang.Integer.valueOf(g, 16).toByte
    }
    listBuffer.toArray
  }

  def getUnsignedValue(b: Byte) = b & 0xFF
}
