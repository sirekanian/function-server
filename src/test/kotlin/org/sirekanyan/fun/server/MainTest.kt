package org.sirekanyan.`fun`.server

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

private val client = HttpClient {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
}

class MainTest {

    @Test
    fun echoPingPong() {
        runBlocking {
            client.webSocket(HttpMethod.Get, port = 8888, path = "echo") {
                sendSerialized("ping")
                val message = receiveDeserialized<String>()
                assertEquals("pong", message)
            }
        }
    }

}
