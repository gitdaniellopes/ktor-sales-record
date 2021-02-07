package daniel.lopes.co.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Sale(
    val productCode: String,
    val nameProduct: String,
    val soldAmount: Long,
    val unitaryValue: Double,
    val date: Long,
    val owners: List<String>,
    @BsonId
    val id: String = ObjectId().toString()
)
