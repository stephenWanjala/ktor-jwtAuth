package com.github.stephenWanjala.routes

import com.github.stephenWanjala.data.requests.AuthRequest
import com.github.stephenWanjala.data.responses.AuthResponse
import com.github.stephenWanjala.domain.models.User
import com.github.stephenWanjala.domain.repository.AuthRepository
import com.github.stephenWanjala.security.TokenService
import com.github.stephenWanjala.security.hashing.HashingService
import com.github.stephenWanjala.security.hashing.SaltedHash
import com.github.stephenWanjala.security.token.TokenClaim
import com.github.stephenWanjala.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    authRepository: AuthRepository
) {
    post("signup") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val fieldsBlank = request.userName.isBlank() || request.password.isBlank()
        val tooShortPassword = request.password.length < 6

        if (fieldsBlank) {
            call.respond(HttpStatusCode.Conflict, message = "Fields required")
            return@post
        }

        if (tooShortPassword) {
            call.respond(HttpStatusCode.Conflict, message = "Password too short")
            return@post
        }
        val saltedHash = hashingService.generateHash(request.password)
        val user = User(
            userName = request.userName,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )

        val wasAcknowledged = authRepository.createUser(user = user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict, message = "Error Creating the user")
            return@post
        }

        call.respond(HttpStatusCode.OK)

    }
}


fun Route.signIn(
    authRepository: AuthRepository,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signin") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val fieldsBlank = request.userName.isBlank() || request.password.isBlank()

        if (fieldsBlank) {
            call.respond(HttpStatusCode.Conflict, message = "Fields required")
            return@post
        }

        val user = authRepository.findUserByUsername(request.userName)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, message = "Incorrect Username or Password")
            return@post
        }

        val isValidPassword = hashingService.verify(
            plainText = request.password, saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, message = "Incorrect Username or Password")
            return@post
        }
        val token = tokenService.generateToken(
            config = tokenConfig,
            TokenClaim(name = "userId", value = user.id.toString())
        )

        call.respond(status = HttpStatusCode.OK, message = AuthResponse(token = token))

    }


}


fun Route.authenticate() {
    authenticate {
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}


fun Route.home() {
    authenticate {
        get("home") {
            val principal = call.principal<JWTPrincipal>()
            val claim = principal?.getListClaim("userId", String::class)
            call.respond(HttpStatusCode.OK, message = "Your UserId is $claim")
        }
    }
}