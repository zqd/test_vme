package pboc

/**
 *  应用文件定位器 Application File Locator (AFL)
 *
 *  PBOC 3.0 - 4 p74
 *  TAG: 94
 *
 */
class AFL private(rawData: Array[Byte]) {
  /**
   * 段 EF 文件标识
   */
  val sfi = (rawData(0) >> 3).toByte
  /**
   * 第一条记录的记录号
   */
  val firstRecodeNo = rawData(1)

  /**
   * 最后一条记录的记录号
   */
  val lastRecodeNo = rawData(2)

  /**
   * 静态认证数据计数器
   */
  val authCount = rawData(3)

  override def toString = s"sfi = $sfi 第一条记录的记录号 = $firstRecodeNo 最后一条记录的记录号 = $lastRecodeNo 静态认证数据计数器 = $authCount"
}

object AFL{
  def apply(rawData: Array[Byte]) = new AFL(rawData)
  def parseMultipleAFL(rawData: Array[Byte]) = rawData.grouped(4).map(AFL(_))
}
