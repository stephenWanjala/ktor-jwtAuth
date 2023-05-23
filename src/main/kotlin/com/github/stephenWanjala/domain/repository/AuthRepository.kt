package com.github.stephenWanjala.domain.repository

import com.github.stephenWanjala.domain.models.User

interface AuthRepository {
    suspend fun findUserByUsername(username: String): User?
    suspend fun createUser(user: User): Boolean
}