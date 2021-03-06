package pw.byakuren.tbf.util

import java.awt.Color

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.{MessageEmbed, User}

object Embeds {

  def levelup(u: User, pl:Int, l: Int): MessageEmbed = {
    footerAuthor(u).setTitle("Level up!").setDescription(s"$pl => $l").setColor(Color.GREEN).build()
  }

  def insufficientLevel(u: User, cl:Int, rl: Int): MessageEmbed = {
    footerAuthor(u).setTitle("Insufficient Level!").setDescription(s"Required: $rl Current $cl").setColor(Color.RED).build()
  }

  def footerAuthor(u: User): EmbedBuilder = {
    new EmbedBuilder().setFooter(u.getName, u.getEffectiveAvatarUrl)
  }

  def insufficientEnergy(u: User, e: Int): MessageEmbed = {
    footerAuthor(u).setTitle(f"You don't have enough energy to perform this action. Needed: $e").setColor(Color.RED).build()
  }

  def ballsMade(u: User, balls: String, wittyQuote: String): MessageEmbed = {
    footerAuthor(u).setTitle(f"$wittyQuote").setDescription(balls).setColor(Color.CYAN).build()
  }

}
