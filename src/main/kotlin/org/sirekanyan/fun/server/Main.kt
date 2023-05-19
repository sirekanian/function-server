@file:JvmName("Main")

package org.sirekanyan.`fun`.server

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json

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
        }
    }.start(wait = true)
}
