package domain.model

import io.realm.kotlin.ext.isValid
import io.realm.kotlin.ext.version
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

@Serializable
open class Currency : RealmObject {

    @PrimaryKey
    @Suppress("unused", "PropertyName")
    var _id: ObjectId = ObjectId()

    @SerialName("code")
    var code: String = ""

    @SerialName("value")
    var value: Double = 0.0

    override fun toString(): String =
            "Currency(code='$code', value='$value' isValid=${isValid()})"

    // Workaround for a compatibility bug in the K2 compiler with realm.
    // See:
    // https://github.com/realm/realm-kotlin/issues/1567
    // https://youtrack.jetbrains.com/issue/KT-62194
    companion object
}
