# Spring-Rest-BookStore
<p align="center">
  <img src="https://i.imgur.com/L8JoB88.png" width="400px"/>
</p>

## Arquitectura
<p align="center">
  <img src="https://i.imgur.com/4r1PwDf_d.webp?maxwidth=1000"/>
</p>

## Diagrama UML
<p align="center">
  <img src="https://i.imgur.com/H4yEED8.png"/>
</p>

<p align="center">
  <img src="https://i.imgur.com/T9f62Df.png"/>
</p>

# NULLERS BOOKS API

**Por NULLERS - 4 de diciembre de 2023**

## Descripción

Bienvenido a la API REST de NULLERS BOOKS, una tienda de libros en línea que te permite realizar diversas operaciones, como consultar libros, gestionar usuarios, administrar tiendas y realizar pedidos. Nuestra API está diseñada para ser segura, eficiente y escalable, proporcionando una interfaz robusta para interactuar con la plataforma de comercio de libros.

### Estructura del Proyecto

- **Controllers:** Manejan las solicitudes HTTP y devuelven las respuestas correspondientes.
- **Exceptions:** Define excepciones específicas utilizadas en la aplicación.
- **Models:** Define los objetos utilizados en la aplicación.
- **Repositories:** Realiza operaciones con la base de datos.
- **Services:** Realiza operaciones necesarias para que el controlador pueda devolver la respuesta.
- **Utils:** Define las clases útiles que se utilizan en la aplicación.
- **Main:** El programa que ejecutará la aplicación.

## Infraestructura

El usuario tiene un UserRole, el cual define el tipo de usuario (si es Admin o User). Dependiendo del tipo de rol, se le otorgará la posibilidad de realizar ciertas peticiones. Los usuarios pueden realizar consultas GET en libros, tiendas o editoriales, pero no pueden realizar peticiones de actualización o eliminación. Los usuarios administradores tienen control para poder realizar estas peticiones.

El usuario cuenta con un email y un username, el cuál no se puede repetir. Utilizamos el borrado lógico en isDeleted para la conservación de los usuarios.

Una tienda tiene una dirección y una lista de libros y clientes. Un cliente también tiene una dirección.

Un Libro tiene asignada una categoría y una editorial. Las editoriales tienen de 0 a muchos libros. La editorial, el libro y el cliente cuentan con una imagen.

El libro cuenta con un stock y un precio del libro, así como cada uno de los elementos que componen al libro: nombre, autor, descripción…

**Borrado lógico:**
- En book empleando active.
- En category empleando isActive.
- En publisher empleando active.
- En order empleando isDeleted.

## Elección de Tecnologías para el Modelo de Datos

### Modelo relacional:

Hemos utilizado un modelo relacional para los pedidos y líneas de pedido.

### SQL:

Para el resto de entidades, hemos utilizado SQL.

La elección de utilizar un modelo relacional para los pedidos y líneas de pedido, y SQL para el resto de entidades, se basa en consideraciones específicas relacionadas con la estructura y las operaciones previstas en el sistema.

### Modelo Relacional para Pedidos y Líneas de Pedido:

**Relaciones Complejas:**
El modelo relacional es especialmente adecuado cuando existen relaciones complejas entre las entidades. En el caso de los pedidos y líneas de pedido, donde se pueden tener múltiples productos asociados a un solo pedido, el modelo relacional proporciona una representación clara y eficiente de estas relaciones.

**Consistencia y Normalización:**
La normalización inherente al modelo relacional ayuda a mantener la consistencia y la integridad de los datos. Al gestionar pedidos, donde es crucial mantener la coherencia de la información, la normalización contribuye a evitar redundancias y posibles incongruencias.

### SQL para el Resto de Entidades:

**Versatilidad y Escalabilidad:**
El uso de SQL permite gestionar de manera eficiente una variedad de operaciones en las diferentes entidades del sistema, desde usuarios y tiendas hasta libros y categorías. SQL es conocido por su versatilidad y escalabilidad, lo que facilita la manipulación y consulta de datos en un amplio espectro de situaciones.

**Consulta y Manipulación de Datos:**
SQL proporciona un lenguaje poderoso para la consulta y manipulación de datos. Esto es esencial para operaciones como la obtención de información del perfil de un usuario, la actualización de datos de una tienda o la gestión de libros y categorías.

## Dependencias

