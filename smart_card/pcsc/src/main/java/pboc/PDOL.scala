package pboc

/**
 * 处理选项数据对象列表 Processing Options Data Object List (PDOL)
 *
 * PBOC 3.0 - 4 p86
 * TAG: 9F37
 *
 */
class PDOL private(raw: Array[Byte]) {
  /**
   * 应用交互特征
   */
  val aip = AIP(raw)
  /**
   * 应用文件定位器
   */
  val afls = AFL.parseMultipleAFL(java.util.Arrays.copyOfRange(raw, 2, raw.length))
}

object PDOL {
  def apply(raw: Array[Byte]) = new PDOL(raw)
}
