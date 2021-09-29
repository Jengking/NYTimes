package my.nytimes.app.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Articles(
    val status: String,
    val copyright: String,
    val num_results: Int,
    val results: List<Results> = listOf()
)

data class Results(
    val uri: String,
    val url: String,
    val source: String,
    val published_date: String,
    val updated: String,
    val section: String,
    val subsection: String,
    val byline: String,
    val title: String,
    val  abstract: String,
    val media: List<Media> = listOf()
)

data class Media(
    val type: String,
    val subtype: String,
    val caption: String,
    val copyright: String,
    @Json(name = "media-metadata") val metadata: List<MetaData> = listOf()
)

data class MetaData(
    val url: String,
    val format: String,
    val height: Int,
    val width: Int
)
