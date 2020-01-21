package com.akashbakshi.repository

import com.akashbakshi.model.User
import com.akashbakshi.model.UserDC
import com.akashbakshi.model.toUserDC
import io.ktor.application.Application
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

/*
* Author: Akash Bakshi
* Purpose: To act as an data access layer for all User Object related queries and create functions that interacts and performs commonly used tasks
 */


fun Application.CreateUser(userToAdd:UserDC):Boolean{
    try {
        transaction {
            User.insert {
                it[username] = userToAdd.username!!
                it[email] = userToAdd.email!!
                it[password] = userToAdd.password
            }
        }
    }catch (err:Exception){
        return false
    }

    return true

}

fun Application.GetUserByUsername(username:String):UserDC?{
    return try{
        transaction {
            User.select{User.username eq username}.firstOrNull()?.toUserDC()
        }
    }catch (err:Exception){
        null
    }
}