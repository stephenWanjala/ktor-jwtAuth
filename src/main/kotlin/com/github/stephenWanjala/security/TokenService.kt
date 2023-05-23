package com.github.stephenWanjala.security

import com.github.stephenWanjala.security.token.TokenClaim
import com.github.stephenWanjala.security.token.TokenConfig

interface TokenService {
    fun generateToken(
        config: TokenConfig,
        vararg claims: TokenClaim
    ): String

}