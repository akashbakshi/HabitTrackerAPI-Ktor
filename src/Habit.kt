package com.akashbakshi

import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import java.util.UUID

data class Habit(val id:UUID = UUID.randomUUID(), val name:String, val author:String)

var habits = mutableListOf<Habit>()

fun Routing.habits(){
    route("/habit"){
        get("/"){
            call.respond(habits)
        }
        post("/"){
            val tmpHabit = call.receive<Habit>()
            habits.add(tmpHabit)
            call.respond(tmpHabit)
        }
    }
}
