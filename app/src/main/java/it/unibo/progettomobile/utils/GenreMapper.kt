package it.unibo.progettomobile.utils

object GenreMapper {
    private val genreMap = mapOf(
        28 to "Azione", 12 to "Avventura", 16 to "Animazione",
        35 to "Commedia", 80 to "Crime", 99 to "Documentario",
        18 to "Drama", 10751 to "Famiglia", 14 to "Fantasy",
        36 to "Storia", 27 to "Horror", 10402 to "Musica",
        9648 to "Mistero", 10749 to "Romance", 878 to "Fantascienza",
        10770 to "TV Movie", 53 to "Thriller", 10752 to "Guerra",
        37 to "Western"
    )
    fun getName(id: Int?): String = genreMap[id] ?: "Sconosciuto"
}