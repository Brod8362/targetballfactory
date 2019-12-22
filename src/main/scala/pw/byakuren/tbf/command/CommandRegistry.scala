package pw.byakuren.tbf.command

import scala.collection.mutable

class CommandRegistry {

  private var commands = new mutable.HashSet[Command]()

  def register(c: Command): Unit = commands.addOne(c)

  def find(n: String): Option[Command] = commands.find{ _.name == n }

  def all: Set[Command] = commands.toSet

}
