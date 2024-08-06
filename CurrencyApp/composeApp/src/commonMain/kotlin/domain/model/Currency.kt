package domain.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

@Serializable
open class Currency : RealmObject {

    @PrimaryKey
    var _id: ObjectId = ObjectId()

    @SerialName("code")
    var code: String = ""

    @SerialName("value")
    var value: Double = 0.0

    // Workaround for a compatibility bug in the K2 compiler with realm.
    // See:
    // https://github.com/realm/realm-kotlin/issues/1567
    // https://youtrack.jetbrains.com/issue/KT-62194
    companion object
}
