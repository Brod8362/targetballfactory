package pw.byakuren.tbf.user

import java.awt.Color

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.User
import pw.byakuren.tbf.actions.Action
import pw.byakuren.tbf.inventory.{Inventory, Item}
import pw.byakuren.tbf.util.{Embeds, SQLWritable, Utility}

class EconomyUser(val user: User, var xp: Int, val balance: Balance, var inventory: Inventory, var energy: Int, var maxEnergy: Int) extends SQLWritable {

  def this(user: User) {
    this(user, 0, new Balance(), new Inventory(), 10, 10)
  }

  final def level:Int = {
    var i = 0
    while (xp >= totalXpRequired(i+1)) i+=1
    i
  }

  final def xpRequired(l: Int): Int = 5 + ((l-1)*3)

  final def totalXpRequired(l: Int): Int = {
    if (l < 1) 0
    else xpRequired(l)+totalXpRequired(l-1)
  }

  def perform(a: Action) {
    val plevel = level
    if (a.minimumLevel>level) {
      a.context.getChannel.sendMessage(Embeds.insufficientLevel(user, level, a.minimumLevel)).queue()
      return
    }
    if (a.energyCost > energy) {
      a.context.getChannel.sendMessage(Embeds.insufficientEnergy(user, a.energyCost)).queue()
      return
    }
    if (a.perform(this)) {
      xp += a.xpYield
      charge(-1*a.energyCost)
    }
    if (level > plevel) {
      a.context.getChannel.sendMessage(Embeds.levelup(user, plevel, level)).queue()
    }
  }

  def charge(e: Int): Unit = {
    energy=(energy+e).min(maxEnergy)
  }

  def netWorth: Double = {
    balance.amount+inventory.cumulativeItemValue
  }

  override def write(): Unit = ???

}
