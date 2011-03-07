package de.tdng2011.game.library.connection

import java.net.Socket
import java.io.DataInputStream
import de.tdng2011.game.library.{World, Shot, Player, ScoreBoard, EntityTypes}
import de.tdng2011.game.library.util.{ScubywarsLogger, ByteUtil, StreamUtil}

abstract class AbstractClient(hostname : String, relation : RelationTypes.Value) extends Runnable with ScubywarsLogger {

  private var world : World = null
  private var scoreBoard : Map[Long, Int] = Map()
  private var nameMap : Map[Long, String] = Map()

  private var publicId : Long = -1

  private var connection : Socket = connect()

  new Thread(this).start

  def run() {
    var iStream = new DataInputStream(connection.getInputStream)

    while(true) {
      try {
        readEntity(iStream)
      } catch {
        case e => {
          logger.warn("error while getting frame. trying to reconnect!", e);
          connection = connect();
          iStream = new DataInputStream(connection.getInputStream);
        }
      }
    }
  }

  def readEntity(iStream : DataInputStream) {
    StreamUtil.read(iStream, 2).getShort match {
      case x if x == EntityTypes.World.id  => {
        world = World(iStream)
        processWorld(world)
      }
      case x if x == EntityTypes.ScoreBoard.id  => {
        scoreBoard = ScoreBoard.parseScoreBoard(iStream)
        processScoreBoard(scoreBoard)
      }
      case x if x == EntityTypes.PlayerJoined.id  => {
        val player = Player.parsePlayerIdAndName(iStream)
        addPlayer(player)
      }
      case x if x == EntityTypes.PlayerLeft.id  => {
        val playerId = Player.parsePlayerId(iStream)
        scoreBoard = scoreBoard - playerId
        nameMap = nameMap - playerId
        updatePlayers
      }
      case x if x == EntityTypes.PlayerName.id  => {
        val player = Player.parsePlayerIdAndName(iStream)
        addPlayer(player)
      }
      case x => {
        logger.warn("unknown typeId received: " + x)
        val size = StreamUtil.read(iStream, 4).getInt
        StreamUtil.read(iStream, size) //skip
      }
    }
  }

  def addPlayer(player : (Long, String)) {
    scoreBoard = scoreBoard + (player._1 -> 0)
    nameMap = nameMap + (player._1 -> player._2)
    updatePlayers
  }

  def updatePlayers {
    processScoreBoard(scoreBoard)
    processNames(nameMap)
  }

  private def handshake(s : Socket) {
    relation match {
      case x if x == RelationTypes.Player     => handshakePlayer(s)
      case x if x == RelationTypes.Visualizer => handshakeVisualizer(s)
      case x => {
        logger.warn("unknown relation: " + x)
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
      logger.info("connected! response code: " + responseCode)
      if (responseCode == 0)
        publicId = response.getLong
      logger.info("public ID: " + responseCode)
    }
  }

  private def handshakeVisualizer(s : Socket) {
      s.getOutputStream.write(ByteUtil.toByteArray(EntityTypes.Handshake, RelationTypes.Visualizer.id.shortValue))
  }

  private def connect() : Socket = {
    try {
      val s = new Socket(hostname, 1337)
      s.setTcpNoDelay(true)
      handshake(s)
      s
    } catch {
      case e => {
        logger.warn("connecting failed. retrying in 5 seconds");
        logger.debug("connection failed exception: ", e)
        Thread.sleep(5000)
        connect()
      }
    }
  }

  def getConnection = connection

  def getPublicId = publicId

  def name = "Player"

  def getWorld = world
  def getScoreBoard = scoreBoard
  def getNames = nameMap

  def processWorld(world : World) : Unit
  def processScoreBoard(scoreBoard : Map[Long, Int]) {}
  def processNames(names : Map[Long, String]) {}

  def action(turnLeft : Boolean, turnRight : Boolean, thrust : Boolean, fire : Boolean) {
    getConnection.getOutputStream.write(ByteUtil.toByteArray(EntityTypes.Action, turnLeft, turnRight, thrust, fire))
  }
}
