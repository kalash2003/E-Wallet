This e-wallet backend project is developed using Spring Boot and Java, offering essential features required for managing a digital wallet system.

1. Core Functionalities:

User Management: Handles user registration, login, and profile management.
Wallet Balance: Allows users to view, add, and manage wallet balances securely.
Transactions: Facilitates fund transfers between wallets and maintains transaction history for audit and user reference.
Authentication: Implements secure user authentication to protect user data and transactions.

2. Technology Stack:

Spring Boot: Framework for building robust RESTful APIs.
Database: Supports MySQL for production data persistence.
Maven: Used for dependency management and project build automation.

3. Setup Instructions:

Clone the repository to your local machine using git clone <repository-url>.
Navigate to the project directory: cd e-wallet-backend.
Configure the database connection in src/main/resources/application.properties with your database credentials.
Build the project using Maven: mvn clean install.
Start the application: mvn spring-boot:run.
Access the API endpoints at http://localhost:8080.
API Endpoints:

4. User Management:
   
Register a user: POST /api/users/register.
User login: POST /api/users/login.
Fetch user details: GET /api/users/{id}.
Wallet Operations:
Add funds: POST /api/wallet/add-funds.
Transfer funds: POST /api/wallet/transfer.
View balance: GET /api/wallet/balance/{userId}.
Transaction History:
Get transaction records: GET /api/transactions/{userId}.

5. Project Highlights:

Secure: Includes mechanisms for protecting sensitive user and transaction data.
Scalable: Designed to handle increasing numbers of users and transactions efficiently.
Testable: Built with H2 database support to allow easy testing during development.
