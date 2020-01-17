package com.akashbakshi.route

import com.akashbakshi.model.*
import com.akashbakshi.model.HabitDC
import io.ktor.application.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.routing.*
import io.ktor.response.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert



val Application.allHabits: List<HabitDC> get() =  transaction { Habit.selectAll().map { it.toHabitDC() } }
fun Application.addHabits(habit:HabitDC) {
    transaction {
        Habit.insert {
            it[name] = habit.name!!
            it[author] = habit.author!!
        }
    }
}
fun Application.habitsModule(){

    routing{
        route("/habit"){
            get("/"){

                call.respond(HttpStatusCode.OK,allHabits)
            }
            post("/"){
                val tmpHabit = call.receive<HabitDC>()

                if(tmpHabit.author.isNullOrEmpty() || tmpHabit.name.isNullOrEmpty())
                    call.respond(HttpStatusCode.BadRequest,"Author or name of habit missing")

                addHabits(tmpHabit)
                call.respond(HttpStatusCode.OK,tmpHabit)
            }
        }
    }

    transaction {
        SchemaUtils.create(Habit)

    }
}