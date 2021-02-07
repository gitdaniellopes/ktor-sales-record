package daniel.lopes.co.routes

import daniel.lopes.co.data.getSalesForUser
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.response.*
import io.ktor.routing.*

fun Route.saleRoutes() {
    route("/getSales") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val sales = getSalesForUser(email)
                call.respond(OK, sales)
            }
        }
    }
}