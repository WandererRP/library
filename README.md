Overview

The Library Management System is a microservices-based project that consists of two main services:

Library API Service – Handles authors and books management, exposing RESTful APIs.

Notification Service – Listens to Kafka events and logs notifications related to library updates.

This system is built using Spring Boot, Kafka, PostgreSQL, and Docker to ensure a scalable and event-driven architecture.


Kafka Topics

author-event-topic – Publishes author-related events

book-event-topic – Publishes book-related events

The Notification Service listens to these topics and logs incoming messages.


Additional Information

Flyway is used for database migrations (located in db.migration).

Kafka UI is available at http://localhost:8085/ for monitoring Kafka topics.

PostgreSQL is running on localhost:5432 with default credentials set in docker-compose.yml.


Steps to Run

1 Clone the Repository

git clone [https://github.com/your-repo/library.git](https://github.com/WandererRP/library.git)
cd library

2 Start the Project Using Docker Compose

docker-compose up -d

4 Verify Running Services

Library API: http://localhost:8081/authors

Notification Service: Logs Kafka messages

Kafka UI: http://localhost:8085/

5 Check Running Containers
docker ps
