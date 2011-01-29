package de.tdng2011.game.library.util

import java.nio.ByteBuffer
import java.nio.ByteBuffer._

object ByteUtil {

  def toByteArray(a : Any*) : Array[Byte] = {
    val byteBuffer : ByteBuffer = allocate(a.size*8) // pessimistic size, works if all elements are 8 bytes
    for(x <- a){
      x match {
        case x : Float => byteBuffer.putFloat(x)
        case x : Double => byteBuffer.putDouble(x)
        case x : Long => byteBuffer.putLong(x)
        case x : Int => byteBuffer.putInt(x)
        case x : Short => byteBuffer.putShort(x)
        case x : Char => byteBuffer.putChar(x)
        case x : Byte => byteBuffer.put(x)
        case x : Boolean => byteBuffer.put(if(x) 1.byteValue else 0.byteValue)
        case barbraStreisand => println("error! unknown value, your byte array will not contain " + barbraStreisand)
      }
    }
    val byteArray = new Array[Byte](byteBuffer.position)
    byteBuffer.position(0)
    byteBuffer.get(byteArray)
    byteArray
  }
}