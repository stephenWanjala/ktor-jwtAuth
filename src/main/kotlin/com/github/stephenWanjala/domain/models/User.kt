package com.github.stephenWanjala.domain.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    val userName: String,
    val password: String,
    val salt: String,
    @BsonId val id: ObjectId = ObjectId()
)
