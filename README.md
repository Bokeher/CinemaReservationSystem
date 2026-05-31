# Cinema Reservation System

## Project description
This project is a recruitment task. The goal is to implement a simple cinema reservation system as a REST API using Spring Boot.

The application allows users to browse movies, view screenings, and reserve seats.  

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

| Method | Endpoint | Access | Success | Description |
|--------|----------|--------|---------|-------------|
| GET | /reservations | Authenticated | 200 | Get current user's reservations |
| GET | /reservations/{id} | Authenticated | 200 | Get reservation details |
| POST | /reservations | Authenticated | 201 | Create a reservation |
| DELETE | /reservations/{id} | Authenticated | 204 | Cancel reservation |
| PATCH | /reservations/{id}/confirm | Authenticated | 204 | Confirm reservation |

### User

| Method | Endpoint | Access | Success | Description |
|--------|----------|--------|---------|-------------|
| GET | /me | Authenticated | 200 | Get current user profile |
| GET | /admin/users | ADMIN | 200 | Get users (optional query: `?username=`) |
| GET | /admin/users/{id} | ADMIN | 200 | Get user by ID |
| POST | /admin/users | ADMIN | 201 | Create user |
| PATCH | /admin/users/{id} | ADMIN | 200 | Update user |
| DELETE | /admin/users/{id} | ADMIN | 204 | Delete user |

## Planned functional requirements
- List available movies and screenings
- Display cinema hall layout with available and reserved seats
- Seat reservation for selected screenings
- Reservation management (confirm / cancel)

## Planned security features
- JWT authentication
- Password hashing 
- Protection against SQL Injection
- Protection against Cross-Site Scripting (XSS)
