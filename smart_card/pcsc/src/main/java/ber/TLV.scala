package ber

import scala.collection.mutable.ListBuffer

class TLV private(val tag: Tag, val len: Int, value: Array[Byte], startIndex: Int = 0) {
  private lazy val children = new ListBuffer[TLV]
  val hasChild = tag.isStructObject && len > 0

  if (hasChild) {
    var index = startIndex + tag.getLen + 1
    val endIndex = index + len

    while(index < endIndex) {
      val c = TLV(value, index)
      add(c)
      index = index + c.tag.getLen + 1 + c.len
    }
  }

  def add(child: TLV): Unit = {
    children += child
  }
}

object TLV {
  def apply(rawBytes: Array[Byte], startIndex: Int = 0): TLV = {
    val len = rawBytes.indexWhere(!Tag.hasSubByte(_), startIndex) - startIndex + 1
    val tag = Tag(rawBytes, startIndex, len)
    val l = rawBytes(startIndex + len)
    new TLV(tag, l, rawBytes, startIndex)
  }
}