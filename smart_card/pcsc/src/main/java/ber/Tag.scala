package ber

import util.ConvertUtil
import scala.xml.Node

class Tag private(raw: Array[Byte], startIndex: Int, len: Int) {
  private val firstByte = ConvertUtil.getUnsignedValue(raw(startIndex))
  private lazy val t = {
    java.util.Arrays.copyOfRange(raw, startIndex, startIndex + len)
  }

  lazy val level = {
    if ((firstByte & 0xC0) == 0) "通用级(Universal class)"
    else if ((firstByte & 0xC0) == 0xC0) "私有级(Private class)"
    else if ((firstByte & 0x80) != 0) "规范级(Context-specific class)"
    else "应用级(Application class)"
  }

  val isStructObject = (firstByte & 0x20) != 0

  lazy val dataType = {
    if (isStructObject) "基本数据对象(Primitive data object)"
    else "结构数据对象(Constructed data object)"
  }

  def getTag = t

  def getHexTag = ConvertUtil.bytes2Hex(raw, startIndex, len, "")

  def getTagDesc = Tag.getTagDesc(getHexTag)

  private[ber] def getLen = len

  override def toString = s"tag = $getHexTag level = $level dataType = $dataType"
}

object Tag {
  def apply(hexStr: String): Tag = apply(ConvertUtil.hex2Bytes(hexStr))

  def apply(rawTag: Array[Byte]): Tag = apply(rawTag, 0)

  def apply(rawTag: Array[Byte], startIndex: Int): Tag = new Tag(rawTag, startIndex, getTagLen(rawTag, startIndex))

  def hasSubByte(firstByte: Byte) = (ConvertUtil.getUnsignedValue(firstByte) & 0x1F) == 0x1F

  var xml: Node = null

  def getTagDesc(tagHexStr: String): String = {
    if(xml == null) "未知"
    else {
      val nodes = xml \\ "tag" filter(node => node.attribute("key").get.text == tagHexStr)

      if(nodes.length > 0)
        nodes(0).attribute("name").get.text
      else
        "未知"
    }
  }

  private def getTagLen(raw: Array[Byte], startIndex: Int): Int = {
    var len = 1
    var hasC = (ConvertUtil.getUnsignedValue(raw(startIndex)) & 0x1F) == 0x1F

    while(hasC) {
      hasC = (ConvertUtil.getUnsignedValue(raw(startIndex + len)) & 0x80) != 0
      len += 1
    }

    len
  }
}
