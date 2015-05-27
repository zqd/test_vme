package ber

import util.ConvertUtil

class Tag private(rawTag: Array[Byte]) {
  private val firstByte = ConvertUtil.getUnsignedValue(rawTag(0))

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

  def getTag = rawTag

  def getHexTag = ConvertUtil.bytes2Hex(rawTag, "")

  override def toString = getHexTag
}

object Tag {
  def apply(hexStr: String) = new Tag(ConvertUtil.hex2Bytes(hexStr))

  def apply(rawTag: Array[Byte]) = new Tag(rawTag)

  def hasSubByte(firstByte: Byte) = (ConvertUtil.getUnsignedValue(firstByte) & 0x1F) == 0x1F
}
