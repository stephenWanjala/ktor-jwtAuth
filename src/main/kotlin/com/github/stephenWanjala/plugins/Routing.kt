package com.github.stephenWanjala.plugins

import com.github.stephenWanjala.domain.repository.AuthRepository
import com.github.stephenWanjala.routes.authenticate
import com.github.stephenWanjala.routes.home
import com.github.stephenWanjala.routes.signIn
import com.github.stephenWanjala.security.TokenService
import com.github.stephenWanjala.security.hashing.HashingService
import com.github.stephenWanjala.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    hashingService: HashingService,
    authRepository: AuthRepository,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        signIn(
            authRepository = authRepository, hashingService = hashingService,
            tokenService = tokenService, tokenConfig = tokenConfig
        )

        signIn(
            authRepository = authRepository,
            tokenService = tokenService,
            hashingService = hashingService,
            tokenConfig = tokenConfig
        )
        authenticate()
        home()
    }
}
