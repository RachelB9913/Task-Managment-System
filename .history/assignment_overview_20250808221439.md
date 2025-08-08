# Assignment Overview
*Rachel Belokopytov*

**Project Title:** Task Management System with AWS Cognito and Role-Based Access

## Objective
The goal of this assignment was to implement a backend system for managing users, projects, and tasks using **Java Spring Boot**. The system includes authentication via **AWS Cognito**, CRUD APIs for projects and tasks, role-based access control, and support for scalability, logging, pagination, and secure access.

## Architecture & Role Model
The application follows a **layered architecture**, with entities (`User`, `Project`, `Task`) mapped using JPA/Hibernate, repositories handling persistence, and services encapsulating business logic. DTOs and mappers provide a clean separation between API models and internal entities. The security layer uses `@PreAuthorize` for method-level access control, a custom `@CurrentUser` annotation for injecting authenticated user data, and ownership checks to ensure that users can only manage their own data.

Two main roles are supported:  
- **USER** – Can create, view, update, and delete their own projects and tasks.  
- **ADMIN** – Can view and manage all projects and tasks across users, and may add or modify data on behalf of a user if needed.  

Roles are managed via AWS Cognito groups (`ROLE_ADMIN` / `ROLE_USER`) and converted into Spring Security authorities.

## Development Workflow
1. **Build an Initial Plan** – Outlined a detailed list of tasks and ideas to guide development and track progress.  
2. **Project Setup** – Initialized the Spring Boot structure and dependencies.  
3. **Entity Design** – Defined models for `User`, `Project`, and `Task`.  
4. **Repositories & Services** – Implemented JPA repositories and service logic.  
5. **DTOs & Mappers** – Created Data Transfer Objects and mapping layers to isolate API input/output from internal logic.  
6. **CRUD API** – Built RESTful controllers for managing projects and tasks.  
7. **AWS Cognito Integration** – Integrated authentication flow with JWT support and linked the internal `User` entity to AWS claims.  
8. **Spring Security** – Secured all endpoints based on authentication and roles.  
9. **Pagination** – Added pageable responses for relevant endpoints.  
10. **Testing** – Wrote unit tests for core service classes.  
11. **Deployment Plan & Documentation** – Outlined how to scale the project for 10k daily users.  
12. **Role-Based Access Control** – Added logic for admin vs. regular users.

Throughout the implementation, I often went back and forth between steps—refactoring services, improving endpoint logic, and adapting my plan as needed. This helped me deliver more reliable and polished code.

## Challenges Faced
- **AWS Cognito** – Learned the hosted UI flow, token decoding, and secure mapping to internal users.  
- **Testing Controllers** – Required mocking security contexts and JWT authentication to validate endpoint behavior.  
- **Spring Boot Annotations** – Learned how to effectively use and combine annotations like `@CurrentUser`, `@PreAuthorize`, and `@PageableDefault`.

## Use of AI Tools
To support and guide development, I used:  
- **ChatGPT** – For learning and debugging topics like Spring Security, pagination, role-based access, and test strategies.  
- **GitHub Copilot** – To speed up repetitive code generation (e.g., getters/setters, mapper methods) and provide starting points for certain methods.  

I intentionally used these tools selectively to ensure the project reflects my own technical ability and understanding.

## Deployment Note
To support 10k daily users, the system should be containerized with Docker, deployed to a cloud environment (e.g., AWS ECS/EKS), fronted by a load balancer, use PostgreSQL instead of H2, and enable horizontal scaling for both backend and frontend services. Full details are provided in the separate deployment document.

## Conclusion
Quite a few of the components and concepts I met here for the first time, and it was genuinely interesting to think through how to combine my existing Spring Boot knowledge with newly learned tools and ideas.  

I hope this project shows that I am capable of learning quickly, adapting to new challenges, and always striving to deliver something complete and thoughtful.
