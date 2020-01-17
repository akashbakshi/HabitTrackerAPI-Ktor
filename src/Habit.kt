package com.akashbakshi

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.*
import io.ktor.response.*
import java.util.UUID

data class Habit(val id:UUID = UUID.randomUUID(), val name:String, val author:String)

var habits = mutableListOf<Habit>()

fun Routing.habits(){ // function use to handle our routing for all habit related endpoints
    route("/habit"){ // use prefix
        get("/"){
            call.respond(HttpStatusCode.OK,habits) // response with all our habit elements
        }
        post("/"){
            val tmpHabit = call.receive<Habit>() // parse the incoming request to our data class
            habits.add(tmpHabit) // add it to our array/ db later on
            call.respond(HttpStatusCode.Created,tmpHabit) // send response back to user with Created and the element they just added
        }
    }
}
