package com.github.stephenWanjala.security.hashing

interface HashingService {
    fun generateHash(plainText: String, saltLength: Int=32): SaltedHash
    fun verify(plainText: String, saltedHash: SaltedHash): Boolean
}