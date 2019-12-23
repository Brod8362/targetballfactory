package pw.byakuren.tbf.command
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import pw.byakuren.tbf.util.Utility._

class MeCommand extends Command {
  override val name: String = "me"
  override val desc: String = "View information about yourself"

  override def run(m: Message, args:Array[String]): Unit = {
    val ec = m.getAuthor.economyUser()
    ec match {
      case Some(x) =>
        val eb = new EmbedBuilder().setTitle(x.user.getName)
          .setThumbnail(m.getAuthor.getEffectiveAvatarUrl)
          .addField("Money", f"$$${x.balance.amount}%.2f", true)
          .addField("Level", s"${x.level}", true)
          .addField("XP", s"${x.xp}/${x.totalXpRequired(x.level+1)}", true)
        m.getChannel.sendMessage(eb.build()).queue()
      case None => m.getChannel.sendMessage("you don't exist").queue()
    }
  }
}
