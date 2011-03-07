package de.tdng2011.game.library.util
import org.apache.log4j.Logger;


/**
 * simple logging trait using log4j
 */
trait ScubywarsLogger {
  val loggerName = this.getClass.getName
  lazy val logger = Logger.getLogger(loggerName)
}