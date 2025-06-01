# API

## API prefix - `/v2`.

## No authorization is required.

| Endpoint                                        | Method | Parameter    | In         | Required |
|-------------------------------------------------|--------|--------------|------------|----------|
| `/api/v2/auth/validate-token`                   | POST   | body         | body       | ✔        |
| `/api/v2/auth/register`                         | POST   | body         | body       | ✔        |
| `/api/v2/auth/login`                            | POST   | body         | body       | ✔        |
| `/api/v2/auth/register/confirm/{email}/{token}` | GET    | email, token | path, path | ✔, ✔     |

## Authorization is required.

| Endpoint                                                             | Method | Parameter                      | In               | Required | Access      |
|----------------------------------------------------------------------|--------|--------------------------------|------------------|----------|-------------|
| `/api/v2/record/{limit}/{offset}`                                    | POST   | limit, offset, body            | path, path, body | ✔, ✔, ✔  | Bearer Auth |
| `/api/v2/record/transfer`                                            | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/record/report`                                              | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/record/income`                                              | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/record/expense`                                             | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/record/count`                                               | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/record`                                                     | GET    |                                |                  |          | Bearer Auth |
| `/api/v2/record/record-categories`                                   | GET    |                                |                  |          | Bearer Auth |
| `/api/v2/record/{id}`                                                | GET    | id                             | path             | ✔        | Bearer Auth |
| `/api/v2/record/{id}`                                                | DELETE | id                             | path             | ✔        | Bearer Auth |
| `/api/v2/record/{id}`                                                | PATCH  | id, body                       | path, body       | ✔, ✔     | Bearer Auth |
|                                                                      |        |                                |                  |          |             |
| `/api/v2/budget`                                                     | GET    |                                |                  |          | Bearer Auth |
| `/api/v2/budget`                                                     | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/budget/report`                                              | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/budget/current-balance`                                     | GET    |                                |                  |          | Bearer Auth |
| `/api/v2/budget/{id}`                                                | GET    | id                             | path             | ✔        | Bearer Auth |
| `/api/v2/budget/{id}`                                                | DELETE | id                             | path             | ✔        | Bearer Auth |
| `/api/v2/budget/{id}`                                                | PATCH  | id, body                       | path, body       | ✔, ✔     | Bearer Auth |
|                                                                      |        |                                |                  |          |             |
| `/api/v2/account`                                                    | GET    |                                |                  |          | Bearer Auth |
| `/api/v2/account`                                                    | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/account/{id}`                                               | GET    | id                             | path             | ✔        | Bearer Auth |
| `/api/v2/account/{id}`                                               | DELETE | id                             | path             | ✔        | Bearer Auth |
| `/api/v2/account/{id}`                                               | PATCH  | id, body                       | path, body       | ✔, ✔     | Bearer Auth |
| `/api/v2/account/{id}/{status}`                                      | POST   | id, status                     | path, path       | ✔, ✔     | Bearer Auth |
| `/api/v2/account/existing`                                           | POST   | body                           | body             | ✔        | Bearer Auth |
|                                                                      |        |                                |                  |          |             |
| `/api/v2/user/{id}`                                                  | DELETE | id                             | path             | ✔        | Bearer Auth |
| `/api/v2/user/{id}`                                                  | PATCH  | id, body                       | path, body       | ✔, ✔     | Bearer Auth |
| `/api/v2/user`                                                       | GET    |                                |                  |          | Bearer Auth |
| `/api/v2/user/notifications`                                         | GET    |                                |                  |          | Bearer Auth |
|                                                                      |        |                                |                  |          |             |
| `/api/v2/auth/validate-token`                                        | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/auth/register`                                              | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/auth/login`                                                 | POST   | body                           | body             | ✔        | Bearer Auth |
| `/api/v2/auth/register/confirm/{email}/{token}`                      | GET    | email, token                   | path, path       | ✔, ✔     | Bearer Auth |
|                                                                      |        |                                |                  |          |             |
| `/api/v2/analytics/overview/{startDate}/{endDate}`                   | GET    | startDate, endDate             | path, path       | ✔, ✔     | Bearer Auth |
| `/api/v2/analytics/overview-line/{startDate}/{endDate}/{recordType}` | GET    | startDate, endDate, recordType | path, path, path | ✔, ✔, ✔  | Bearer Auth |
| `/api/v2/analytics/dashboard`                                        | GET    |                                |                  |          | Bearer Auth |
| `/api/v2/analytics/dashboard/expenses-line-chart`                    | GET    |                                |                  |          | Bearer Auth |
| `/api/v2/analytics/dashboard/categories-pie`                         | GET    |                                |                  |          | Bearer Auth |

