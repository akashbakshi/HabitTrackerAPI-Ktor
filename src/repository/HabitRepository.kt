package com.akashbakshi.repository

import com.akashbakshi.model.Habit
import com.akashbakshi.model.HabitDC
import com.akashbakshi.model.toHabitDC
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


/*
* will retrieve all records in Habit table and map it to our HabitDC model
*/
val Application.allHabits: List<HabitDC> get() =  transaction { Habit.selectAll().map { it.toHabitDC() } }

/*
* will retrieve single record by the ID in Habit table and map it to our HabitDC model
*/
fun Application.getHabitById(id:Int): HabitDC?{
    var habitToFind: HabitDC? = null
    transaction {
        habitToFind = Habit.select { Habit.id eq id}.firstOrNull()?.toHabitDC()

    }
    return habitToFind
}

/*
* will create a new record in the Habit table
*/
fun Application.addHabits(habit: HabitDC) {
    transaction {
        Habit.insert {
            it[name] = habit.name!!
            it[author] = habit.author!!
        }
    }
}

/*
* Parameter(s):
* id: The id of the record we would like to update
* reqHabit: The data class that holds the information parsed from the request body
*
* Used to update records in the database
*/

fun Application.updateHabit(id:Int,reqHabit:HabitDC):HabitDC?{


    val habitToUpdate = getHabitById(id) ?: run {
        log.error("Couldn't get habit with ID $id")
        return null
    } // finally attempt to get the record by it's ID

    transaction {
        // Only update if record if it isn't empty or null and if it isn't equal to the current value in database
        if(!reqHabit.name.isNullOrBlank() && !reqHabit.name.equals(habitToUpdate.name))
       {
           Habit.update({Habit.id eq habitToUpdate.id!!}){
               it[name] = reqHabit.name!!
           }
       }

        // Only update if property isn't empty or null and if it isn't equal to the current value in database
        if(!reqHabit.author.isNullOrBlank() && !reqHabit.author.equals(habitToUpdate.author)){
            Habit.update({Habit.id eq habitToUpdate.id!!}){
                it[author] = reqHabit.author!!
            }
        }
    }
    return getHabitById(id)
}
