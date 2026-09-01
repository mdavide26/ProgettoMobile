# MyFilms

App Android per scoprire, salvare e organizzare i propri film preferiti.

Progetto per il corso di Programmazione di Sistemi Mobile — A.A. 2025/2026

**Autori:** Vincent Rey Ramos, Davide Magyari

## Setup del progetto

1. Clona il repository
2. Crea un file `local.properties` nella root del progetto (allo stesso livello di `settings.gradle.kts`) con il seguente contenuto:

```properties
   TMDB_API_TOKEN=<il tuo Read Access Token TMDb>
```

   Il token si ottiene registrando un account su [themoviedb.org](https://www.themoviedb.org/settings/api) e richiedendo una API key — verrà generato automaticamente anche il Read Access Token (v4), che è quello da usare qui.

3. Apri il progetto in Android Studio, lascia sincronizzare Gradle
4. Esegui l'app su un emulatore o dispositivo fisico (minSdk 26)
