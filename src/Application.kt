package com.akashbakshi

import com.akashbakshi.model.Habit
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.jackson.jackson
import io.ktor.features.ContentNegotiation
import io.ktor.response.respond
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import kotlin.system.exitProcess
import io.ktor.routing.*

@UseExperimental(io.ktor.util.KtorExperimentalAPI::class)
val Application.envKind get() = environment.config.property("ktor.environment").getString()


fun main(args: Array<String>){
    embeddedServer(Netty,commandLineEnvironment(args)).start(wait=true) // Start a Netty Server, and use our application.conf file to fill the rest of the server info like PORt and modules
}

/*
* Author: Akash Bakshi
* mainModule used to register jackson JSON parser and make the database connection
 */
fun Application.mainModule(){

    log.info("A) Application in main module")
    install(ContentNegotiation) {
        // use ContentNegotiation similar to import or require statement in nodejs
        jackson {
            //use jackson JSON library so we can parse incoming requests as JSON
            Compiler.enable()
        }
    }

    log.info("B) Attempting to Retrieve DB URL...")
    val url = GetDatabaseURL()

    if(url.isNullOrEmpty()) {
        log.error("No Connection string found, Now Exiting ...")
        exitProcess(1)
    }

    log.info("C) Attempting to Connect To DB with URL ($url)")
    Database.connect(url,driver = "com.mysql.cj.jdbc.Driver")

    log.info("D) Connected to Database!")

}

/*
* Author: Akash Bakshi
* Used to read the environment variable and return the URL of the database
 */

fun Application.GetDatabaseURL(): String?{

    if (envKind == "dev")
        return "jdbc:mysql://habitsAdmin:app@localhost:3306/habitsDB"
    else if (envKind == "prod")
        return System.getenv("DB_URL").toString() // get envrionment variable called DB_URL
    else
        return null
}

