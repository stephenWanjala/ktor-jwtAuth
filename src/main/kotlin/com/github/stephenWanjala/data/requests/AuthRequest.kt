package com.github.stephenWanjala.data.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val userName: String,
    val password: String
)
