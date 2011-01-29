package de.tdng2011.game.library.util

import java.nio.ByteBuffer
import java.io.DataInputStream

/**
 * Created by IntelliJ IDEA.
 * User: benjamin
 * Date: 25.01.11
 * Time: 21:26
 * To change this template use File | Settings | File Templates.
 */

object StreamUtil {

  def read(stream : DataInputStream, count : Int) : ByteBuffer = {
    val byteArray = new Array[Byte](count)
    stream.readFully(byteArray)
    ByteBuffer.wrap(byteArray)
  }
}