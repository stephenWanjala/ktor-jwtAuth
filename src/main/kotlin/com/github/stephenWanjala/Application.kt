package com.github.stephenWanjala

import com.github.stephenWanjala.data.repositoryImpl.AuthRepositoryImpl
import io.ktor.server.application.*
import com.github.stephenWanjala.plugins.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val dbName = "ktor_auth"
    val mongoPassword = System.getenv("mongoPassword")
    val db = KMongo.createClient(connectionString = "mongodb+srv://stephenwanjala145:$mongoPassword@cluster0.eobhrxp.mongodb.net/?retryWrites=true&w=majority")
        .coroutine.getDatabase(name = dbName)
    val authSource =AuthRepositoryImpl(db)
    configureSecurity()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}
