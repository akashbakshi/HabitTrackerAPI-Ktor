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

    embeddedServer(Netty,commandLineEnvironment(args)).start(wait=true) // Start a Netty Server, and use our applicaiton.conf file to fill the rest of the server info like PORt and modules
}

fun Application.module(){

    install(ContentNegotiation) { // use ContentNegotiation similar to import or require statement in nodejs
        jackson {//use jackson JSON library so we can parse incoming requests as JSON
            Compiler.enable()
        }
    }

    routing{//all routing for all handled inside here
        habits() // use the habits() function inside Habits.kt and use the routes defined inside that
    }
}