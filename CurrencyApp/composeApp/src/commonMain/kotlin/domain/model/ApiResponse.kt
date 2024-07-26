package domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

@Serializable
data class ApiResponse(

    @SerialName("meta")
    val meta: Metadata,

    @SerialName("data")
    val data: Map<String, Currency>
)

@Serializable
data class Metadata(

    @SerialName("last_updated_at")
    val lastUpdatedAt: String
)

@Serializable
open class Currency : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()

    @SerialName("code")
    var code: String = ""

    @SerialName("value")
    var value: Double = 0.0
}
