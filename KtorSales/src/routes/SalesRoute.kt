package daniel.lopes.co.routes

import daniel.lopes.co.data.collections.Sale
import daniel.lopes.co.data.deleteSaleForUser
import daniel.lopes.co.data.getSalesForUser
import daniel.lopes.co.data.requests.DeleteSaleRequest
import daniel.lopes.co.data.saveSale
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
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

    route("/deleteSale") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<DeleteSaleRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }

                if (deleteSaleForUser(email, request.id)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }

    route("/addSale") {
        authenticate {
            post {
                val sale = try {
                    call.receive<Sale>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }

                if (saveSale(sale)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }
}
