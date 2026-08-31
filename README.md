# Wikibook

Wikibook displays public bookcases in Berlin on an interactive map. The application
loads the locations from the German Wikipedia page
[Liste öffentlicher Bücherschränke in Berlin](https://de.wikipedia.org/wiki/Liste_%C3%B6ffentlicher_B%C3%BCcherschr%C3%A4nke_in_Berlin),
extracts their coordinates and details, and renders them with Leaflet and
OpenStreetMap tiles.

## Features

- Interactive Berlin map with a marker for each public bookcase
- Bookcase address, type, comments, and available photos in marker popups
- Links from each marker to Google Maps
- Optional browser geolocation to show the user's current position
- In-memory caching of scraped data for three hours

## Requirements

- Java 25
- Internet access at runtime, because the bookcase data and map assets are external

The project includes Gradle Wrapper scripts, so a separate Gradle installation is
not required.

## Run locally

Start the application with:

```bash
./gradlew run
```

Then open [http://localhost:8080/berlin](http://localhost:8080/berlin) in a browser.
The root URL `/` redirects to this endpoint.

The server listens on all interfaces (`0.0.0.0`) and port `8080` by default. The
port can be changed with Micronaut's standard environment variable:

```bash
MICRONAUT_SERVER_PORT=9090 ./gradlew run
```

The first request may take a little longer while the Wikipedia page is downloaded
and parsed. Later requests use the cache. The cache expires three hours after the
data is written.

## Docker

Build the JAR first, then build and run the container:

```bash
./gradlew shadowJar
docker build -t wikibook .
docker run --rm -p 8080:8080 wikibook
```

Open [http://localhost:8080/berlin](http://localhost:8080/berlin) after the
container starts. The root URL `/` redirects there as well.

## Project structure

```text
src/main/java/de/gumerbaev/
├── Application.java             # Micronaut application entry point
├── MapController.java           # HTTP endpoint
├── data/                        # Bookcase and coordinate records
└── model/WikiProcessor.java     # Wikipedia fetching, parsing, and caching
src/main/resources/
├── application.yml              # Server and cache configuration
├── images/                      # Map marker icons and favicon
└── views/map.html               # Thymeleaf map page
```

## Data and external services

The source page is fetched when the cache is empty, so changes to Wikipedia are
reflected after the cache expires or the application restarts. The map uses
OpenStreetMap tiles, while Tailwind CSS and Leaflet are loaded from CDNs by the
browser. Browser geolocation generally requires a secure context (HTTPS), except
for localhost.

Because the parser depends on the structure of the Wikipedia table, changes to
that page may require updates to `WikiProcessor`.
