package de.tdng2011.game.library.connection

import java.net.Socket
import java.io.DataInputStream
import de.tdng2011.game.library.util.{ByteUtil, StreamUtil}
import de.tdng2011.game.library.{World, Shot, Player, ScoreBoard, EntityTypes}

abstract class AbstractClient(hostname : String, relation : RelationTypes.Value) extends Runnable {

  private var entityList = List[Any]()

  private var publicId : Long = -1

  private var connection : Socket = connect()

  new Thread(this).start

  def run() {
    var iStream = new DataInputStream(connection.getInputStream)

    while(true) {
      try {
        processEntity(getEntity(iStream))
      } catch {
        case e => {
          e.printStackTrace
          println("error while getting frame. trying to reconnect!");
          connection = connect();
          iStream = new DataInputStream(connection.getInputStream);
        }
      }
    }
  }

  def getEntity(iStream : DataInputStream) : Option[Any] = StreamUtil.read(iStream, 2).getShort match {
    case x if x == EntityTypes.World.id  => {
      Some(World(iStream))
    }
    case x if x == EntityTypes.Scoreboard.id  => {
      Some(ScoreBoard(iStream))
    }
    case x => {
      println("barbra streisand! (unknown bytes, wth?!) typeId: " + x)
      val size = StreamUtil.read(iStream, 4).getInt
      StreamUtil.read(iStream, size) //skip
      None
    }
  }

  def processEntity(entity : Option[Any]) {
    entity match {
      case x : Some[Any] => x.get match {
        case s : World => processWorld(s)
        case s : ScoreBoard => processScoreBoard(s)
      }
      case x => {}
    }
  }

  private def handshake(s : Socket) {
    relation match {
      case x if x == RelationTypes.Player     => handshakePlayer(s)
      case x if x == RelationTypes.Visualizer => handshakeVisualizer(s)
      case x => {
        println("barbra streisand! (unknown relation, wth?!) typeId: " + x)
        System exit -1
      }
    }
  }

  private def handshakePlayer(s : Socket) {
    val iStream = new DataInputStream(s.getInputStream)
    s.getOutputStream.write(ByteUtil.toByteArray(EntityTypes.Handshake, RelationTypes.Player.id.shortValue, name))

    val buf    = StreamUtil.read(iStream, 6)
    val typeId = buf.getShort
    val size   = buf.getInt

    val response = StreamUtil.read(iStream, size)
    if (typeId == EntityTypes.Handshake.id) {
      val responseCode = response.get
      println("response code: " + responseCode)
      if (responseCode == 0)
        publicId = response.getLong
    }
  }

  private def handshakeVisualizer(s : Socket) {
      s.getOutputStream.write(ByteUtil.toByteArray(EntityTypes.Handshake, RelationTypes.Visualizer.id.shortValue))
  }

  private def connect() : Socket = {
    try {
      val s = new Socket(hostname, 1337)
      handshake(s)
      s
    } catch {
      case e => {
        println("connecting failed. retrying in 5 seconds");
        Thread.sleep(5000)
        connect()
      }
    }
  }

  def getConnection = connection

  def getPublicId = publicId

  def name = "Player"

  def processWorld(world : World) : Unit

  def processScoreBoard(scoreBoard : ScoreBoard) : Unit

  def action(turnLeft : Boolean, turnRight : Boolean, thrust : Boolean, fire : Boolean) {
    getConnection.getOutputStream.write(ByteUtil.toByteArray(EntityTypes.Action, turnLeft, turnRight, thrust, fire))
  }
}
