package com.example.nexleinterview.data.model.response

import com.example.nexleinterview.data.model.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MoviePopularResponse(
    @SerialName("page" )var page: Int? = -1,
    @SerialName("results")var results: MutableList<Movie>? = mutableListOf(),
    @SerialName("total_pages")var totalPages: Int? = -1,
    @SerialName("total_results")var totalResults: Int? = -1
)
