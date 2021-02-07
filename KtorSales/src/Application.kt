package daniel.lopes.co

import daniel.lopes.co.data.checkPasswordForEmail
import daniel.lopes.co.routes.loginRoute
import daniel.lopes.co.routes.registerRoute
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication) {
        configureAuth()
    }

    install(Routing) {
        registerRoute()
        loginRoute()
    }
}

private fun Authentication.Configuration.configureAuth() {
    basic {
        realm = "Sales Server"
        validate { credentials ->
            val email = credentials.name
            val password = credentials.password
            if (checkPasswordForEmail(email, password)) {
                //Um usuário autenticado com sucesso em nosso servidor
                UserIdPrincipal(email)
            } else null
        }
    }
}

