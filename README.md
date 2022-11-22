# vMark-Backend
vMark E-commercial System Backend

## RESTful API
### Authorize API
Controller: com.vmark.backend.controller.AuthController

| URL               | Method | Params                     | Description                               |
|:------------------|:-------|:---------------------------|:------------------------------------------|
| /api/auth/captcha | Get    | (Empty)                    | Generate new captcha for currrent session |
| /api/auth/login   | Post   | account, password, captcha | User login for current session            |
| /api/auth/logout  | Get    | (Empty)                    | User logout for current session           |
| /api/auth/info    | Get    | (Empty)                    | Get current user info                     |

