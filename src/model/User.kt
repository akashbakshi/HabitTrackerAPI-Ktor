package com.akashbakshi.model

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

data class UserDC(val username:String?,val email:String?,var password:String)

object User: Table("users"){
    val username = varchar("username",512).uniqueIndex()
    val email = varchar("email",512).uniqueIndex()
    val password = varchar("password",512)
}

fun ResultRow.toUserDC():UserDC = UserDC(
    username = this[User.username],
    email = this[User.email],
    password = this[User.password]
)