- **Spring Boot Starter Data JPA:** Facilita el acceso a datos mediante Java Persistence API (JPA), permitiendo una integración eficiente con bases de datos relacionales en aplicaciones Spring.
- **Spring Boot Starter Web:** Proporciona configuraciones predeterminadas para el desarrollo de aplicaciones web con Spring MVC. Incluye todo lo necesario para manejar solicitudes HTTP y construir aplicaciones web.
- **Spring Boot Starter Cache:** Integra la capa de caché en la aplicación, lo que permite mejorar el rendimiento almacenando en caché resultados de operaciones costosas, reduciendo así la carga en recursos.
- **Spring Boot Starter Validation:** Proporciona funcionalidades para la validación de datos en la aplicación, asegurando la integridad y consistencia de los datos ingresados.
- **Spring Boot Starter Test:** Incluye dependencias necesarias para realizar pruebas en aplicaciones Spring Boot, facilitando la escritura y ejecución de pruebas unitarias y de integración.
- **Jackson Dataformat XML:** Habilita el manejo de datos en formato XML mediante la biblioteca Jackson, permitiendo la serialización y deserialización de objetos Java en formato XML.
- **Spring Boot Starter Websocket:** Proporciona soporte para WebSocket en aplicaciones Spring Boot, permitiendo la comunicación bidireccional entre el cliente y el servidor en tiempo real.
- **Jackson Datatype JSR310:** Extiende la biblioteca Jackson para proporcionar soporte adicional para tipos de datos de la API de fecha y hora de Java (JSR-310), como LocalDate y LocalDateTime.
- **Spring Boot Starter Security:** Facilita la integración de la seguridad en la aplicación, permitiendo la configuración de autenticación y autorización.
- **Spring Security Test:** Proporciona herramientas y utilidades para realizar pruebas de seguridad en aplicaciones Spring.
- **Java JWT (JSON Web Tokens):** Ofrece soporte para la creación y validación de tokens JWT, utilizados comúnmente en la autenticación y autorización de aplicaciones.
- **SpringDoc OpenAPI Starter Webmvc UI:** Integra la generación de documentación OpenAPI (anteriormente Swagger) para la API de la aplicación, facilitando la comprensión y prueba de la API.
- **Spring Boot Starter Data MongoDB:** Facilita el acceso a datos en bases de datos MongoDB en aplicaciones Spring Boot.
- **de.flapdoodle.embed.mongo:** Proporciona herramientas para embeber una instancia de MongoDB durante las pruebas, eliminando la necesidad de una instancia externa para pruebas de integración.
- **H2 Database (Runtime):** Incorpora la base de datos H2 en tiempo de ejecución, una base de datos en memoria útil para desarrollo y pruebas.
- **Lombok (Compile-Only y Annotation Processor):** Simplifica la creación de clases Java mediante anotaciones, reduciendo la necesidad de escribir código boilerplate y mejorando la legibilidad del código.

## Lista de Endpoints

### Shops:

- **GET /api/shops/{id}:** Obtiene una tienda por su ID.
- **PUT /api/shops/{id}:** Actualiza una tienda existente.
- **DELETE /api/shops/{id}:** Elimina una tienda por su ID.
- **GET /api/shops:** Obtiene todas las tiendas.
- **POST /api/shops:** Crea una nueva tienda.
- **DELETE /api/shops/{id}/clients/{clientId}:** Elimina un cliente de una tienda.
- **PATCH /api/shops/{id}/clients/{clientId}:** Añade un cliente a una tienda.
- **DELETE /api/shops/{id}/books/{bookId}:** Elimina un libro de una tienda.
- **PATCH /api/shops/{id}/books/{bookId}:** Añade un libro a una tienda.

### Publishers:

- **GET /api/publishers/{id}:** Obtiene una editorial dado un ID.
- **PUT /api/publishers/{id}:** Actualiza una editorial.
- **DELETE /api/publishers/{id}:** Elimina una editorial.
- **GET /api/publishers:** Obtiene todas las editoriales.
- **POST /api/publishers:** Crea una editorial.
- **PATCH /api/publishers/image/{id}:** Actualiza la imagen de una editorial.

### Orders:

- **GET /api/orders/{id}:** Obtiene un pedido dado un ID.
- **PUT /api/orders/{id}:** Actualiza un pedido.
- **DELETE /api/orders/{id}:** Elimina un pedido.
- **PUT /api/orders/delete/{id}:** Elimina un pedido de manera simulada.
- **GET /api/orders:** Obtiene todos los pedidos.
- **POST /api/orders:** Crea un pedido.
- **GET /api/orders/user/{id}:** Obtiene un pedido dado el ID de un usuario.
- **GET /api/orders/shop/{id}:** Obtiene un pedido dado el ID de una tienda.
- **GET /api/orders/client/{id}:** Obtiene un pedido dado el ID de un cliente.

### Clients:

- **GET /api/clients/{id}:** Obtiene un cliente dado un ID.
- **PUT /api/clients/{id}:** Actualiza un cliente.
- **DELETE /api/clients/{id}:** Elimina un cliente.
- **GET /api/clients:** Obtiene todos los clientes.
- **POST /api/clients:** Crea un cliente.
- **PATCH /api/clients/{id}/image:** Actualiza la imagen de un cliente.
- **GET /api/clients/email/{email}:** Obtiene un cliente dado un email.

### Categories:

- **GET /api/categories/{id}:** Busca una categoría por ID.
- **PUT /api/categories/{id}:** Actualiza una categoría.
- **DELETE /api/categories/{id}:** Borra una categoría.
- **GET /api/categories:** Obtiene todas las categorías.
- **POST /api/categories:** Crea una categoría.

### Books:

- **GET /api/books/{id}:** Busca un libro dado su ID.
- **PUT /api/books/{id}:** Actualiza un libro.
- **DELETE /api/books/{id}:** Borra un libro.
- **PATCH /api/books/{id}:** Actualiza un libro parcialmente.
- **GET /api/books:** Obtiene todos los libros.
- **POST /api/books:** Crea un libro.
- **PATCH /api/books/image/{id}:** Actualiza la imagen de un libro.

### Authentication:

- **POST /api/auth/signup:** Crea una cuenta.
- **POST /api/auth/signin:** Inicia sesión.

## Autores
- [Madirex](https://github.com/Madirex/)
- [Jaimesalcedo1](https://github.com/jaimesalcedo1/)
- [Danniellgm03](https://github.com/Danniellgm03)
- [Binweiwang](https://github.com/Binweiwang)
- [Alexdor11](https://github.com/alexdor11)
