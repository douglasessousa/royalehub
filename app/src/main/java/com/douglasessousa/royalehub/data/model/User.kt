package com.douglasessousa.royalehub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class User(
    @PrimaryKey
    val id: String,
    val nickname: String,
    val avatarUrl: String,
)