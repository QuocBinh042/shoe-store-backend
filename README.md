
# 🛒 Shoe Store – Backend (Spring Boot + VnPay + Redis)

This is the backend service of the Shoe Store application, developed using **Spring Boot**. It exposes RESTful APIs to manage authentication, products, orders, users, and payment processing.

---

## 🔧 Tech Stack

- **Spring Boot 3.x**
- **Spring Security** with JWT
- **Spring Data JPA** with Hibernate
- **Redis** for session caching and performance
- **Cloudinary SDK** for image hosting
- **VnPay SDK** for online payment
- **Swagger** for API documentation
- **Docker** + **Docker Compose**

---

## ✨ API Features

- 🔐 Secure login & registration with role-based access (User/Admin)
- 👟 Product CRUD (Admin)
- 🛒 Order creation and tracking
- 🧾 Invoice and payment confirmation with VnPay callback
- 🖼️ Cloudinary image upload
- 🔁 Redis caching for improved performance

---

## 📄 API Documentation

Swagger UI:  
➡️ [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven
- Redis running on `localhost:6379`

### Setup

```bash
./mvnw clean install
./mvnw spring-boot:run
