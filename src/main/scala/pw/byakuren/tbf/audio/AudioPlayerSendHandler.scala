package pw.byakuren.tbf.audio

import java.nio.ByteBuffer

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler

class AudioPlayerSendHandler(audioPlayer: AudioPlayer) extends AudioSendHandler {

  var lastFrame: AudioFrame = null

  override def canProvide: Boolean = {
    lastFrame = audioPlayer.provide()
    lastFrame != null
  }

  override def provide20MsAudio(): ByteBuffer = {
    ByteBuffer.wrap(lastFrame.getData)
  }

  override def isOpus: Boolean = true
}
