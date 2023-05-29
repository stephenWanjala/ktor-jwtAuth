package com.github.stephenWanjala.data.repositoryImpl

import com.github.stephenWanjala.domain.models.User
import com.github.stephenWanjala.domain.repository.AuthRepository
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class AuthRepositoryImpl
    (db: CoroutineDatabase) : AuthRepository {
    private val users = db.getCollection<User>()
    override suspend fun findUserByUsername(username: String): User? =
        users.findOne(User::username eq username)


    override suspend fun createUser(user: User): Boolean = users.insertOne(user).wasAcknowledged()
}