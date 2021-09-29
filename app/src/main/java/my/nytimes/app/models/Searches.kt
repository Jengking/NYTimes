package my.nytimes.app.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Searches(
    val status: String,
    val copyright: String,
    val response: Response
)

data class Response(
    val docs: List<Docs> = listOf()
)

data class Docs(
    val abstract: String,
    val web_url: String,
    val snippet: String,
    val multimedia: List<Multimedia> = listOf(),
    val pub_date: String,
    val headline: Headlines
)

data class Headlines(
    val main:String
)

data class Multimedia(
    val type: String,
    val url: String,
    val height: Int,
    val width: Int
)
