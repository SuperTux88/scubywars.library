package de.tdng2011.game.library

import java.io.DataInputStream
import util.{Vec2, StreamUtil}

class Player(stream : DataInputStream) {
  private val size = StreamUtil.read(stream, 4).getInt
  private val buf = StreamUtil.read(stream, size)

  val publicId  : Long    = buf.getLong
  val pos       : Vec2    = Vec2(buf.getFloat, buf.getFloat)
  val direction : Float   = buf.getFloat
  val radius    : Short   = buf.getShort
  val speed     : Short   = buf.getShort
  val rotSpeed  : Float   = buf.getFloat
  val turnLeft  : Boolean = buf.get == 1
  val turnRight : Boolean = buf.get == 1
  val thrust    : Boolean = buf.get == 1
  val fire      : Boolean = buf.get == 1
  
  override def toString() = "Player(id: " + publicId + ", pos: " + pos + ", direction: " + direction + ", radius: " + radius + ", speed: " + speed + 
                              ", rotSpeed: " + rotSpeed + ", turnLeft: " + turnLeft + ", turnRight: " + turnRight + ", thrust: " + thrust + ", fire: " + fire + ")"
}
object Player {
  def parsePlayerId(iStream : DataInputStream) = {
    val size = StreamUtil.read(iStream, 4).getInt
    val buf = StreamUtil.read(iStream, size)

    buf.getLong
  }

  def parsePlayerIdAndName(iStream : DataInputStream) = {
    val size = StreamUtil.read(iStream, 4).getInt
    val buf = StreamUtil.read(iStream, 8)

    (buf.getLong, StreamUtil.read(iStream, size-8).asCharBuffer.toString)
  }
}