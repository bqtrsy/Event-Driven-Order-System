# Event-Driven Order System

## ⚠️ Project Status
This is a learning project developed during my academic studies. Some features are still in development.

## ✅ Working Features
- Order placement

## 🚧 Known Issues / In Progress
- Payment gateway integration (mock implementation)
- Email notifications (not yet implemented)
- Performance optimization needed for large datasets

## 🎯 Purpose
This project demonstrates my understanding of:
- Event-driven architecture with RabbitMQ
- Full-stack development (React.js + Spring Boot)
- RESTful API design
- Database design with MongoDB
- Responsive UI with Tailwind CSS

## 🚀 Technologies Used
- Frontend: React.js, Tailwind CSS
- Backend: Java Spring Boot
- Message Broker: RabbitMQ
- Database: MongoDB
- DevOps: Docker

## 📝 Setup Instructions
📋 Prerequisites
Before you begin, ensure you have the following installed:

Node.js (v16 or higher)
Java JDK (v11 or higher) 
MongoDB (v5.0 or higher) 
RabbitMQ (v3.9 or higher)
Git 
Maven (v3.6 or higher) - For building Spring Boot backend

🚀 Installation Steps
1. Clone the Repository
bashgit clone https://github.com/yourusername/event-driven-order-system.git
cd event-driven-order-system
2. Backend Setup (Spring Boot)
bash# Navigate to backend folder
cd backend

# Install dependencies
mvn clean install

# Configure application.properties
# Create src/main/resources/application.properties with:
spring.data.mongodb.uri=mongodb://localhost:27017/orderdb
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
server.port=8080

# Run the backend server
mvn spring-boot:run
Backend will run on http://localhost:8080
3. Frontend Setup (React.js)
bash# Open new terminal, navigate to frontend folder
cd frontend

# Install dependencies
npm install

# Configure environment variables
# Create .env file in frontend folder:
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_ENV=development

# Start the development server
npm start
Frontend will run on http://localhost:3000
4. Start MongoDB
bash# In a new terminal
mongod
5. Start RabbitMQ
bash# In a new terminal
# Windows:
rabbitmq-server

# Mac/Linux:
sudo rabbitmq-server
Access RabbitMQ Management Console at http://localhost:15672 (default credentials: guest/guest)
✅ Verify Installation

Open browser and navigate to http://localhost:3000
You should see the application homepage
Check backend health: http://localhost:8080/actuator/health
RabbitMQ management: http://localhost:15672

🧪 Running Tests
bash# Backend tests
cd backend
mvn test

# Frontend tests
cd frontend
npm test
🐛 Troubleshooting
Issue: MongoDB connection failed

Ensure MongoDB is running: mongod --version
Check MongoDB is listening on port 27017

Issue: RabbitMQ connection refused

Verify RabbitMQ is running: Check services/task manager
Ensure port 5672 is not blocked by firewall

Issue: Port already in use

Change backend port in application.properties
Change frontend port: Set PORT=3001 in .env

Issue: npm install fails

Clear npm cache: npm cache clean --force
Delete node_modules and package-lock.json, then npm install again
