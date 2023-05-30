package org.sirekanyan.`fun`.server

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test
import org.sirekanyan.`fun`.server.model.Hello

private const val HOST = "localhost"
private const val PORT = 8888
private val client = HttpClient {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
}

class MainTest {

    @Test
    fun echoPingPong() {
        runBlocking {
            client.webSocket(HttpMethod.Get, HOST, PORT, "echo") {
                sendSerialized("ping")
                val message = receiveDeserialized<String>()
                assertEquals("pong", message)
            }
        }
    }

    @Test
    fun syncHello() {
        runBlocking {
            launch {
                client.webSocket(HttpMethod.Get, HOST, PORT, "sync") {
                    sendSerialized("123")
                    assertEquals(Hello("777", "123", "How are you?"), receiveDeserialized<Hello>())
                    sendSerialized(Hello("123", "777", "I'm fine."))
                    sendSerialized(Unit)
                }
            }
            delay(1000)
            launch {
                client.webSocket(HttpMethod.Get, HOST, PORT, "sync") {
                    sendSerialized("777")
                    sendSerialized(Hello("777", "123", "How are you?"))
                    assertEquals(Hello("123", "777", "I'm fine."), receiveDeserialized<Hello>())
                    sendSerialized(Unit)
                }
            }
        }
    }

}
