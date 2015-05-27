import javax.smartcardio._
import MyUtil._
object MyApp extends App {
  val factory = TerminalFactory.getDefault
  val terminals = factory.terminals().list()

  val terminal = terminals.get(0)
  terminal.waitForCardPresent(10000)
  val card = terminal.connect("*")
  val channel = card.getBasicChannel

  println("选择PSE:1PAY.SYS.DDF01")
  var r = card.select("1PAY.SYS.DDF01", first = true)
  println(r)
  println(bytesToHex(r.getBytes))
  var tlv = new Tlv(r.getData)
  println(tlv.toString)

  println("读取应用ID")
  r = card.readRecord(tlv.findBy("88").getValue()(0), 1)
  println(r)
  println(bytesToHex(r.getBytes))
  tlv = new Tlv(r.getData)
  println(tlv.toString)

  println("选择应用")
  r = card.select(tlv.findBy("4F").getValue, first = true)
  println(r)
  println(bytesToHex(r.getBytes))
  tlv = new Tlv(r.getData)
  println(tlv.toString)

  println("获取处理选项")
  val pdol = Tlv.makeTlvBy(tlv.findBy("9F38").getValue)

  for(t <- pdol) {
    println(t)
  }

  r = card.getProcessingOptions(pdol)
  println(r)
  println(bytesToHex(r.getBytes))
  tlv = new Tlv(r.getData)
  println(tlv.toString)

  println("读处理选项")
  val aflsRaw = tlv.findBy("80").getValue
  val aip = aflsRaw.take(2)
  val afls = AFL.parseMultipleAFL(aflsRaw.drop(2))

  for(afl <- afls) {
    for(index <- afl.beginIndex.toInt to afl.endIndex) {
      r = card.readRecord(afl.sfi, index)
      println(r)
      println(bytesToHex(r.getBytes))
      tlv = new Tlv(r.getData)
      println(tlv.toString)
    }
  }

  def bytesToHex(bytes: Array[Byte]): String = {
    val sb = new StringBuilder()
    bytes.foreach(b => sb.append("%02X" format b).append(" "))
    sb.toString()
  }
}
