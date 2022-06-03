package com.example.nexleinterview.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("adult") var adult: Boolean? = false,
    @SerialName("backdrop_path") val backdropPath: String? = "",
    @SerialName("genre_ids") var genreIds: MutableList<Int>? = mutableListOf(),
    @SerialName("id") var id: Int? = -1,
    @SerialName("original_language") var originalLanguage: String? = "",
    @SerialName("original_title") var originalTitle: String? = "",
    @SerialName("overview") var overview: String? = "",
    @SerialName("popularity") var popularity: Float? = 0f,
    @SerialName("poster_path") var posterPath: String? = "",
    @SerialName("release_date") var releaseDate: String? = "",
    @SerialName("title") var title: String? = "",
    @SerialName("video") var video: Boolean? = false,
    @SerialName("vote_average") var voteAverage: Float? = 0f,
    @SerialName("vote_count") var voteCount: Int? = -1
)
