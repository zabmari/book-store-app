# 📚 Book Store App

Book Store App is a web application built with Spring Boot that allows users to authenticate and manage books, categories, shopping cart, and orders.
This project was created to demonstrate backend development best practices using REST APIs.

---

## 💻 Technologies Used

- Spring Boot – REST API framework
- Spring Security – Authentication & authorization
- Spring Data JPA – Database access
- MapStruct – DTO ↔ Entity mapping
- Liquibase – DB schema migrations
- MySQL – Production database
- H2 – In-memory test database
- Testcontainers – Containerized DB for integration tests
- JJWT – JWT Token handling
- Swagger – API documentation
- Lombok – Boilerplate reduction

---

## 🛠️ Base URL

All endpoints are prefixed with `/api` as defined in:

`server.servlet.contextPath=/api`

---

## 🔐 Authentication

| Endpoint             | Method | Access Role | Description            |
|----------------------|--------|-------------|------------------------|
| `/auth/registration` | POST   | Public      | Register a new user    |
| `/auth/login`        | POST   | Public      | Authenticate and login |

---

## 📖 Book Management

| Endpoint         | Method | Access Role | Description              |
|------------------|--------|-------------|--------------------------|
| `/books`         | GET    | ADMIN, USER | Retrieve all books       |
| `/books/{id}`    | GET    | ADMIN, USER | Get details of a book    |
| `/books`         | POST   | ADMIN       | Create a new book        |
| `/books/{id}`    | PUT    | ADMIN       | Update a book            |
| `/books/{id}`    | DELETE | ADMIN       | Delete a book            |
| `/books/search`  | GET    | ADMIN, USER | Search books by params   |

---

## 🗂️ Category Management

| Endpoint               | Method | Access Role | Description                 |
|------------------------|--------|-------------|-----------------------------|
| `/categories`          | GET    | ADMIN, USER | Get all categories          |
| `/categories/{id}`     | GET    | ADMIN, USER | Get category details        |
| `/categories`          | POST   | ADMIN       | Create a new category       |
| `/categories/{id}`     | PUT    | ADMIN       | Update category             |
| `/categories/{id}`     | DELETE | ADMIN       | Delete category             |
| `/categories/{id}/books` | GET  | ADMIN, USER | Get books from specific category |

---

## 🛒 Shopping Cart

| Endpoint                    | Method | Access Role | Description                |
|-----------------------------|--------|-------------|----------------------------|
| `/cart`                     | GET    | USER        | Get user shopping cart     |
| `/cart`                     | POST   | USER        | Add book to cart           |
| `/cart/cart-items/{cartItemId}` | PUT | USER        | Update quantity of cart item |
| `/cart/cart-items/{cartItemId}` | DELETE | USER     | Remove item from shopping cart |

---

## 🛍️ Order Management

| Endpoint                       | Method | Access Role | Description                     |
|--------------------------------|--------|-------------|---------------------------------|
| `/orders`                      | GET    | USER        | Get user’s order history         |
| `/orders`                      | POST   | USER        | Place a new order                |
| `/orders/{id}`                 | PATCH  | ADMIN       | Update order status             |
| `/orders/{orderId}/items`      | GET    | USER        | Get all items in a specific order |
| `/orders/{orderId}/items/{itemId}` | GET | USER        | Get a specific item from the order |

---

## 👥 User Roles

- **USER** – Can browse books, use cart, place orders, and view their order history.
- **ADMIN** – Can manage books, categories, and order statuses.

---

## 🚀 How to Run Locally with Docker

Make sure Docker is installed and running.

### 🧾 Clone the repository:

git clone https://github.com/zabmari/book-store-app

cd book-store-app

### 🧱 Build and run containers:

docker-compose up --build

## 🔍 Testing Endpoints with Sample Users

The database is preloaded with sample users for testing purposes:

| Email             | Password | Role  | Description                                    |
|-------------------|----------|-------|------------------------------------------------|
| user@example.com  | password | USER  | Regular user for browsing, shopping cart, and orders |
| admin@example.com | password | ADMIN | Admin user with full management rights          |

### How to Log In

- Use the `/api/auth/login` endpoint to obtain a JWT token.
- Example login request body:

```json
{
  "email": "user@example.com",
  "password": "password"
}
```
## How to Test Endpoints

You can test the API using **Postman** by opening the `Authorization` tab, selecting **Bearer Token** as the type, and pasting your JWT token into the token field.

Postman will automatically add the following header to your request:

`Authorization: Bearer <your_token_here>`

Alternatively, you can use the **Swagger UI** at:

http://localhost:8081/api/swagger-ui.html

## 📦 Sample JSON Request Bodies

🔐 POST /api/auth/registration
```json
{
  "email": "newuser@example.com",
  "password": "securePass123",
  "repeatPassword": "securePass123",
  "firstName": "Alice",
  "lastName": "Smith"
}
```

🔐 POST /api/auth/login
```json
{
  "email": "newuser@example.com",
  "password": "securePass123"
}
```
📘 POST /api/books (Admin only)
 ```json
{
  "title": "Clean Architecture",
  "author": "Robert C. Martin",
  "price": 49.99,
  "isbn": "9780134494166",
  "description": "A guide to software architecture and design principles.",
  "categoryId": 1
}
```
✏️ PUT /api/books/{id} (Admin only)
```json
{
  "title": "Clean Architecture - Updated",
  "author": "Robert C. Martin",
  "price": 44.99,
  "isbn": "9780134494166",
  "description": "Updated edition with new chapters.",
  "categoryId": 3
}
```
🗂️ POST /api/categories (Admin only)
```json
{
  "name": "Software Engineering",
  "description": "Books about software development and best practices."
}
```
🛒 POST /api/cart
```json
{
  "bookId": 1,
  "quantity": 2
}
```
🔄 PUT /api/cart/cart-items/{cartItemId}
```json
{
  "quantity": 3
}
```
🛍️ POST /api/orders
```json
{
  "shippingAddress": "456 Elm Street, Springfield, USA"
}
```
🚚 PATCH /api/orders/{id} (Admin only – update order status)
```json
{
  "status": "SHIPPED"
}
```

//Status options may include:  NEW, PENDING, COMPLETED, SHIPPED, DELIVERED.