package de.tdng2011.game.library

import java.io.DataInputStream
import util.StreamUtil

/**
 * Created by IntelliJ IDEA.
 * User: benjamin
 * Date: 24.01.11
 * Time: 23:47
 * To change this template use File | Settings | File Templates.
 */

class Shot(stream : DataInputStream) {
  val buf = StreamUtil.read(stream, 36)

  val publicId  : Long    = buf.getLong
  val pos       : Vec2    = Vec2(buf.getFloat, buf.getFloat)
  val direction : Float   = buf.getFloat
  val radius    : Short   = buf.getShort
  val speed     : Short   = buf.getShort
  val parentId  : Long    = buf.getLong
  val lifeTime  : Float   = buf.getFloat
  
  override def toString() = "Shot(id: " + publicId + ", pos: " + pos + ", direction: " + direction + ", radius: " + radius + ", speed: " + speed + 
                              ", parentId: " + parentId + ", lifeTime: " + lifeTime + ")"
}