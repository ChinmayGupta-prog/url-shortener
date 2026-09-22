# URL Shortener

A Java and Spring Boot service for turning long URLs into short links. The project is being built around PostgreSQL for durable storage and Redis for fast redirects and request limiting.

## Planned capabilities

- Create a short code for a long URL using Base62 encoding.
- Redirect short links to their original URLs.
- Cache frequently used redirects in Redis.
- Limit requests by IP with a token bucket.
- Track click counts.

The repository currently contains the application foundation, local infrastructure, and a database-backed URL model. URL creation and redirect endpoints are not implemented yet.

## Tech stack

| Component | Technology |
| --- | --- |
| Runtime | Java 21 |
| Framework | Spring Boot 3.5.16 |
| Build | Maven |
| Database | PostgreSQL 16, Spring Data JPA, Flyway |
| Cache | Redis 7, Spring Data Redis |
| Tests | JUnit 5, Spring Boot Test |
| Local services | Docker Compose |

## Run locally

Install Java 21, Maven, and Docker with Docker Compose. From the repository root, run:

```powershell
docker compose up -d --wait
mvn test
mvn spring-boot:run
```

The application starts at `http://localhost:8081`. PostgreSQL is available on `localhost:5434`, and Redis on `localhost:6380`. These host ports avoid conflicts with common local defaults. The services remain running after the application stops; use `docker compose down` to stop them. The PostgreSQL data stays in a Docker volume.

The integration tests start the Spring application, check PostgreSQL and Redis connectivity, and verify that a URL can be saved and found by its short code. Start the Compose services before running `mvn test`.

## Configuration

The defaults work with the included Compose file. Set environment variables when connecting to different services or changing ports.

| Variable | Default | Purpose |
| --- | --- | --- |
| `SERVER_PORT` | `8081` | Application HTTP port |
| `DB_URL` | `jdbc:postgresql://localhost:5434/url_shortener` | JDBC connection URL |
| `POSTGRES_USER` | `url_shortener` | Database username |
| `POSTGRES_PASSWORD` | `url_shortener` | Database password |
| `REDIS_HOST` | `localhost` | Redis host |
| `REDIS_PORT` | `6380` | Redis port used by the application and published by Compose |
| `REDIS_PASSWORD` | empty | Redis password, if required |

Compose also accepts `POSTGRES_DB` and `POSTGRES_PORT` (defaults: `url_shortener` and `5434`). If you change either, set `DB_URL` to match. The default database credentials are intended for local development.

## Project layout

```text
src/main/java/       Spring Boot application
src/main/resources/  Application configuration and Flyway migrations
src/test/java/       Integration tests
docker-compose.yml   Local PostgreSQL and Redis
```
