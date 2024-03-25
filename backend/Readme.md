# CMS Backend

Implementado con Spring Boot, Spring Security y Keycloak

## Keycloak OAuth2Server

Este repositorio emplea Keycloak como proveedor de autenticación y autorización. Los pasos para crear un nuevo Realm, Cliente, Usuarios y Roles son los siguientes:

1. Ejecutar contenedor de Docker
    ```
    cd keycloak
    docker compose up
    ```
2. Entrar a la consola de administración en `http://localhost:9000/admin` e introducir las credenciales fijadas como entorno en el `compose`
3. Crear un nuevo Realm `realmName`
4. Crear un nuevo Client `clientName`. Pasos default y marcar `Client Authentication` On. Valid Redirect URIs `*` (se puede añadir las que se desee para redireccion
5. Crear un nuevo User `userName`. Confirmar su correo.
6. Roles.
  1. Crear con el cliente seleccionado, en la pestaña `Role` un nuevo rol. Importante el nombre que se asigne al rol, en la clase `SecurityConfig` de la aplicación de Spring, deberá indicarse.
  2. Seleccionar el usuario. En la pestaña `Role mapping` seleccionar `Assign role`. `Filter by clients` e introducir el nombre del rol creado. Seleccionar.
7. En la aplicación de Spring, además de los módulos de programa `JwtConverter.java` `JwtConverterProperties.java` y `SecurityConfig.java` se debe especificar lo siguiente en el `application.properties`
    ```
    # Security Configuration
    spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:9000/realms/realmName
    spring.security.oauth2.resourceserver.jwt.jwk-set-uri=${spring.security.oauth2.resourceserver.jwt.issuer-uri}/protocol/openid-connect/certs

    # JWT Configuration
    jwt.auth.converter.resource-id=clientName
    jwt.auth.converter.principal-attribute=principal_username
    ```

Para testar los endpoints desde Postman:
1. Post request: `http://localhost:9000/realms/realmName/protocol/openid-connect/token` Body `x-www-form-urlencoded`
  - grant_type: password
  - client_id: clientName
  - username: userName
  - password: userPassword
  - client_secret: clientSecret (se extrae en Keycloak -> Selecciona cliente -> Tab `Credentials`)
2. Si esta correcto, devolvera un código `200` con el `access_token`, se copia
3. Acceder a un endpoint del controller de SpringBoot protegido para el rol asignado al usuario, añadir el Token en `Authorization` `Bearer access_token`
  
