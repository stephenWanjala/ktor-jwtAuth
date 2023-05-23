package com.github.stephenWanjala.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

class SHA256HashingService : HashingService {
    override fun generateHash(plainText: String, saltLength: Int): SaltedHash {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLength)
        val saltAsHex = Hex.encodeHexString(salt)
        val hash = DigestUtils.sha256Hex("$salt$plainText")
        return SaltedHash(hash = hash, salt = saltAsHex)

    }

    override fun verify(plainText: String, saltedHash: SaltedHash): Boolean =
        DigestUtils.sha256Hex(saltedHash.salt + plainText) == saltedHash.hash
}