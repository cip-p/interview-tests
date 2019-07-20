# pass-management Java Spring Boot 2 interview test

####Requirement
Implement a pass management system using `REST APIs`, `java` and `spring boot`.
I had to build 4 APIs and use a database which had to persist data during application restarts.

####Tools and approach
- `java 8`, `spring boot 2` and `maven` coding test
- `TDD` - `unit testing`, `component testing`, `integration testing`
- `mockito` for mocking
- `spring-boot-test` for `component` and `integration` testing

####Integration tests
`@SpringBootTest` `Spring Boot` using `H2` database

`cip.interview.passmanagement.PassManagementIntegrationTest`

####Controller tests
`@WebMvcTest` `Spring Boot` test

`cip.interview.passmanagement.web.PassControllerTest`

####Data/JPA tests
`@DataJpaTest` `Spring Boot` test

`cip.interview.passmanagement.domain.PassRepositoryTest`

####Unit tests
`mockito` test

`cip.interview.passmanagement.service.PassServiceTest`


####Tip
Use the following property to save data on disk when using `H2` db:

`spring.datasource.url=jdbc:h2:${java.io.tmpdir}/pass-db`
