# _Java-shareit_
**ShareIt** - sharing application for common items you need for daily routine.

- share you tools, equipment and any other stuff you are ready to give for some period of time 
- search and use what others have shared
- enjoy your experience

### _Technologies used_
REST API service with Spring-Boot, JPA Hibernate, PostgreSQL, Java 11, Lombok, Docker

### _Project Structure_
There are 2 microservices made as modules in project:

ShareIt-gateway - works as proxy processing and validating income requests and redirecting them to main server.
Depends on ShareIt-server. Runs on port 8080.

ShareIt-server - main service makes all the work and application logic. Uses Postgres database.
Runs on port 9090.


### _Starting the service_
CLI start command: docker-compose -p shareit up

*Manual start-up*:

Set environmental variables.
Run database.
Run shareit-server.
Run shareit-gateway.
Environmental variables set by default in docker-compose:
- Shareit-server:
  - POSTGRES_DB=shareitdb
  - POSTGRES_USER=shareit_user
  - POSTGRES_PASSWORD=password
  - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/shareitdb


SERVER_PORT=9090
- Shareit-gateway:
  - SERVER_URL=http://server:9090