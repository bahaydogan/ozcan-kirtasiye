A RESTful API for an e-commerce platform specializing in stationery products, built with Java Spring Boot and PostgreSQL. The app supports cart management, product catalogs, user authentication, order processing, and comments/reviews.
---
Key Features

1. Cart Management
- Retrieve a user's cart contents.
- Add/remove items, adjust quantities, or clear the cart.
- Calculate cart totals and process checkouts.

2. Category Management
- Organize products into categories.
- Assign products to categories and fetch products by category.

3. Comment/Review System
- Users can add, view, or delete comments on products.

- Filter comments by user or product.


4. Order Processing
-  Track orders by user, status, or date range.

- Cancel orders and generate user statistics via PostgreSQL stored procedures.

5. Product Management
- CRUD operations for products.

- Manage stock levels and product images (upload, update, delete).

- Retrieve summarized product data using a PostgreSQL view table.


6. User Management
- Register, activate, and manage user accounts.

- Authentication via login endpoint.

------
The system follows the MVC (Model-View-Controller) architecture, 
separating business logic (Models), HTTP request handling (Controllers), and presentation, which promotes scalability, testability, and maintainability. 

Interfaces play a critical role in defining consistent contracts between components (e.g., service layers, repositories), facilitating loose coupling, 
and seamless integration of future enhancements. 

The application operates database triggers to automatically log historical price changes whenever a product’s price is updated, 
ensuring an audit trail for data transparency and compliance. 

Data Transfer Objects (DTOs) are utilized to decouple the internal domain model from the API layer, 
enabling controlled data exposure, validation, and optimized payloads for client interactions. 

For security, user passwords are hashed using robust algorithms before storage, 
ensuring sensitive credentials remain protected even in the event of a database breach. 
Together, these practices ensure a secure, modular, and maintainable codebase aligned with industry standards.






