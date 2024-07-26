package domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
