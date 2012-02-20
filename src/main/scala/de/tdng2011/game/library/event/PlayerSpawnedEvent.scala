package de.tdng2011.game.library.event

import java.io.DataInputStream
import de.tdng2011.game.library.util.{ StreamReading, Vec2 }

class PlayerSpawnedEvent(iStream: DataInputStream) extends StreamReading(iStream) {

  val publicId: Long = buf.getLong
  val pos: Vec2 = Vec2(buf.getFloat, buf.getFloat)

  override def toString() = "PlayerSpawnedEvent(id: " + publicId + ", pos: " + pos + ")"
}