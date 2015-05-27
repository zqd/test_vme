import javax.smartcardio.{ResponseAPDU, CommandAPDU, Card}

import scala.collection.mutable.ListBuffer

object MyUtil {
  implicit class MyCard(card: Card) {
    def select(dfName: String, first: Boolean): ResponseAPDU = {
      select(dfName.getBytes(), first)
    }

    def select(dfName: Array[Byte], first: Boolean): ResponseAPDU = {
      val cmd = "00 A4 04 " + (if(first) "00" else "02") + (" %02X " format dfName.length) + bytesToHex(dfName)
      val apdu = new CommandAPDU(hexToBytes(cmd))
      card.getBasicChannel.transmit(apdu)
    }

    def readRecord(sfi: Byte, recordNo: Int) = {
      val cmd = "00 B2 " + ("%02X " format recordNo) + ("%02X " format (sfi << 3 | 0x04)) + "00"
      val apdu = new CommandAPDU(hexToBytes(cmd))
      card.getBasicChannel.transmit(apdu)
    }

    def getProcessingOptions(pdol: Array[Tlv]) = {
      val lb = new ListBuffer[Byte]
      for(pdo <- pdol) {
        lb ++= pdo.getValue
      }

      val tag83 = new Tlv(lb.toArray, 0, lb.size, Array(0x83.toByte))
      val data = tag83.getRawByteArray

      val cmd = "80 A8 00 00 " + ("%02X " format data.length) + bytesToHex(data) + "00"
      val apdu = new CommandAPDU(hexToBytes(cmd))
      card.getBasicChannel.transmit(apdu)
    }

    private def hexToBytes(hex: String): Array[Byte] = {
      val hexPairs = hex.split("\\s+")
      val bytes = hexPairs.map { pair => java.lang.Integer.parseInt(pair.mkString, 16).toByte }
      bytes
    }

    private def bytesToHex(bytes: Array[Byte]): String = {
      val sb = new StringBuilder()
      bytes.foreach(b => sb.append("%02X" format b).append(" "))
      sb.toString()
    }
  }

  class AFL(rawData: Array[Byte]) {
    val sfi = (rawData(0) >> 3).toByte
    val beginIndex = rawData(1)
    val endIndex = rawData(2)
    val authCount = rawData(3)

    override def toString = s"sfi = $sfi beginIndex = $beginIndex endIndex = $endIndex authCount = $authCount"
  }

  object AFL{
    def apply(rawData: Array[Byte]) = new AFL(rawData)
    def parseMultipleAFL(rawData: Array[Byte]) = rawData.grouped(4).map(AFL(_))
  }
}
