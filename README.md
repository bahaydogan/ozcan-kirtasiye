# 📒 Stationery E-Commerce API

A **RESTful API** built with **Java Spring Boot** and **PostgreSQL** for an e-commerce platform specializing in stationery products. This API supports essential e-commerce features, including **cart management, product catalogs, user authentication, order processing, and a comment/review system**.

---

## ✨ Key Features

### 🛒 Cart Management
- 🛍️ Retrieve a user's cart contents.
- ➕ Add/remove items, adjust quantities, or clear the cart.
- 💰 Calculate cart totals and process checkouts.

### 📂 Category Management
- 🏷️ Organize products into categories.
- 🔍 Assign products to categories and fetch products by category.

### 📝 Comment & Review System
- 💬 Users can **add, view, and delete** comments on products.
- 🎯 Filter comments by **user or product**.

### 📦 Order Processing
- 📋 Track orders by **user, status, or date range**.
- ❌ Cancel orders and generate **user statistics via PostgreSQL stored procedures**.

### 🏷️ Product Management
- 🏗️ Full **CRUD** operations for products.
- 📸 Manage stock levels and product images (**upload, update, delete**).
- 📊 Retrieve summarized product data using a **PostgreSQL view table**.

### 👤 User Management
- 🔐 Register, activate, and manage user accounts.
- 🔑 Secure authentication via **login endpoint**.

---

## 🏗️ Architecture & Best Practices

### 🔹 MVC Architecture
This API follows the **Model-View-Controller (MVC)** architecture, ensuring **scalability, maintainability, and testability** by separating concerns:
- **🛠️ Models** handle business logic.
- **📡 Controllers** process HTTP requests.
- **📂 Services & Repositories** manage data and operations.

### 🔹 Interfaces & Loose Coupling
- 🏗️ Interfaces define **consistent contracts** between components, promoting **loose coupling**.
- 🔄 Allows for **seamless integration of future enhancements**.

### 🔹 Database Features
- 📜 **Triggers** automatically log historical price changes whenever a product’s price is updated, ensuring an **audit trail** for transparency.
- 📊 **Stored procedures** optimize complex queries like **user order statistics**.
- 🔍 **View tables** provide summarized product data for efficient API responses.

### 🔹 DTOs (Data Transfer Objects)
- 📦 DTOs **decouple** the internal domain model from the API layer.
- 🛡️ Ensures **validation**, **controlled data exposure**, and **optimized payload sizes**.

### 🔹 Security Best Practices
- 🔒 **Password hashing** with robust cryptographic algorithms ensures sensitive credentials remain protected, even in case of a data breach.

---

## 🚀 Technologies Used

- 🖥 **Java 17+**
- 🔥 **Spring Boot** (Spring Security, Spring Data JPA)
- 🗄 **PostgreSQL** (Stored Procedures, Triggers, View Tables)
- 🔐 **JWT Authentication**
- 📦 **Lombok & MapStruct** (for code simplification)

---

## 🛠️ Getting Started

### 1️⃣ Clone the repository
```bash
git clone https://github.com/your-username/your-repository.git
cd your-repository
```

### 2️⃣ Configure the database
Update `application.properties` with your **PostgreSQL** configuration:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your-database
spring.datasource.username=your-username
spring.datasource.password=your-password
```

### 3️⃣ Run the application
```bash
mvn spring-boot:run
```

### 4️⃣ API Documentation
API endpoints are documented using **Swagger**. Once the app is running, visit:
🔗 `http://localhost:8080/swagger-ui.html`

---

## 🤝 Contributing
Contributions are welcome! Feel free to fork the repository and submit a pull request.

---

## 📜 License
This project is licensed under the **MIT License**.
