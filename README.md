# User Management System

This is a User Management System built with Spring Boot and H2 in-memory database. It provides RESTful APIs for managing accounts, projects, black lists.

## Setup Instructions

### Prerequisites

- Java 17 or later
- Maven 3.8+

### Installation

1. **Clone the repository:**
   ```sh
   git clone <repository-url>
   cd user-management-system
   ```

2. **JWT Configuration:**
    - In the `application.properties` file, configure JWT settings as follows:
   ```properties
      jwt.secret=your-secret-string
   ```

3. **Build the project:**
   ```sh
   mvn clean install
   ```

4. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```

## API Documentation in Swagger UI

This application includes API documentation using Swagger UI.

### Accessing Swagger UI

After starting the application, you can access the Swagger UI at:

```
http://localhost:8080/api/swagger-ui/index.html
```

## API Documentation

### Base URL

```
http://localhost:8080/api/v1
```

### Account Endpoints

#### 1. Register an account (Anyone can access)

**POST** `/accounts/register`

##### Request Body:

```json
{
  "name": "string",
  "password": "string",
  "phoneNumber": "string",
  "nrcNumber": "string"
}
```

##### Response:

```json
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {
    "id": "UUID",
    "name": "string",
    "phoneNumber": "string",
    "nrcNumber": "string",
    "role": "string"
  },
  "errors": {}
}
```
#### 2. Approve an account registration (Superuser can access)

**PATCH** `/accounts/{id}/approve`

##### Path variable:

``` id - UUID ```

##### Response:
```json
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {
    "id": "UUID",
    "name": "string",
    "phoneNumber": "string",
    "nrcNumber": "string",
    "status": "string",
    "role": "string",
    "createdAt": "string (ISO 8601 datetime)",
    "updatedAt": "string (ISO 8601 datetime)"
  },
  "errors": {}
}
```

#### 3. Cancel an approved account (Superuser can access)

**PATCH** `/accounts/{id}/cancel-approval`

##### Path variable:

``` id - UUID ```

##### Response:

```json
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {
    "id": "UUID",
    "name": "string",
    "phoneNumber": "string",
    "nrcNumber": "string",
    "status": "string",
    "role": "string",
    "createdAt": "string (ISO 8601 datetime)",
    "updatedAt": "string (ISO 8601 datetime)"
  },
  "errors": {}
}
```

#### 4. Reject an account registration (Superuser can access)

**PATCH** `/accounts/{id}/reject`

##### Path variable:

``` id - UUID ```

##### Response:

```json
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {
    "id": "UUID",
    "name": "string",
    "phoneNumber": "string",
    "nrcNumber": "string",
    "status": "string",
    "role": "string",
    "createdAt": "string (ISO 8601 datetime)",
    "updatedAt": "string (ISO 8601 datetime)"
  },
  "errors": {}
}
```

#### 5. Cancel a rejected account (Superuser can access)

**PATCH** `/accounts/{id}/cancel-rejection`

##### Path variable:

``` id - UUID ```

##### Response:

```json
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {
    "id": "UUID",
    "name": "string",
    "phoneNumber": "string",
    "nrcNumber": "string",
    "status": "string",
    "role": "string",
    "createdAt": "string (ISO 8601 datetime)",
    "updatedAt": "string (ISO 8601 datetime)"
  },
  "errors": {}
}
```

#### 6. Block an account (Superuser can access)

**PATCH** `/accounts/{id}/block`

##### Path variable:

``` id - UUID ```

##### Response:

```json
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {
    "id": "UUID",
    "name": "string",
    "phoneNumber": "string",
    "nrcNumber": "string",
    "status": "string",
    "role": "string",
    "createdAt": "string (ISO 8601 datetime)",
    "updatedAt": "string (ISO 8601 datetime)"
  },
  "errors": {}
}
```

#### 7. Unblock account (Superuser can access)

**PATCH** `/accounts/{id}/unblock`

##### Path variable:

``` id - UUID ```

##### Response:

```json
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {
    "id": "UUID",
    "name": "string",
    "phoneNumber": "string",
    "nrcNumber": "string",
    "status": "string",
    "role": "string",
    "createdAt": "string (ISO 8601 datetime)",
    "updatedAt": "string (ISO 8601 datetime)"
  },
  "errors": {}
}
```

#### 8. View an account (User and Superuser can access)

**GET** `/accounts/{id}`

##### Path variable:

``` id - UUID ```

##### Response:

```json
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {
    "id": "UUID",
    "name": "string",
    "phoneNumber": "string",
    "nrcNumber": "string",
    "status": "string",
    "role": "string",
    "createdAt": "string (ISO 8601 datetime)",
    "updatedAt": "string (ISO 8601 datetime)"
  },
  "errors": {}
}
```

#### 9. Filter accounts (Superuser can access)

**GET** `/accounts`

##### Request parameters:

```json
{
  "page": "number",
  "size": "number",
  "order": "string (asc/ desc)",
  "sortBy": "string",
  "status": "string",
  "role": "string"
}
```

##### Response:

```json
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
   "data": {
      "content": [
         {}
      ],
      "pagination": {
         "currentPage": "number",
         "pageSize": "number",
         "totalPages": "number",
         "totalItems": "number",
         "nextPageUrl": "string",
         "prevPageUrl": "string"
      }
   },
  "errors": {}
}
```

### Project Endpoints

#### 1. Create a project (User can access)

**POST** `/projects`

##### Request Body:

```json
{
   "title": "string",
   "description": "string"
} 
```

##### Response:

```json 
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {
    "id": "UUID",
    "title": "string",
    "description": "string"
  },
  "errors": {}
}
```

#### 2. View a project (User and Superuser can access)

**GET** `/projects/{id}`

##### Path variable:

``` id - UUID ```

##### Response:

```json
{
   "timestamp": "string (ISO 8601 datetime)",
   "status": "http status code",
   "message": "string",
   "data": {
      "id": "UUID",
      "title": "string",
      "description": "string"
   },
   "errors": {}
} 
```

#### 3. Update a project (User can access)

**PATCH** `/projects/{id}/update`

##### Request Body:

```json 
{
  "title": "string",
  "description": "string"
}
```

##### Response:

```json 
{
   "timestamp": "string (ISO 8601 datetime)",
   "status": "http status code",
   "message": "string",
   "data": {
      "id": "UUID",
      "title": "string",
      "description": "string"
   },
   "errors": {}
}
```

#### 4. Soft-delete a project (User can access)

**PATCH** `/projects/{id}/erase`

##### Path variable:

``` id - UUID ```

##### Response:

```json 
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {},
  "errors": {}
}
```

#### 5. Delete a project (User can access)

**DELETE** `/projects/{id}`

##### Path variable:

``` id - UUID ```

##### Response:
```json 
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {},
  "errors": {}
}
```

#### 6. Filter projects (User and Superuser can access)

**GET** `/projects`

##### Request parameters:

```json 
{
  "page": "number",
  "size": "number",
  "order": "string (asc/ desc)",
  "sortBy": "string"
}
```

##### Response:

```json 
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
   "data": {
      "content": [
         {}
      ],
      "pagination": {
         "currentPage": "number",
         "pageSize": "number",
         "totalPages": "number",
         "totalItems": "number",
         "nextPageUrl": "string",
         "prevPageUrl": "string"
      }
   },
  "errors": {}
}
```

### Blacklist Endpoints

#### 1. Create a blacklist record (Superuser can access)

**POST** `/blacklists/{accountID}`

##### Path variable:

``` id - UUID ```

##### Response:

```json 
{

   "timestamp": "string (ISO 8601 datetime)",
   "status": "http status code",
   "message": "string",
   "data": {
      "id": "UUID",
      "phoneNumber": "string",
      "nrcNumber": "string",
      "blackListedBy": {
         "id": "UUID",
         "name": "string",
         "phoneNumber": "string",
         "nrcNumber": "string",
         "status": "string",
         "role": "string",
         "createdAt": "string (ISO 8601 datetime)",
         "updatedAt": "string (ISO 8601 datetime)"
      }
   },
   "errors": {}
}
```

#### 2. View a blacklist record (Superuser can access)

#### 3. Delete a blacklist record (Superuser can access)

#### 4. Filter blacklist records (Superuser can access)

### Auth Endpoints

#### 1. Sign-in (Anyone can access)

**POST** `/auth/signin`

##### Request Body:

```json
{
  "phoneNumber": "string",
  "password": "string"
}
```

##### Response:

```json
{
  "timestamp": "string (ISO 8601 datetime)",
  "status": "http status code",
  "message": "string",
  "data": {
     "accessToken": "string"
  },
  "errors": {}
}
```

#### 2. Refresh token (Anyone can access)

**POST** `/auth/refresh`

##### Response:
```json
{
   "timestamp": "string (ISO 8601 datetime)",
   "status": "http status code",
   "message": "string",
   "data": {
      "accessToken": "string"
   },
   "errors": {}
}
```

---

## License

This project is for educational purposes as part of an internship assignment.

