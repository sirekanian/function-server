@file:JvmName("Main")

package org.sirekanyan.`fun`.server

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.util.concurrent.ConcurrentHashMap

private val sessions = ConcurrentHashMap<String, WebSocketServerSession>()

fun main() {
    embeddedServer(Netty, port = 8888) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
        routing {
            get("count") {
                call.respondText(sessions.size.toString())
            }
            webSocket("echo") {
                val message = receiveDeserialized<String>()
                println("Received '$message' from client")
                sendSerialized("pong")
            }
            webSocket("sync") {
                sessions.forEach { (id, session) ->
                    if (!session.isActive) {
                        sessions.remove(id)
                    }
                }
                val id = receiveDeserialized<String>()
                sessions[id] = this
                val json = receiveDeserialized<JsonObject>()
                val to = checkNotNull(json.getValue("to").jsonPrimitive.contentOrNull)
                sessions.getValue(to).sendSerialized(json)
                receiveDeserialized<Unit>()
                sessions.remove(id)
            }
        }
    }.start(wait = true)
}
