package it.unibo.progettomobile.data.remote.dto

import kotlinx.serialization.Serializable

/*
* Classe che serve a prendere i dati dal JSON passati dalla chiamata API del sito
* */
@Serializable
data class MovieDTO(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String? = null,
    val vote_average: Double = 0.0
)
