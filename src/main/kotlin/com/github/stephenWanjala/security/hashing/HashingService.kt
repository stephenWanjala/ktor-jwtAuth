package com.github.stephenWanjala.security.hashing

interface HashingService {
    fun generateHash(plainText: String, saltLength: Int): SaltedHash
    fun verify(plainText: String, saltedHash: SaltedHash): Boolean
}