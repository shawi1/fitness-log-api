# Fitness Log API

A REST API for tracking workout sessions, built with Spring Boot, MongoDB, and JWT authentication.

## Team Information

| Name | CWID |
|------|------|
| [Your Name] | [Your CWID] |

## Project Overview

Users register and log in to receive a JWT token. All workout endpoints are protected — only authenticated users can create, read, update, or delete their own workout sessions. Data isolation is enforced: a user can never access another user's workouts.

**Entities:** `User` (one) → `WorkoutSession` (many) — one-to-many relationship

## Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running
- [MongoDB Community](https://www.mongodb.com/try/download/community) running locally on port `27017`
- [Postman](https://www.postman.com/) for API testing

## Build the Docker Image

From the project root (same directory as `Dockerfile` and `pom.xml`):

```bash
docker build -t fitness-log-api:1.0 .
```

The build uses a multi-stage Dockerfile: Maven compiles the JAR in Stage 1, and a minimal JRE image runs it in Stage 2. This takes a few minutes on the first run while Maven downloads dependencies.

## Run the Application

```bash
docker run -d --name fitness-log \
  -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI=mongodb://host.docker.internal:27017/fitnesslog \
  fitness-log-api:1.0
```

> `host.docker.internal` lets the container reach MongoDB running on your laptop. This works on Docker Desktop for Mac and Windows.

Verify the app started:

```bash
docker logs fitness-log
```

You should see the Spring Boot banner and a line like:
```
Started FitnessLogApplication in X.XXX seconds
```

Stop and remove the container when done:

```bash
docker stop fitness-log && docker rm fitness-log
```

## API Endpoints

### Authentication

| Method | Endpoint | Auth Required | Description |
|--------|----------|:-------------:|-------------|
| POST | `/api/auth/register` | No | Register a new user |
| POST | `/api/auth/login` | No | Log in and get a token |

### Workouts (all require `Authorization: Bearer <token>`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/workouts` | Create a workout session |
| GET | `/api/workouts` | Get all your workouts |
| GET | `/api/workouts/{id}` | Get a single workout by ID |
| PUT | `/api/workouts/{id}` | Update a workout |
| DELETE | `/api/workouts/{id}` | Delete a workout |

## Example Postman Requests

### 1. Register

**POST** `http://localhost:8080/api/auth/register`

Headers:
```
Content-Type: application/json
```

Body:
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "secret123"
}
```

Response `201 Created`:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### 2. Login

**POST** `http://localhost:8080/api/auth/login`

Body:
```json
{
  "email": "john@example.com",
  "password": "secret123"
}
```

Response `200 OK`:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

### 3. Create a Workout

**POST** `http://localhost:8080/api/workouts`

Headers:
```
Content-Type: application/json
Authorization: Bearer <token>
```

Body:
```json
{
  "exerciseName": "Bench Press",
  "duration": 45,
  "sets": 4,
  "reps": 10,
  "date": "2026-05-09"
}
```

Response `201 Created`:
```json
{
  "id": "6639f1a2b3c4d5e6f7a8b9c0",
  "userId": "6639f0a1b2c3d4e5f6a7b8c9",
  "exerciseName": "Bench Press",
  "duration": 45,
  "sets": 4,
  "reps": 10,
  "date": "2026-05-09"
}
```

---

### 4. Get All Workouts

**GET** `http://localhost:8080/api/workouts`

Headers:
```
Authorization: Bearer <token>
```

Response `200 OK` — returns only the authenticated user's workouts.

---

### 5. Delete a Workout

**DELETE** `http://localhost:8080/api/workouts/{id}`

Headers:
```
Authorization: Bearer <token>
```

Response `204 No Content`

---

## HTTP Status Codes

| Code | When |
|------|------|
| 200 | Successful GET / PUT |
| 201 | Successful POST (register, create workout) |
| 204 | Successful DELETE |
| 400 | Missing or invalid request fields |
| 401 | Missing, expired, or invalid JWT token |
| 403 | Valid token but resource belongs to another user |
| 404 | Resource not found |
| 409 | Email already registered |
| 500 | Unexpected server error |
