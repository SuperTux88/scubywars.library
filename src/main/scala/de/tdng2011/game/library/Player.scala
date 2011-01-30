package de.tdng2011.game.library

import java.io.DataInputStream
import util.StreamUtil

class Player (stream : DataInputStream) {
  val buf = StreamUtil.read(stream, 32)

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