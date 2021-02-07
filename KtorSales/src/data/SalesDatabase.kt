package daniel.lopes.co.data

import daniel.lopes.co.data.collections.Sales
import daniel.lopes.co.data.collections.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo


private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("SalesDatabase")
private val users = database.getCollection<User>()
private val sales = database.getCollection<Sales>()

suspend fun registerUser(user: User): Boolean {
    return users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExists(email: String): Boolean {
    return users.findOne(User::email eq email) != null
}

//verifica se a senha bate com o email cadastrado
suspend fun checkPasswordForEmail(email: String, passwordToCheck: String): Boolean{
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return actualPassword == passwordToCheck
}
