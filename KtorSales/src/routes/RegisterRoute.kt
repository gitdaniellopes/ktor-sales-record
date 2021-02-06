package daniel.lopes.co.routes

import daniel.lopes.co.data.checkIfUserExists
import daniel.lopes.co.data.collections.User
import daniel.lopes.co.data.registerUser
import daniel.lopes.co.data.requests.AccountRequest
import daniel.lopes.co.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.registerRoute() {
    route("/register") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }

            val userExists = checkIfUserExists(request.email)
            if (!userExists) {
                if (registerUser(User(request.email, request.password))) {
                    call.respond(OK, SimpleResponse(true, "Conta criada com sucesso"))
                } else {
                    call.respond(OK, SimpleResponse(false, "Ocorreu um erro desconhecido"))
                }
            } else {
                call.respond(OK, SimpleResponse(false, "Um usuário com este email já existe"))
            }
        }
    }
}