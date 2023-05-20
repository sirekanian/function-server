@file:JvmName("Main")

package org.sirekanyan.`fun`.server

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import org.sirekanyan.`fun`.server.model.Hello
import java.util.concurrent.ConcurrentHashMap

private val sessions = ConcurrentHashMap<Int, WebSocketServerSession>()

fun main() {
    embeddedServer(Netty, port = 8888) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
        routing {
            webSocket("echo") {
                val message = receiveDeserialized<String>()
                println("Received '$message' from client")
                sendSerialized("pong")
            }
            webSocket("sync") {
                val id = receiveDeserialized<Int>()
                sessions[id] = this
                val hello = receiveDeserialized<Hello>()
                sessions.getValue(hello.to).sendSerialized(hello)
                receiveDeserialized<Unit>()
                sessions.remove(id)
            }
        }
    }.start(wait = true)
}
