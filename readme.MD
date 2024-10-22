# Shared Shopping List Backend

This is the backend for a Shared Shopping List application where users can create, manage, and share lists.

**Kotlin** - Primary language for backend development. \
**Spring Boot** - Framework used for building the application. \
**PostgreSQL** - Relational database used for storing data. \
**Docker** - Used for containerizing the application, ensuring portability and ease of deployment.

## API endpoints
TODO

## Running Locally
To run the application locally (without Docker), make sure your PostgreSQL server is running and update the application.yml with the appropriate database settings:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shoppingdb
    username: your-username
    password: your-password
```




### License
This project is licensed under the MIT License - see the LICENSE file for details.

### Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.