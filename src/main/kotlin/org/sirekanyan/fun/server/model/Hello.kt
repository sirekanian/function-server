package org.sirekanyan.`fun`.server.model

import kotlinx.serialization.Serializable

@Serializable
data class Hello(val to: Int, val message: String)