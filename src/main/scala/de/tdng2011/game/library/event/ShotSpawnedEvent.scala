package de.tdng2011.game.library.event

import java.io.DataInputStream
import de.tdng2011.game.library.util.{ StreamReading, Vec2 }

class ShotSpawnedEvent(iStream: DataInputStream) extends StreamReading(iStream) {

  val publicId: Long = buf.getLong
  val parentId: Long = buf.getLong
  val pos: Vec2 = Vec2(buf.getFloat, buf.getFloat)

  override def toString() = "ShotSpwawnedEvent(id: " + publicId + ", parentId: " + parentId + ", pos: " + pos + ")"
}