# Hero Maintenance API

The purpose of this api is to make the **maintenance** and management of **superheroes**.

This api will let you:
• Find all super heroes
• Find a unique heroe by id
• Delete a heroe
• Modify an heroe
And
• Find all heroes that contains X parameter on his name
> For example: if the **X** parameter is **“man”**, the api will return **“Spiderman”**, **“Superman”**, **“Manolito el fuerte”**, etc.

## How to run
To run the application you must **execute the following comands** being in the **root folder** of the app
> 1. $ ./mvnw clean package
> 2. $ docker build -t lean/heroesapi .
> 3. $ docker run -e ADMIN_USER_USERNAME='foo' -e ADMIN_USER_PASSWORD='foo' -p 8080:8080  lean/heroesapi

### Security
This REST API is protected with Spring Security Basic Auth. To perform request to the API, it's necessary to be authenticated with the credentials that 
you provided as environment variables with name **ADMIN_USER_USERNAME** and **ADMIN_USER_PASSWORD**

## Documentation
Spring doc library with Openapi and Swagger V3 was used for the REST API documentation.
To see it and be able to make request for test with Swagger UI platform, you must consult the following 
URL while the app is running on local:
- To access into **Swagger-UI** dashboard is necessary a **basic authentication** (With the credentials that you provide by **application.properties** or **environment variables** in the docker run)
> http://localhost:8080/swagger-ui/index.html
 
## Technical details
This application was developed using Java 11 and the version 2.7.5 of Spring Boot.

For testing, the Service shape was tested with unit testing using Mockito, Junit 5 and assertJ library.
The presentation shape was tested with integration tests using assertJ, Spring security test and Spring Boot test library.
The data persistance shape was tested with integration tests using Junit Spring Boot test library (Specifically with DataJpaTest interface) .
