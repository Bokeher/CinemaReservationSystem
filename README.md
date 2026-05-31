# Cinema Reservation System

A REST API for a cinema reservation system built with Spring Boot. The application provides functionality for 
browsing movies, managing screenings, and reserving seats in cinema rooms.

The system is built using a layered architecture (Controller / Service / Repository) with JWT-based authentication 
and role-based access control (USER / ADMIN). It uses a relational MySQL database to model movies, screenings, 
rooms, seats, and reservations, ensuring consistency of seat availability across concurrent requests.

## Tech stack
- Java 17
- Spring Boot 3.5.14
- Spring Web (REST API)
- Spring Data JPA (Hibernate)
- Spring Security (JWT)
- MySQL
- Maven
- JUnit / Spring Boot Test
- Testcontainers (integration testing)
- Lombok
- Docker

## How to run

### Docker (recommended)

#### Requirements

- Docker
- Docker Compose

#### Steps

1. Create Docker configuration file:

Windows (cmd)
```cmd
copy src\main\resources\application-docker.properties.example src\main\resources\application-docker.properties
```

Linux / macOS / Git Bash / WSL
```bash
cp src/main/resources/application-docker.properties.example src/main/resources/application-docker.properties
```

2. Run application:

```bash
docker compose up --build
```

3. API is available at: http://localhost:8080

### Locally

#### Requirements

- Java 17
- Maven (or Maven Wrapper included)
- MySQL 8+

#### Steps

1. Start MySQL server

2. Create database with name 'cinema'

3. Create configuration file based on ``.example``:

Windows (cmd)
```cmd
copy src\main\resources\application-local.properties.example src\main\resources\application-local.properties
```

Linux / macOS / Git Bash / WSL
```bash
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

4. Run application
```bash
./mvnw spring-boot:run
```

5. API is available at: http://localhost:8080

## API Endpoints

### Authentication

| Method | Endpoint       | Access | Success | Description       |
|--------|----------------|--------|---------|-------------------|
| POST   | /auth/register | Public | 201     | User registration |
| POST   | /auth/login    | Public | 200     | User login        |

### Movies

| Method | Endpoint           | Access | Success | Description                                 |
|--------|--------------------|--------|---------|---------------------------------------------|
| GET    | /movies            | Public | 200     | List all movies (optional query: `?title=`) |
| GET    | /movies/{id}       | Public | 200     | Get movie details                           |
| POST   | /admin/movies      | ADMIN  | 201     | Add a new movie                             |
| PATCH  | /admin/movies/{id} | ADMIN  | 200     | Update movie                                |
| DELETE | /admin/movies/{id} | ADMIN  | 204     | Delete movie                                |

### Screenings

| Method | Endpoint                  | Access | Success | Description                  |
|--------|---------------------------|--------|---------|------------------------------|
| GET    | /screenings               | Public | 200     | List all screenings          |
| GET    | /screenings/{id}          | Public | 200     | Get screening details        |
| GET    | /screenings/{id}/seat-map | Public | 200     | Get seat map for a screening |
| POST   | /admin/screenings         | ADMIN  | 201     | Create a new screening       |
| PATCH  | /admin/screenings/{id}    | ADMIN  | 200     | Update screening             |
| DELETE | /admin/screenings/{id}    | ADMIN  | 204     | Delete screening             |

### Rooms

| Method | Endpoint          | Access        | Success | Description       |
|--------|-------------------|---------------|---------|-------------------|
| GET    | /rooms/{id}       | Authenticated | 200     | Get room details  |
| GET    | /admin/rooms      | ADMIN         | 200     | List all rooms    |
| POST   | /admin/rooms      | ADMIN         | 201     | Create a new room |
| DELETE | /admin/rooms/{id} | ADMIN         | 204     | Delete room       |

### Reservations

| Method | Endpoint                   | Access        | Success | Description                     |
|--------|----------------------------|---------------|---------|---------------------------------|
| GET    | /reservations              | Authenticated | 200     | Get current user's reservations |
| GET    | /reservations/{id}         | Authenticated | 200     | Get reservation details         |
| POST   | /reservations              | Authenticated | 201     | Create a reservation            |
| DELETE | /reservations/{id}         | Authenticated | 204     | Cancel reservation              |
| PATCH  | /reservations/{id}/confirm | Authenticated | 204     | Confirm reservation             |

### User

| Method | Endpoint          | Access        | Success | Description                              |
|--------|-------------------|---------------|---------|------------------------------------------|
| GET    | /me               | Authenticated | 200     | Get current user profile                 |
| GET    | /admin/users      | ADMIN         | 200     | Get users (optional query: `?username=`) |
| GET    | /admin/users/{id} | ADMIN         | 200     | Get user by ID                           |
| POST   | /admin/users      | ADMIN         | 201     | Create user                              |
| PATCH  | /admin/users/{id} | ADMIN         | 200     | Update user                              |
| DELETE | /admin/users/{id} | ADMIN         | 204     | Delete user                              |

## Database schema

The database schema describes the core domain model of the application, including users, movies, screenings, rooms, seats, and reservations, along with their relationships and constraints.

### Entity-Relationship diagram

<img width="900" alt="image" src="https://github.com/user-attachments/assets/5352f4c6-faff-44ce-821d-27a0a5b71b3a" />

### Additional Resources

- [Interactive Diagram](https://dbdocs.io/rychter47/CinemaReservationSystem?view=relationships)
- [DBML schema](docs/schema.dbml)

> **Why are both `active` and `ReservationStatus` used in the Reservation table?**
>
> MySQL does not support partial unique constraints, which makes it difficult to enforce uniqueness only for active reservations while still keeping historical (cancelled) records.
>
> To solve this, the system uses a combination of `ReservationStatus` and an `active` flag. Only active reservations are considered when enforcing uniqueness constraints for seat reservations, while cancelled reservations remain stored in the database and do not block rebooking of the same seat.

## Main Features
- User registration
- Login
- Seat reservation
- Browsing movies
- Browsing screenings
- Viewing seats for a given screening
- Confirm or cancel reservation

## Security Features
- JWT authentication
- Password hashing (bcrypt)
- Role-based access control (USER / ADMIN)
- Protection against SQL Injection via Hibernate
- Protection against XSS using Content Security Policy (cannot be fully solved on backend side, frontend is also required)

## Business logic
- Preventing race conditions during seat reservation
- Preventing reservation of seats that do not belong to a screening
- Preventing reservation of already occupied seats
- Allowing cancelled reservations to remain in database and be rebookable
- Preventing confirmation of cancelled reservations

