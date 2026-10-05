# Smart Banking Backend - Minimum Working Review Guide

## Modules implemented

1. User & Authentication
2. Account CRUD
3. Deposit
4. Withdraw
5. Transfer with `@Transactional`
6. Transaction history
7. JWT-protected banking endpoints

Fraud rules, transaction flagging, AI explanations, advanced validation, and frontend are intentionally out of scope for this minimum working version.

## Request flow

HTTP request -> Spring Security/JWT filter -> Controller -> Service -> Repository -> JPA/Hibernate -> MySQL

## Postman test order

### 1. Register
`POST http://localhost:8080/users`
```json
{
  "name": "Sanjit",
  "email": "sanjit-review@example.com",
  "password": "test12345"
}
```
Expected: `201 Created`

### 2. Login
`POST http://localhost:8080/auth/login`
```json
{
  "email": "sanjit-review@example.com",
  "password": "test12345"
}
```
Expected: `200 OK` with a JWT token.

Use the returned token as:
`Authorization: Bearer <token>`

### 3. Create account
`POST http://localhost:8080/accounts?userId=1`
```json
{
  "accountType": "SAVINGS"
}
```
Expected: `201 Created`.

Use the returned account `id` for the next requests.

### 4. Deposit
`POST http://localhost:8080/transactions/deposit/{accountId}`
```json
{
  "amount": 5000,
  "description": "Initial deposit"
}
```

### 5. Withdraw
`POST http://localhost:8080/transactions/withdraw/{accountId}`
```json
{
  "amount": 1000,
  "description": "Cash withdrawal"
}
```

### 6. Create a second user and account
Register/login another user and create another account.

### 7. Transfer
`POST http://localhost:8080/transactions/transfer`
```json
{
  "sourceAccountId": 1,
  "destinationAccountId": 2,
  "amount": 500,
  "description": "Test transfer"
}
```

The transfer is wrapped in one database transaction so the debit, credit, and transaction record succeed together or roll back together if an exception occurs.

### 8. Transaction history
`GET http://localhost:8080/transactions/account/{accountId}`

### Account CRUD
- `GET /accounts/{id}`
- `GET /accounts/user/{userId}`
- `PUT /accounts/{id}`
- `DELETE /accounts/{id}`

## Review concepts to remember

- Entity: Java class mapped to a database table.
- DTO: object used to carry API request/response data without exposing the entity directly.
- Repository: persistence layer; Spring Data JPA generates common database operations.
- Service: business logic layer.
- Controller: HTTP/API layer.
- JPA: Java persistence specification used to map objects to relational data.
- Hibernate: JPA implementation used by the application.
- JWT: signed token used to carry authentication claims between client and server.
- BCrypt: password hashing algorithm; raw passwords are not stored.
- `@Transactional`: makes a group of database operations atomic.
- `BigDecimal`: used for monetary values instead of floating-point types.
- 201: resource created.
- 200: request succeeded.
- 401: authentication is missing/invalid.
- 403: authenticated but not authorized for the requested resource.
- 404: requested resource does not exist.
- 409: request conflicts with existing data, such as duplicate email.
