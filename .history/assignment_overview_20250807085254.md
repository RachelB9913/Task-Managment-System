# Assignment Overview

**Project Title:** Task Management System with AWS Cognito and Role-Based Access


## Objective  
The goal of this assignment was to implement a backend system for managing users, projects, and tasks using Java Spring Boot. The system includes authentication via AWS Cognito, CRUD APIs for projects and tasks, and support for scalability, logging, pagination, and secure access.


## Development Workflow

I approached the project in the following order:

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
12. **Role-Based Access Control** – (Extra) Added logic for admin vs. regular users.  

Throughout the implementation, I often went back and forth between steps - refactoring services, improving endpoint logic, and adapting my plan as needed. This helped me deliver more reliable and polished code.

## Challenges Faced

This assignment included multiple technologies that I had limited prior experience with. That pushed me to learn quickly and adapt on the go.
Here are a few examples of the:

- **AWS Cognito** – This was my first time using AWS Cognito. I had to understand the hosted UI login flow, token decoding, and how to securely connect authenticated users to internal entities.  
- **Testing Controllers** – While service-level testing was more straightforward, mocking security contexts and validating controller behavior proved more complex and required additional research and retries.  
- **Spring Boot Annotations** – I learned how to use and combine annotations like `@CurrentUser`, `@PreAuthorize`, and `@PageableDefault` to build clean and maintainable code.  


## Use of AI Tools

To support and guide the development process, I used:

- **ChatGPT** – For learning and debugging new topics such as Spring Security, role-based access, pagination, and test strategies. It helped me understand unknown annotations, explain security flows, create examples and resolve blocking errors.  
- **GitHub Copilot** – I used Copilot mostly to speed up development of repetitive or boilerplate code (e.g., getters/setters, mapper methods, controller methods). It provided autocomplete suggestions that I could accept as-is or use as a base to improve. In some cases, I used it to **start** a function and then refined the logic manually.  

Although I know I could have used these tools for much more, such as building full classes, refining function logic, or accelerating the entire implementation, I intentionally chose to rely on them sparingly. My goal was to provide an assignment that reflects my own understanding and technical ability.


## Conclusion

Quite a few of the components and concepts I met here for the first time, and it was genuinely interesting to think through how to combine my existing Spring Boot knowledge with newly learned tools and ideas.  

I hope this project shows that I am capable of learning quickly, adapting to new challenges, and always striving to deliver something complete and thoughtful.