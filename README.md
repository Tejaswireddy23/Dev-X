# Smart To-Do API (Java + SQL)

A Spring Boot backend for a resume-worthy project: **To-Do List with Smart Suggestions**.

## Tech Stack
- Java 17
- Spring Boot 3 (Web, Security, JPA, Validation)
- SQL database (PostgreSQL in production, H2 in local default)
- Flyway for SQL migrations
- JWT authentication

## Features
- Signup and login with JWT token
- Create, edit, delete tasks
- Task categorization
- Mark task as pending or completed
- Smart suggestion endpoint (`/api/tasks/suggest`) using rule-based hints (AI-ready placeholder)

## API Endpoints
### Auth
- `POST /api/auth/signup`
- `POST /api/auth/login`

### Tasks (requires `Authorization: Bearer <token>`)
- `GET /api/tasks`
- `POST /api/tasks`
- `PUT /api/tasks/{id}`
- `DELETE /api/tasks/{id}`
- `POST /api/tasks/suggest`

## Run locally
```bash
mvn spring-boot:run
```

## Run tests
```bash
mvn test
```

## Sample payloads
### Signup / Login
```json
{
  "email": "you@example.com",
  "password": "password123"
}
```

### Create task
```json
{
  "title": "Finish Spring Boot API",
  "category": "Work",
  "status": "PENDING"
}
```

### Suggest tasks
```json
{
  "prompt": "I want to improve my health and study daily"
}
```

## Production DB (PostgreSQL)
Set environment variables:
- `DB_URL=jdbc:postgresql://localhost:5432/todo_db`
- `DB_USERNAME=postgres`
- `DB_PASSWORD=postgres`
- `DB_DRIVER=org.postgresql.Driver`
- `JWT_SECRET=<base64-secret-at-least-32-bytes>`
