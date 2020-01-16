package com.akashbakshi

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.routing.*
import io.ktor.jackson.jackson
import io.ktor.features.ContentNegotiation

fun main(args: Array<String>){
    embeddedServer(Netty,commandLineEnvironment(System.getenv("PORT")?.toInt() ?:args)).start(wait=true) // get heroku system env variable for the PORT if it's not found default to application.conf port we specified
}

fun Application.module(){

    install(ContentNegotiation) {
        jackson {
            Compiler.enable()
        }
    }

    routing{
        habits()
    }
}