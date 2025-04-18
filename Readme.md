## AeroHub

This project is a web service that provides airport data from JSON or CSV files. The service includes features like 
sorting, filtering, and adding new airports at runtime. It also includes a UI to display the data in a table format.


### Prerequisites
- Java 17 or higher
- Maven 3.8 or higher
- Spring Boot 3.x

### Backend Features

1. Swagger UI for API documentation : [swagger](https://aero-hub.cfapps.us10-001.hana.ondemand.com/swagger-ui/index.html)
2. Supports filtering of airports by name, country, and city
3. Provides Pagination and sorting capabilities
4. Deployed on BTP Cloud Foundry
   1. Consumes Application Logging Service for Opensearch Dashboard
   2. Postgres DB for data persistence and support scalability
   3. Redis Cache for caching airports 
5. Logback for logging

### Local Setup
1. Clone the repository
2. Make sure you've a reddis server running locally
   ```bash
      docker pull redis:latest
      docker run --name redis-dev -p 6379:6379 -d redis:latest
   ````
3. Navigate to the project directory and build the project
    ```bash
      mvn clean install 
      mvn spring-boot:run
    ```

## Configuration
- Datasets: Place the JSON or CSV files in the src/main/resources/data folder.
- Profiles: Use application-dev.properties for development and application-prod.properties for production.

## Todo
1. Dockerize the application
2. Add more unit tests
3. Add more error handling
5. If time permits:
   1. Role based authentication
   2. Devops integration
