package com.github.stephenWanjala

import com.github.stephenWanjala.data.repositoryImpl.AuthRepositoryImpl
import com.github.stephenWanjala.plugins.configureMonitoring
import com.github.stephenWanjala.plugins.configureRouting
import com.github.stephenWanjala.plugins.configureSecurity
import com.github.stephenWanjala.plugins.configureSerialization
import com.github.stephenWanjala.security.JwtService
import com.github.stephenWanjala.security.hashing.SHA256HashingService
import com.github.stephenWanjala.security.token.TokenConfig
import io.ktor.server.application.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val dbName = "ktor_auth"
    val mongoPassword = System.getenv("mongoPassword")
    val db =
        KMongo.createClient(connectionString = "mongodb+srv://stephenwanjala145:$mongoPassword@cluster0.eobhrxp.mongodb.net/")
            .coroutine.getDatabase(name = dbName)
    val authSource = AuthRepositoryImpl(db)
    val tokenService = JwtService()
    val hashingService = SHA256HashingService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    configureSecurity(tokenConfig)
    configureMonitoring()
    configureSerialization()
    configureRouting(
        hashingService = hashingService,
        authRepository = authSource,
        tokenService = tokenService, tokenConfig = tokenConfig
    )
}
