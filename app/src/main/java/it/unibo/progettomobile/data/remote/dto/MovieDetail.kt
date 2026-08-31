package it.unibo.progettomobile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GenreDTO(
    val id: Int,
    val name: String
)

@Serializable
data class MovieDetailDTO(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String? = null,
    val vote_average: Double = 0.0,
    val genres: List<GenreDTO> = emptyList()
)