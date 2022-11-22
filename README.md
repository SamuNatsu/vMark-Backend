# vMark-Backend
vMark E-commercial System Backend

## RESTful API
### Attachment API
#### Controller
com.vmark.backend.controller.AttachmentController

#### Method
> **URL:** `/api/attachment/delete`
>
> **Method:** `Get`
>
> **Params:** `\<aid>`
>
> **Description:** Delete attachment
>
> **Permission:** Admin
> 
> **Message:** `message.invalid.aid`, `message.fail.permission`, `message.fail.database`

> URL: /api/attachment/get
>
> **Method:** Get (dst: image/png)
>
> **Params:** \<aid>
>
> **Description:** Get attachment contents
>
> **Permission:** Everyone
> 
> **Message:** (Empty)

> URL: /api/attachment/info
>
> **Method:** Get
>
> **Params:** \[aid], \[s], \[p], \[on], \[ot]
>
> **Description:** Get attachment infomation
>
> **Permission:** Admin
> 
> **Message:** `message.fail.permission`, `message.invalid.aid`, `message.invalid.page`, `message.invalid.order_name`, `message.invalid.order_type`, `message.fail.database`

> URL: /api/attachment/rename
>
> **Method:** Get
>
> **Params:** \<aid>, \<new_name>
>
> **Description:** Rename attachment
>
> **Permission:** Admin
> 
> **Message:** `message.invalid.aid`, `message.invalid.new_name`, `message.fail.permission`, `message.fail.database`

> URL: /api/attachment/upload
>
> **Method:** Post (src: multipart/form-data)
>
> **Params:** \<file>
>
> **Description:** Upload attachment
>
> **Permission:** Admin
> 
> **Message:** `message.fail.permission`, `message.fail.upload`, `message.fail.database`

### Authorize API
Controller: com.vmark.backend.controller.AuthController

| URL               | Method | Params                     | Description                               |
|:------------------|:-------|:---------------------------|:------------------------------------------|
| /api/auth/captcha | Get    | (Empty)                    | Generate new captcha for currrent session |
| /api/auth/login   | Post   | account, password, captcha | User login for current session            |
| /api/auth/logout  | Get    | (Empty)                    | User logout for current session           |
| /api/auth/info    | Get    | (Empty)                    | Get current user info                     |

