package ber

import util.ConvertUtil

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class TLV private(val tag: Tag, val len: Int, raw: Array[Byte], startIndex: Int = 0) {
  private lazy val children = new ListBuffer[TLV]
  private lazy val value = java.util.Arrays.copyOfRange(raw, startIndex, startIndex + len)
  val hasChild = tag.isStructObject && len > 0

  if (hasChild) {
    var index = startIndex
    val endIndex = index + len

    while (index < endIndex) {
      val c = TLV(raw, index)
      add(c)
      index = index + c.tag.getLen + 1 + c.len
    }
  }

  def add(child: TLV): Unit = {
    children += child
  }

  def getValue: Option[Array[Byte]] = if(!hasChild) Some(value) else None

  def getChildren: Option[Seq[TLV]] = if(hasChild) Some(children) else None

  def fill(arrayBuffer: ArrayBuffer[Byte]): Unit = {
    if (hasChild) {
      arrayBuffer ++= tag.getTag
      arrayBuffer += len.toByte

      children.foreach(_.fill(arrayBuffer))
    } else {
      arrayBuffer ++= value
    }
  }

  def getRawData: Array[Byte] = {
    val arrayBuffer = new ArrayBuffer[Byte]
    fill(arrayBuffer)
    arrayBuffer.toArray
  }

  override def toString = {
    val sb = new StringBuilder()
    fill("", sb)
    sb.toString()
  }

  private def fill(prefix: String, sb: StringBuilder): Unit = {
    if (hasChild) {
      sb.append(prefix).append("tag = ").append(tag.getHexTag).append("(").append(tag.getTagDesc).append(")").append(" len = ").append(len).append("\n")
      val childPrefix = prefix + "  "
      children.foreach(_.fill(childPrefix, sb))
    } else {
      sb.append(prefix).append("tag = ").append(tag.getHexTag).append("(").append(tag.getTagDesc).append(")").append(" len = ").append(len).append(" val = ").
        append(ConvertUtil.bytes2Hex(value)).append("\n")
    }
  }

  def find(tag: Array[Byte]): TLV = {

    find(ConvertUtil.bytes2Hex(tag, ""))
  }

  def find(tag: String): TLV = {
    if (this.tag.getHexTag == tag) this
    else if (hasChild) {
      var ret: TLV = null
      var i = 0
      var noFound = true
      while (i < children.length && noFound) {
        ret =children(i).find(tag)
        if (ret != null)  noFound = false
        i += 1
      }
      ret
    } else null
  }
}

object TLV {
  def apply(raw: Array[Byte], startIndex: Int = 0): TLV = {
    val tag = Tag(raw, startIndex)
    val l = ConvertUtil.getUnsignedValue(raw(startIndex + tag.getLen))
    new TLV(tag, l, raw, startIndex + tag.getLen + 1)
  }

  def apply(tag: Tag, raw: Array[Byte]): TLV = apply(tag, raw, 0)

  def apply(tag: Tag, raw: Array[Byte], startIndex: Int): TLV = {
    new TLV(tag, raw.length, raw, startIndex)
  }
}
