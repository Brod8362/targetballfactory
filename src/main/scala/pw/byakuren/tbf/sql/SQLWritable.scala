package pw.byakuren.tbf.sql

trait SQLWritable {

  def write()(implicit sql: SQLConnection): Unit

}
