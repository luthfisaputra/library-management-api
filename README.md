# 📚 Library Management API

A RESTful API for managing a library system — books, members, and loan transactions — built as a backend portfolio project to practice production-grade Spring Boot architecture, layered design, and proper error handling.

> ⚠️ **Status: Work in Progress.** Core features (CRUD, pagination, loan logic, exception handling) are functional and tested. Additional improvements are planned — see [Future Improvements](#-future-improvements).

---

## ✨ Features

- **Book Management** — full CRUD with author relations, ISBN uniqueness validation, and stock tracking
- **Member Management** — full CRUD with duplicate email prevention
- **Loan System** — borrow/return logic with stock validation, duplicate-active-loan prevention, and transactional consistency
- **Pagination & Filtering** — paginated book listing with sort and filter by title/author
- **Centralized Exception Handling** — consistent error response format with appropriate HTTP status codes (404, 400, 409)
- **DTO Pattern** — clean separation between request/response models and database entities

---

## 🛠 Tech Stack

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.2.x |
| Database | PostgreSQL |
| ORM | Spring Data JPA (Hibernate) |
| Build Tool | Maven |
| Validation | Jakarta Bean Validation |
| Boilerplate Reduction | Lombok |

---

## 🏗 Architecture

This project follows a standard **layered architecture**:

```
Controller → Service → Repository → Database
```

| Layer | Responsibility |
|---|---|
| **Controller** | Handles HTTP requests, parameter binding, input validation |
| **Service** | Business logic (stock checks, duplicate validation, pagination rules) |
| **Repository** | Database queries via Spring Data JPA |
| **DTO** | Decouples API contracts (request/response) from database entities |
| **Exception** | Centralized error handling via `@RestControllerAdvice` |

### Entity Relationships

```
Author  ──< Book         (one author has many books)
Member  ──< Loan          (one member has many loans)
Book    ──< Loan          (one book can be loaned multiple times)
```

---

## 🚀 Getting Started

### Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 14+

### Installation

1. Clone the repository
   ```bash
   git clone https://github.com/luthfisaputra/library-management-api.git
   cd library-management-api
   ```

2. Create the database
   ```sql
   CREATE DATABASE library_db;
   ```

3. Configure local credentials

   Create `src/main/resources/application-local.properties` (this file is gitignored):
   ```properties
   spring.datasource.password=your_postgres_password
   ```

4. Run the application
   ```bash
   mvn spring-boot:run
   ```

   The API will be available at `http://localhost:8080`

---

## 📡 API Endpoints

### Books

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/books` | Create a new book |
| `GET` | `/api/books` | List books (paginated, filterable) |
| `GET` | `/api/books/{id}` | Get book by ID |
| `PUT` | `/api/books/{id}` | Update a book |
| `DELETE` | `/api/books/{id}` | Delete a book |

**Query params for `GET /api/books`:** `page`, `size`, `sortBy`, `direction`, `title`, `authorId`

### Members

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/members` | Register a new member |
| `GET` | `/api/members` | List all members |
| `GET` | `/api/members/{id}` | Get member by ID |
| `PUT` | `/api/members/{id}` | Update a member |
| `DELETE` | `/api/members/{id}` | Delete a member |

### Loans

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/loans` | Borrow a book |
| `PATCH` | `/api/loans/{id}/return` | Return a borrowed book |
| `GET` | `/api/loans` | List all loans (paginated) |
| `GET` | `/api/loans/member/{memberId}/active` | Get a member's active loans |

---

## 🗄 Database Schema

| Table | Key Columns |
|---|---|
| `authors` | id, name, bio |
| `books` | id, title, isbn, year_published, stock, author_id (FK) |
| `members` | id, name, email, joined_at |
| `loans` | id, book_id (FK), member_id (FK), loan_date, return_date, is_returned |

---

## 💡 What I Learned

Building this project pushed me beyond basic CRUD into thinking like a backend engineer rather than just writing code that works:

- **DTO pattern matters more than it looks.** Separating request/response models from entities isn't just "extra boilerplate" — it controls exactly what clients can send and see, and protects the API from breaking when the database schema evolves.
- **Layered architecture is about responsibility, not just folders.** Understanding *why* pagination logic belongs in the Service layer (not Controller or Repository) clarified how each layer should own a distinct kind of decision.
- **Generic exceptions hide real problems.** Replacing `RuntimeException` with specific exceptions (`ResourceNotFoundException`, `DuplicateResourceException`, `BadRequestException`) mapped to correct HTTP status codes made the API genuinely usable by a client, not just functional.
- **`@Transactional` is a correctness guarantee, not a performance trick.** Implementing the loan/return flow showed concretely how partial failures (e.g., stock decremented but loan record not saved) lead to silent data inconsistency — and how transactions prevent that.
- **Security hygiene is a habit, not an afterthought.** Accidentally committing a database password to a public repo — and having to rotate the credential and rebuild the repo cleanly — was a hands-on lesson in why `.gitignore` and profile-based config should be set up *before* the first commit, not after.

---

## 🔮 Future Improvements

- [ ] Authentication & authorization (Spring Security + JWT)
- [ ] Unit & integration tests (JUnit + Mockito)
- [ ] Loan due dates with overdue tracking/fines
- [ ] Swagger/OpenAPI documentation
- [ ] Dockerize for easier setup
- [ ] CI pipeline (GitHub Actions) for automated build/test on push

---

## 👤 Author

**Luthfi Saputra**
Informatics Engineering student | Backend Engineering enthusiast

[GitHub Repository](https://github.com/luthfisaputra/library-management-api)
