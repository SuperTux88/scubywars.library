package de.tdng2011.game.library.event

import java.io.DataInputStream
import de.tdng2011.game.library.util.{ StreamReading, Vec2 }

class PlayerKilledEvent(iStream: DataInputStream) extends StreamReading(iStream) {

  val victimPublicId: Long = buf.getLong
  val shotPublicId: Long = buf.getLong
  val killerPublicId: Long = buf.getLong
  val shotPosition: Vec2 = Vec2(buf.getFloat, buf.getFloat)
  val victimPosition: Vec2 = Vec2(buf.getFloat, buf.getFloat)

  override def toString() = "PlayerKilledEvent(victimPublicId: " + victimPublicId + ", shotPublicId: " + shotPublicId + ", killerPublicId: " + killerPublicId +
    ", shotPosition: " + shotPosition + ", victimPosition: " + victimPosition + ")"
}