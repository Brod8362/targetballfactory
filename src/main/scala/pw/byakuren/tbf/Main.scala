package pw.byakuren.tbf

import java.io.{File, FileInputStream, InputStream}

import org.yaml.snakeyaml.Yaml

object Main {

  def main(args: Array[String]): Unit = {
    val yaml = new Yaml()
    val dir = System.getProperty("user.dir")
    val input:InputStream = new FileInputStream(new File(dir+"/config.yaml"))
    val output:java.util.LinkedHashMap[String,AnyRef] = yaml.load(input)

  }

}
