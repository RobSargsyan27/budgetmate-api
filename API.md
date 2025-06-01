# API

## API prefix - `/v1`.

## No authorization is required.

| Endpoint                                        | Method | Parameter    | In         | Required |
|-------------------------------------------------|--------|--------------|------------|----------|
| `/api/v1/auth/validate-token`                   | POST   | body         | body       | ✔        |
| `/api/v1/auth/register`                         | POST   | body         | body       | ✔        |
| `/api/v1/auth/login`                            | POST   | body         | body       | ✔        |
| `/api/v1/auth/register/confirm/{email}/{token}` | GET    | email, token | path, path | ✔, ✔     |

## Authorization is required.

| Endpoint                                                             | Method | Parameter                      | In               | Required | Access      |
|----------------------------------------------------------------------|--------|--------------------------------|------------------|----------|-------------|
| `/api/v1/record/{limit}/{offset}`                                    | POST   | limit, offset, body            | path, path, body | ✔, ✔, ✔  | Bearer Auth |
| `/api/v1/record/transfer`                                            | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/record/report`                                              | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/record/income`                                              | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/record/expense`                                             | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/record/count`                                               | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/record`                                                     | GET    |                                |                  |          | Bearer Auth |
| `/api/v1/record/record-categories`                                   | GET    |                                |                  |          | Bearer Auth |
| `/api/v1/record/{id}`                                                | GET    | id                             | path             | ✔        | Bearer Auth |
| `/api/v1/record/{id}`                                                | DELETE | id                             | path             | ✔        | Bearer Auth |
| `/api/v1/record/{id}`                                                | PATCH  | id, body                       | path, body       | ✔, ✔     | Bearer Auth |
|                                                                      |        |                                |                  |          |             |
| `/api/v1/budget`                                                     | GET    |                                |                  |          | Bearer Auth |
| `/api/v1/budget`                                                     | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/budget/report`                                              | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/budget/current-balance`                                     | GET    |                                |                  |          | Bearer Auth |
| `/api/v1/budget/{id}`                                                | GET    | id                             | path             | ✔        | Bearer Auth |
| `/api/v1/budget/{id}`                                                | DELETE | id                             | path             | ✔        | Bearer Auth |
| `/api/v1/budget/{id}`                                                | PATCH  | id, body                       | path, body       | ✔, ✔     | Bearer Auth |
|                                                                      |        |                                |                  |          |             |
| `/api/v1/account`                                                    | GET    |                                |                  |          | Bearer Auth |
| `/api/v1/account`                                                    | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/account/{id}`                                               | GET    | id                             | path             | ✔        | Bearer Auth |
| `/api/v1/account/{id}`                                               | DELETE | id                             | path             | ✔        | Bearer Auth |
| `/api/v1/account/{id}`                                               | PATCH  | id, body                       | path, body       | ✔, ✔     | Bearer Auth |
| `/api/v1/account/{id}/{status}`                                      | POST   | id, status                     | path, path       | ✔, ✔     | Bearer Auth |
| `/api/v1/account/existing`                                           | POST   | body                           | body             | ✔        | Bearer Auth |
|                                                                      |        |                                |                  |          |             |
| `/api/v1/user/{id}`                                                  | DELETE | id                             | path             | ✔        | Bearer Auth |
| `/api/v1/user/{id}`                                                  | PATCH  | id, body                       | path, body       | ✔, ✔     | Bearer Auth |
| `/api/v1/user`                                                       | GET    |                                |                  |          | Bearer Auth |
| `/api/v1/user/notifications`                                         | GET    |                                |                  |          | Bearer Auth |
|                                                                      |        |                                |                  |          |             |
| `/api/v1/auth/validate-token`                                        | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/auth/register`                                              | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/auth/login`                                                 | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v1/auth/register/confirm/{email}/{token}`                      | GET    | email, token                   | path, path       | ✔, ✔     | Bearer Auth |
|                                                                      |        |                                |                  |          |             |
| `/api/v1/analytics/overview/{startDate}/{endDate}`                   | GET    | startDate, endDate             | path, path       | ✔, ✔     | Bearer Auth |
| `/api/v1/analytics/overview-line/{startDate}/{endDate}/{recordType}` | GET    | startDate, endDate, recordType | path, path, path | ✔, ✔, ✔  | Bearer Auth |
| `/api/v1/analytics/dashboard`                                        | GET    |                                |                  |          | Bearer Auth |
| `/api/v1/analytics/dashboard/expenses-line-chart`                    | GET    |                                |                  |          | Bearer Auth |
| `/api/v1/analytics/dashboard/categories-pie`                         | GET    |                                |                  |          | Bearer Auth |

