package pw.byakuren.tbf.util

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.{GuildChannel, TextChannel}

class Channels(jda: JDA, stockId: Long, targetBallId: Long) {

   val stockChannel: TextChannel = jda.getTextChannelById(stockId)
   val targetBallChannel: TextChannel = jda.getTextChannelById(targetBallId)

}
