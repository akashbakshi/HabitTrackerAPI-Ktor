package com.akashbakshi.route

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.routing.*
import io.ktor.response.*
import io.ktor.http.HttpStatusCode
import io.ktor.request.*

import com.akashbakshi.model.UserDC
import com.akashbakshi.model.User
import com.akashbakshi.repository.CreateUser
import com.akashbakshi.repository.GetUserByUsername
import org.jetbrains.exposed.sql.SchemaUtils

import org.mindrot.jbcrypt.*
import org.jetbrains.exposed.sql.transactions.*

/*
* Author: Akash Bakshi
* Purpose: Provide the routing and business logic for any user related requests
 */


fun Application.userModule(){

    routing{
        route("/api/user"){
            post("/"){
                val newUserDC = call.receive<UserDC>()
                newUserDC.password = BCrypt.hashpw(newUserDC.password,BCrypt.gensalt(12))

                if(!newUserDC.email.isNullOrBlank() && !newUserDC.username.isNullOrBlank() && !newUserDC.password.isNullOrBlank()){
                    val username = newUserDC.username

                    if(CreateUser(newUserDC))
                        call.respond(HttpStatusCode.Created,"Successfully created user $username")
                    else
                        call.respond(HttpStatusCode.InternalServerError,"Cannot create user in database")
                }else{

                    call.respond(HttpStatusCode.BadRequest,"Invalid properties provided for registration")
                }
            }

            post("/authenticate"){
                val userToAuthDC = call.receive<UserDC>()
                if(userToAuthDC.username.isNullOrBlank()){
                    call.respond(HttpStatusCode.BadRequest,"Please Provide the correct credentials")
                }else{
                    val retrievedUser = GetUserByUsername(userToAuthDC.username)
                    if(retrievedUser != null){

                        val isValidCredentials = BCrypt.checkpw(userToAuthDC.password,retrievedUser.password)

                        if(isValidCredentials)
                            call.respond(HttpStatusCode.OK,isValidCredentials)
                        else
                            call.respond(HttpStatusCode.Unauthorized,"Bad Username/Password combination")
                    }else{
                        call.respond(HttpStatusCode.Unauthorized,"Bad Username/Password combination")

                    }
                }
            }

            get("/"){
                call.respond("OK")
            }


            get("/{username}"){
                val userToFind = call.parameters["username"]
                if(userToFind.isNullOrBlank())
                    call.respond(HttpStatusCode.BadRequest,"Please provide a username")
                else
                {
                    val user = GetUserByUsername(userToFind)
                    if(user != null){
                        val email = user.email
                        call.respond(HttpStatusCode.OK,UserDC(username = user.username,email = user.email,password = ""))
                    }else{
                        call.respond(HttpStatusCode.NotFound,"Couldn't find user $userToFind")
                    }
                }



            }
        }

        transaction{
            SchemaUtils.create(User)
        }
    }

}