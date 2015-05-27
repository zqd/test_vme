package ber

import util.ConvertUtil

class Tag private(rawTag: Array[Byte], startIndex: Int, len: Int) {
  private val firstByte = ConvertUtil.getUnsignedValue(rawTag(startIndex))
  private lazy val t = {
    val cp = new Array[Byte](len)
    rawTag.copyToArray(cp, startIndex)
    cp
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

  def getHexTag = ConvertUtil.bytes2Hex(rawTag, "")

  private[ber] def getLen = len

  override def toString = getHexTag
}

object Tag {
  def apply(hexStr: String): Tag = apply(ConvertUtil.hex2Bytes(hexStr))

  def apply(rawTag: Array[Byte]): Tag = apply(rawTag, 0, rawTag.length)

  def apply(rawTag: Array[Byte], startIndex: Int, len: Int): Tag = new Tag(rawTag, startIndex, len)

  def hasSubByte(firstByte: Byte) = (ConvertUtil.getUnsignedValue(firstByte) & 0x1F) == 0x1F
}
