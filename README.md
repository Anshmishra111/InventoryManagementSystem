# Inventory Management System

A robust microservices-based Inventory Management System designed to handle complex warehouse operations, sales, purchases, and reporting with high scalability.

## Microservices Architecture

This project is built using a microservices architecture, where each service handles a specific domain of the system.

### Service Port Mapping

| Service Name | Port | Description |
|--------------|------|-------------|
| **Auth Service** | `8081` | Manages user authentication, authorization, and JWT security tokens. |
| **Product Service** | `8082` | Handles product catalog management, including categories and specifications. |
| **Warehouse Service** | `8083` | Manages warehouse details, storage locations, and capacity tracking. |
| **Supplier Service** | `8084` | Maintains supplier records and contact information. |
| **Purchase Service** | `8085` | Handles procurement workflows, purchase orders, and receiving. |
| **Movement Service** | `8086` | Tracks the movement of stock between different warehouse locations. |
| **Alert Service** | `8087` | Monitors inventory levels and generates notifications for low stock. |
| **Sales Service** | `8088` | Processes customer sales orders and maintains sales history. |
| **Report Service** | `8089` | Provides comprehensive data analysis and business intelligence reports. |
| **Website Controller** | `9090` | Acts as the **API Gateway**, routing all frontend requests to the relevant microservices. |

## Technology Stack

- **Backend**: Java 17+, Spring Boot 3.x
- **Database**: MySQL (per-service databases)
- **Messaging**: RabbitMQ (for inter-service communication)
- **Security**: Spring Security with JWT
- **API Documentation**: OpenAPI / Swagger UI
- **Build Tool**: Maven

## Getting Started

### Prerequisites

- JDK 17 or higher
- MySQL Server
- RabbitMQ
- Maven

### Database Setup

Each service requires its own database. Ensure you create the following databases in your MySQL instance:
- `inventory_auth_db`
- `inventory_product_db`
- `inventory_warehouse_db`
- `inventory_supplier_db`
- `inventory_purchase_db`
- `inventory_movement_db`
- `inventory_alert_db`
- `inventory_sales_db`
- `inventory_report_db`

### Running the Services

1. Clone the repository:
   ```bash
   git clone https://github.com/Anshmishra111/InventoryManagementSystem.git
   ```
2. Navigate to each service directory and run using Maven:
   ```bash
   mvn spring-boot:run
   ```
   *Note: Start the **Auth Service** and **RabbitMQ** first.*

## API Gateway

The **Website Controller** (running on port `9090`) serves as the central entry point for the system. All external requests should be directed here.

---
Developed by [Himanshu Mishra](https://github.com/Anshmishra111)
