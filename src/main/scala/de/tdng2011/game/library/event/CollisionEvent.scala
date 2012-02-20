package de.tdng2011.game.library.event

import java.io.DataInputStream
import de.tdng2011.game.library.util.{ StreamReading, Vec2 }

class CollisionEvent(iStream: DataInputStream) extends StreamReading(iStream) {

  val publicId1: Long = buf.getLong
  val publicId2: Long = buf.getLong
  val pos1: Vec2 = Vec2(buf.getFloat, buf.getFloat)
  val pos2: Vec2 = Vec2(buf.getFloat, buf.getFloat)

  override def toString() = "Collision(id1: " + publicId1 + ", id2: " + publicId2 + ", pos1: " + pos1 + ", pos2: " + pos2 + ")"
}