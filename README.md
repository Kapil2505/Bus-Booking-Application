# Bus Booking Application

A comprehensive Bus Booking Application built using Java, Spring Boot, Hibernate, and MySQL. This project provides real-time bus booking, tracking, and route optimization features, with secure payment integration and JWT-based authentication.

## Table of Contents
1. [Features](#features)
2. [Technologies Used](#technologies-used)
3. [Installation](#installation)
4. [Configuration](#configuration)
5. [Usage](#usage)
6. [API Documentation](#api-documentation)
7. [Contributing](#contributing)

## Features
- Real-time bus booking with dynamic route optimization.
- RESTful APIs for seamless communication.
- Secure payment gateway integration.
- JWT-based authentication and authorization.
- Real-time bus tracking with dynamic updates.
- Ticket generation using iText PDF.
- Email and SMS notifications.

## Technologies Used
- **Java**
- **Spring Boot**: For building RESTful services.
- **Hibernate**: ORM framework.
- **MySQL**: Database management system.
- **iText PDF**: For generating dynamic tickets.
- **Spring Security**: For JWT authentication and security.
- **Swagger**: For API documentation.
- **AWS**: For cloud deployment.

## Installation
### Prerequisites
- Java 8 or higher
- Maven
- MySQL database

### Steps to Set Up the Project:
1. Clone the repository:
    ```bash
    git clone https://github.com/Kapil2505/Bus-Booking-Application.git
    cd Bus-Booking-Application
    ```

2. Configure database settings in `application.properties`:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/bus_booking
    spring.datasource.username=your-username
    spring.datasource.password=your-password
    ```

3. Build the project:
    ```bash
    mvn clean install
    ```

4. Run the application:
    ```bash
    mvn spring-boot:run
    ```

## Configuration
Configure environment variables for email, SMS, and JWT:
```bash
MAIL_USERNAME=your-email
MAIL_PASSWORD=your-email-password
SMS_API_KEY=your-sms-api-key
JWT_SECRET=your-jwt-secret

## Usage
- Access the application at `http://localhost:8080`.
- View API documentation at `http://localhost:8080/swagger-ui/`.

### API Endpoints:
- **User Registration**: 
    ```
    POST /api/users/register
    ```
- **User Login**: 
    ```
    POST /api/users/login
    ```
- **Book a Bus**: 
    ```
    POST /api/bus/book
    ```
- **Get Bus Details**: 
    ```
    GET /api/bus/details/{id}
    ```
- **Process Payment**: 
    ```
    POST /api/payment/process
    ```

## API Documentation
- Swagger API documentation is available at: 'http://localhost:8080/swagger-ui/'


## Contributing
1. Fork the repository.
2. Create a new branch:
    ```bash
    git checkout -b feature-branch
    ```
3. Commit your changes:
    ```bash
    git commit -m 'Add feature'
    ```
4. Push to the branch:
    ```bash
    git push origin feature-branch
    ```
5. Open a pull request.
 
