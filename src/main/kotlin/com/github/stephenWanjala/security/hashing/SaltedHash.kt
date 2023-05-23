package com.github.stephenWanjala.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String,
)
