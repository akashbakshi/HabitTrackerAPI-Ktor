package com.akashbakshi.model

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table


//data class used to map our Habit Exposed object to this data class
data class HabitDC(val id:Int?,val name:String?,val author:String?)


// our object used for Expose ORM database operations
object Habit: Table("habits") {
    override val primaryKey =  PrimaryKey(integer("id").autoIncrement())
    val name = varchar("name",50)
    val author = varchar("author",255)
}

// extension of ResultRow to Map Habit to our HabitDC
fun ResultRow.toHabitDC() = HabitDC(
    id =  this[Habit.primaryKey],
    name = this[Habit.name],
    author = this[Habit.author]
)

//Convert the Primary Key to integer
private operator fun ResultRow.get(primaryKey: Table.PrimaryKey): Int? = primaryKey.columns.firstOrNull()?.indexInPK
