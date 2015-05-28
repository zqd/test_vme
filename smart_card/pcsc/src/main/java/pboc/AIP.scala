package pboc

import util.ConvertUtil

/**
  * 应用交互特征 Application Interchange Profile (AIP)
  *
  * PBOC 3.0 - 4 p74
  * TAG: 82
  *
  */
class AIP private(b1: Byte, b2: Byte) {
  private val i1 = ConvertUtil.getUnsignedValue(b1)

  /**
   * SDA 支持
   */
  val supportSDA = (i1 & 0x40) != 0
  /**
   * DDA 支持
   */
  val supportDDA = (i1 & 0x20) != 0
  /**
   * 持卡人认证支持
   */
  val supportCardholderAuth = (i1 & 0x10) != 0
  /**
   * 终端风险管理支持
   */
  val supportTerminalRisk = (i1 & 0x8) != 0
  /**
   * 发卡行认证支持
   */
  val supportIssuerAuth = (i1 & 0x4) != 0
  /**
   * CDA 支持
   */
  val supportCDA = (i1 & 0x1) != 0

  override def toString =
    s"""(${getSupportSymbol(supportSDA)})支持 SDA
       |(${getSupportSymbol(supportDDA)})支持 DDA
       |(${getSupportSymbol(supportCardholderAuth)})支持持卡人认证
       |(${getSupportSymbol(supportTerminalRisk)})支持终端风险管理
       |(${getSupportSymbol(supportIssuerAuth)})支持发卡行认证
       |(${getSupportSymbol(supportCDA)})支持 CDA
     """.stripMargin

  private def getSupportSymbol(isSupport: Boolean): String = if(isSupport) "*" else " "
}

object AIP {
  def apply(raw: Array[Byte], startIndex: Int = 0) = new AIP(raw(startIndex), raw(startIndex + 1))
}
