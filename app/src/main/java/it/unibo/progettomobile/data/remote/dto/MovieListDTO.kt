package it.unibo.progettomobile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieListDTO(
    val page: Int,
    val results: List<MovieDTO>
)
