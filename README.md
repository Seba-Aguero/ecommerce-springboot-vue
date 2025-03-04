# E-commerce Application with Spring Boot and Vue.js

A modern e-commerce platform built with Spring Boot and Vue.js, featuring JWT authentication, product management, interactive shopping cart, and order processing with email confirmations. Includes dark mode support and responsive design for optimal user experience.

## 🌟 Features

### User Authentication & Authorization
- JWT-based authentication
- Email verification system
- Protected routes and API endpoints
- Role-based access control (Admin/User)

### Product Management
- Product listing with pagination
- Advanced product search and filtering
- Category and tag management
- Product details view
- Image management with fallback handling

### Shopping Experience
- Interactive shopping cart with real-time updates
- Persistent cart data
- Dynamic shipping cost calculation
- Responsive cart modal

### Checkout Process
- Multi-step checkout flow
- Shipping information
- Order review
- Order confirmation
- Email notifications

### User Dashboard
- Order history and tracking
- Order details view
- Profile management

### UI/UX Features
- Responsive design for all devices
- Dark mode support
- Loading states and animations
- Toast notifications system
- Form validation with error handling
- Skeleton loading for better UX

## 🛠 Technology Stack

### Backend
- Java 21
- Spring Boot 3.4.0
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL Database
- Maven for dependency management
- JUnit and Mockito for testing
- Swagger/OpenAPI for documentation
- Lombok for boilerplate reduction
- MapStruct for object mapping

### Frontend
- Vue.js 3.5.13 with Composition API
- Vite 6.0.11 for build tooling
- Pinia for state management
- Vue Router for navigation
- TailwindCSS 3.4.17 for styling
- Lucide icons
- Vue Toastification
- VeeValidate + Zod
- Axios for HTTP requests

## 📋 Prerequisites

- Java Development Kit (JDK) 21
- PostgreSQL 15+
- Maven 3.8+
- Node.js (v18 or higher)

## 🚀 Getting Started

### Backend Setup

1. Clone the repository:
```bash
git clone https://github.com/Seba-Aguero/ecommerce-springboot-vue.git
cd ecommerce-springboot-vue
```

2. Configure database:
   - Create a PostgreSQL database
   - Copy `backend/.env.example` to `.env`
   - Update database credentials in `.env`

3. Configure email service:
   - Update email configuration in `.env`
   - Set up SMTP credentials

4. Run the backend:
```bash
cd backend
mvn spring-boot:run
```

### Frontend Setup

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Run development server:
```bash
npm run dev
```

## 📁 Project Structure

```
ecommerce-springboot-vue/
|
├── backend/
│   └── src/
│       ├── main/
│       │   ├── java/
│       │   │   └── ecommerce_springboot_vue/
│       │   │       ├── config/
│       │   │       ├── controller/
│       │   │       ├── dto/
│       │   │       ├── entity/
│       │   │       ├── enums/
│       │   │       ├── exception/
│       │   │       ├── mapper/
│       │   │       ├── repository/
│       │   │       ├── security/
│       │   │       └── service/
│       │   └── resources/
│       └── test/
│           └── java/
│               └── ecommerce_springboot_vue/
|
└── frontend/
    └── src/
        ├── assets/
        ├── components/
        │   ├── auth/
        │   ├── cart/
        │   ├── common/
        │   ├── orders/
        │   └── products/
        ├── composables/
        ├── directives/
        ├── router/
        ├── services/
        ├── stores/
        ├── utils/
        └── views/
```

## 🔒 Security Features

- JWT token authentication
- Password encryption using BCrypt
- CORS configuration
- XSS protection
- CSRF protection
- Secure HTTP headers
- Input validation
- Rate limiting

## 🌐 API Endpoints

### Authentication
- `POST /api/v1/auth/register` - User registration
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/confirm-email` - Email confirmation
- `POST /api/v1/auth/change-password` - Password change
- `GET /api/v1/auth/profile` - Get user profile

### Products
- `GET /api/v1/products` - Get all products (with pagination, filtering, search)
- `GET /api/v1/products/{id}` - Get product by ID
- `POST /api/v1/products` - Create product (Admin)
- `PUT /api/v1/products/{id}` - Update product (Admin)
- `DELETE /api/v1/products/{id}` - Delete product (Admin)

### Cart
- `GET /api/v1/cart/{userId}` - Get user's cart
- `POST /api/v1/cart/{userId}/items` - Add item to cart
- `PATCH /api/v1/cart/{userId}/items/{productId}` - Update item quantity (increment/decrement)
- `DELETE /api/v1/cart/{userId}/items/{productId}` - Remove item from cart
- `DELETE /api/v1/cart/{userId}` - Clear cart

### Orders
- `POST /api/v1/orders` - Create order
- `GET /api/v1/orders` - Get all orders (Admin)
- `GET /api/v1/orders/user/{userId}` - Get user orders
- `PUT /api/v1/orders/{orderId}/status` - Update order status (Admin)
- `GET /api/v1/orders/{orderId}/items` - Get order items

### User Management
- `GET /api/v1/users/{id}` - Get user by ID (Admin)
- `GET /api/v1/users/profile` - Get user profile
- `PUT /api/v1/users/profile` - Update user profile
- `GET /api/v1/users/role` - Get user role
