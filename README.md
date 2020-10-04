# KIPOS API

Kipos API description.

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->
**Table of Contents**  *generated with [DocToc](https://github.com/thlorenz/doctoc)*

- [Run](#run)
  - [Prerequisite](#prerequisite)
  - [Database & mail services setup](#database--mail-services-setup)
  - [Configuration](#configuration)
  - [Project dev env links](#project-dev-env-links)
- [Development](#development)
  - [External references and documentations](#external-references-and-documentations)
  - [Rights and access management](#rights-and-access-management)
  - [WS HTTP response and Exception handling (ApiException)](#ws-http-response-and-exception-handling-apiexception)
  - [Authentication](#authentication)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Run
### Prerequisite

Dependencies required before installing the project:
* [Java 11](https://github.com/AdoptOpenJDK/openjdk11-binaries/releases)
* [Maven 3.3+](https://maven.apache.org/install.html)
* [Docker](https://docs.docker.com/install/)
* [Docker-compose](https://docs.docker.com/compose/install/)

### Database & mail services setup

[Postgresql](https://www.postgresql.org/docs/13/index.html) and [MailDev](https://github.com/maildev/maildev) can be easily up and running thanks to this docker-compose command:
```shell
docker-compose up
```

### Configuration
* application.yml file contains the main configuration.
* application-dev.yml file contains the dev configuration and override the main configuration.
* application-test.yml file contains the test configuration.

To correctly setup the dev environment, simply add "dev" value to "active profiles" in your Spring boot configuration.

### Project dev env links

* [API](http://localhost:8000)
* [Swagger](http://localhost:8000/swagger-ui.html)
* [MailDev](http://localhost:8081)
* [Actuator](http://localhost:9000/actuator)

## Development

### External references and documentations
* Framework: [Spring Boot 2.3.4](https://docs.spring.io/spring-boot/docs/2.3.4.RELEASE/reference/html/)
* Database migration: [Flyway](https://flywaydb.org/)
* Getter/Setter generation: [Lombock](https://projectlombok.org/)

### Rights and access management

Rights and access management is carried out via Spring Security.
The @PreAuthorize annotation is used to check access to an endpoint.
Business checks can also be done through the service call in the annotation.

See the documentation: [Spring Security 3.0.x](https://docs.spring.io/spring-security/site/docs/3.0.x/reference/el-access.html)

### WS HTTP response and Exception handling (ApiException)

To return an HTTP code other than 200 as a response to a WS (in the event of a functional or technical error), all you have to do is throw an ApiException which takes an HTTP code and a message as parameters.
It will be caught by an ExceptionHandler and returned in response to the WS with the specified HTTP code.
This exception can be called anywhere at any time and it stops the current processing to return the desired error.

### Authentication

JWT is used for authentication, which means that for calls requiring authentication, you must have the token retrieved by the /auth /login call and send it in the header of the request:
```yaml
key: Authorization
value: Bearer {your-token}
```
