package daniel.lopes.co.routes

import daniel.lopes.co.data.checkPasswordForEmail
import daniel.lopes.co.data.requests.AccountRequest
import daniel.lopes.co.data.responses.SimpleResponse
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


fun Route.loginRoute() {

    route("/login") {
        post {
            val request = try {
                call.receive<AccountRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(BadRequest)
                return@post
            }

            val isPasswordCorrect =
                checkPasswordForEmail(request.email, request.password)
            if (isPasswordCorrect) {
                call.respond(OK, SimpleResponse(true, "Agora você está logado"))
            } else {
                call.respond(OK, SimpleResponse(false, "O e-mail ou senha estão incorretos"))
            }
        }
    }

}