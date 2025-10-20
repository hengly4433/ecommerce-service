# E-commerce Service

- Java 21, Spring Boot 3.5.6
- Postgres 16, Flyway, JPA, JWT (jjwt 0.12.6), MapStruct 1.6.x, Springdoc OpenAPI
- Google Cloud Storage for Firebase bucket uploads

## Run

1) `docker compose up -d` (Postgres)
2) Configure `application.yml` (GCS bucket & credentials).
3) `mvn spring-boot:run`
4) Swagger: `http://localhost:8080/swagger`

Default seeded admin: `admin@local` / `password` (change hash in Flyway for prod).

## Image Uploads

- Profile image (users handled via dedicated endpoint you may add)
- Product main image: `POST /api/products/{id}/main-image` (`multipart/form-data` with `file`)
- Product gallery: `POST /api/products/{id}/gallery` (`multipart/form-data` with `files`)
