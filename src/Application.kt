package com.akashbakshi

import com.akashbakshi.model.Habit
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.jackson.jackson
import io.ktor.features.ContentNegotiation
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import kotlin.system.exitProcess



val Application.envKind get() = environment.config.property("ktor.environment").getString()
val Application.dbCred get() = environment.config.property("ktor.db")


fun main(args: Array<String>){
    embeddedServer(Netty,commandLineEnvironment(args)).start(wait=true) // Start a Netty Server, and use our application.conf file to fill the rest of the server info like PORt and modules
}

fun Application.mainModule(){

    install(ContentNegotiation) { // use ContentNegotiation similar to import or require statement in nodejs
        jackson {//use jackson JSON library so we can parse incoming requests as JSON
            Compiler.enable()
        }
    }

    val url = GetDatabaseURL()

    if(url.isNullOrEmpty())
        exitProcess(1)

    Database.connect(url,driver = "com.mysql.cj.jdbc.Driver")


}

/*
* Author: Akash Bakshi
*
 */
fun Application.GetDatabaseURL(): String?{

    if (envKind == "dev")
        return "jdbc:mysql://habitsAdmin:app@localhost:3306/habitsDB"
    else if (envKind == "prod")
        return null
    else
        return null
}

