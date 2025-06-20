📚 README — Сервис управления заявками на ремонт оборудования

REST-сервис для управления заявками на ремонт промышленного оборудования с разграничением прав доступа для ролей: ADMIN, MANAGER, TECHNICIAN.
Реализовано с использованием Spring Boot, JPA, Flyway, JWT, PostgreSQL, Spring Kafka, Docker, покрыто тестами.

Функционал
Создание/управление заявками (только MANAGER)

Назначение мастера (только ADMIN)

Изменение статуса заявки (только TECHNICIAN)

Комментарии к заявке (все роли, согласно правам)

Просмотр заявок:

MANAGER — свои заявки

TECHNICIAN — только назначенные

ADMIN — все заявки

Публикация событий в Kafka при изменении статуса заявки (IN_PROGRESS, DONE)

Swagger UI/OpenAPI с поддержкой авторизации JWT

Стек технологий
Java 21

Spring Boot 3

Spring Security (JWT)

Spring Data JPA + Flyway

PostgreSQL (или H2 для тестов)

Spring Kafka

Lombok, MapStruct

JUnit 5, Testcontainers

Docker Compose

Архитектура
Модели данных: User, RepairRequest, Comment

Роли пользователей: ADMIN, MANAGER, TECHNICIAN

3 учетные записи: admin:admin, manager:manager, technician:technician

Слои: Controller → Service (с AOP для авторизации) → Repository

Собственная аннотация для проверки ролей (через AOP)

События Kafka: Отправка событий в топик repair-request-events при изменении статуса заявки

swagger: http://localhost:8080/swagger-ui/index.html