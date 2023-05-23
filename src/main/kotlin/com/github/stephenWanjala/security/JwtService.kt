package com.github.stephenWanjala.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.github.stephenWanjala.security.token.TokenClaim
import com.github.stephenWanjala.security.token.TokenConfig
import java.util.*

class JwtService : TokenService {
    override fun generateToken(config: TokenConfig, vararg claims: TokenClaim): String =
        JWT.create()
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
            .withJWTId(config.secret)
            .apply {
                claims.forEach { claim ->
                    withClaim(claim.name, claim.value)
                }
            }
            .sign(Algorithm.HMAC256(config.secret))

}