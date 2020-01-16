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
    embeddedServer(Netty,2020,module=Application::module).start(wait=true)
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