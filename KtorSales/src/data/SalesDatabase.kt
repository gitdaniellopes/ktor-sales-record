package daniel.lopes.co.data

import daniel.lopes.co.data.collections.Sale
import daniel.lopes.co.data.collections.User
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue


private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("SalesDatabase")
private val users = database.getCollection<User>()
private val sales = database.getCollection<Sale>()

suspend fun registerUser(user: User): Boolean {
    return users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExists(email: String): Boolean {
    return users.findOne(User::email eq email) != null
}

//verifica se a senha bate com o email cadastrado
suspend fun checkPasswordForEmail(email: String, passwordToCheck: String): Boolean {
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return actualPassword == passwordToCheck
}

suspend fun getSalesForUser(email: String): List<Sale> {
    return sales.find(Sale::owners contains email).toList()
}

suspend fun isOwnerOfSale(saleID: String, owner: String): Boolean {
    val sale = sales.findOneById(saleID) ?: return false
    return owner in sale.owners
}

suspend fun addOwnerToSale(saleID: String, owner: String): Boolean {
    val owners = sales.findOneById(saleID)?.owners ?: return false
    return sales.updateOneById(saleID, setValue(Sale::owners, owners + owner)).wasAcknowledged()
}

suspend fun saveSale(sale: Sale): Boolean {
    val saleExists = sales.findOneById(sale.id) != null
    return if (saleExists) {
        sales.updateOneById(sale.id, sale).wasAcknowledged()
    } else {
        sales.insertOne(sale).wasAcknowledged()
    }
}

suspend fun deleteSaleForUser(email: String, saleID: String): Boolean {
    val sale = sales.findOne(Sale::id eq saleID, Sale::owners contains email)
    sale?.let { safeSale ->
        if (safeSale.owners.size > 1) {
            val newOwners = safeSale.owners - email
            val updateResult = sales.updateOne(
                Sale::id eq safeSale.id,
                setValue(Sale::owners, newOwners)
            )
            return updateResult.wasAcknowledged()
        }
        return sales.deleteOneById(safeSale.id).wasAcknowledged()
    } ?: return false
}
