package de.tdng2011.game.library

object EntityTypes extends Enumeration {
  val
    Player,               // 0
    Shot,                 // 1
    PowerUp,              // 2
    World,                // 3
    Handshake,            // 4
    Action,               // 5
    ScoreBoard,           // 6
    PlayerJoined,         // 7
    PlayerLeft,           // 8
    PlayerName,           // 9
    // NG Messages
    PlayerKilledEvent,    // 10
    PlayerCollisionEvent, // 11
    ShotCollisionEvent,   // 12
    PlayerSpawnedEvent,   // 13
    ShotSpawnedEvent      // 14
  = Value
}