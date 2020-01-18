package com.akashbakshi.route

import com.akashbakshi.model.*
import com.akashbakshi.model.HabitDC
import com.akashbakshi.repository.*
import io.ktor.application.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.routing.*
import io.ktor.response.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction


fun Application.habitsModule(){

    routing{
        route("/api/habit"){

            get("/"){

                call.respond(HttpStatusCode.OK,allHabits) // return all records from habit table
            }

            get("/{id}"){
                val id:Int? = call.parameters["id"]?.toIntOrNull()

                if(id == null){
                    val invalidId = call.parameters["id"]
                    call.respond(HttpStatusCode.NotFound,"Could not find record with ID:$invalidId")

                }else{
                    val habit = getHabitById(id)
                    if (habit != null) {
                        call.respond(HttpStatusCode.OK,habit)// return all records from habit table
                    }
                }

            }
            post("/"){
                val tmpHabit = call.receive<HabitDC>() // parse the request body to HabitDC

                if(tmpHabit.author.isNullOrBlank() || tmpHabit.name.isNullOrBlank()) // Check if any of the required fields are missing
                    call.respond(HttpStatusCode.BadRequest,"Author or name of habit missing") // return bad request if we're missing one
                else{
                    addHabits(tmpHabit) // add the record
                    call.respond(HttpStatusCode.OK,tmpHabit) // send the user the OK status with the record info
                }
            }

            put("/{id}"){
                val id = call.parameters["id"] // get the ID from the parameter
                if(id.isNullOrBlank())
                    call.respond(HttpStatusCode.BadRequest,"Please Provide an ID for the Habit you wish to update") // if the ID is missing then send appropriate status along with message
                else{
                    lateinit var tmpHabit:HabitDC
                    try {
                       tmpHabit =  call.receive<HabitDC>() // parse the request body and map it to our data class
                    }catch (err:Exception){
                        log.error("Error parsing request BODY as HabitDC")
                        call.respond(HttpStatusCode.BadRequest,"Request body is empty") // if we can't parse it for some reason (blank request) then let the user know
                    }

                    var habitId: Int? = -1

                    try{
                        habitId = id.toIntOrNull() // attempt to parse the id to int
                    }catch(err:Exception){
                        log.error(err.toString())
                        call.respond(HttpStatusCode.BadRequest,"Invalid ID Format, must be number") // if it's not an int or throws an error while attempting to parse let the user know and log the error
                    }

                    if(habitId == null) // make sure that our Id isn't null
                        call.respond(HttpStatusCode.BadRequest,"Invalid Id, enter number")
                    else{
                        val updatedHabitDC = updateHabit(habitId,tmpHabit) ?: call.respond(HttpStatusCode.InternalServerError,"Could Not Updated Habit with ID $habitId")
                        call.respond(HttpStatusCode.OK,updatedHabitDC)
                    }


                }
            }
        }
    }

    transaction {
        SchemaUtils.create(Habit)

    }
}