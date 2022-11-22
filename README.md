# vMark-Backend
vMark E-commercial System Backend

## RESTful API
### Attachment API
#### Controller
com.vmark.backend.controller.AttachmentController

#### Method
> **URL:** `/api/attachment/delete`
>
> **Method:** `Post`
>
> **Params:** `<aid>`
>
> **Description:** Delete attachment
>
> **Permission:** Admin
> 
> **Message:** `message.invalid.aid`, `message.fail.permission`, `message.fail.database`

> **URL:** `/api/attachment/get`
>
> **Method:** `Get` (dst: image/png)
>
> **Params:** `<aid>`
>
> **Description:** Get attachment contents
>
> **Permission:** Everyone
> 
> **Message:** (None)

> **URL:** `/api/attachment/info`
>
> **Method:** `Get`
>
> **Params:** `[aid]`, `[s]`, `[p]`, `[on]`, `[ot]`
>
> **Description:** Get attachment infomation
>
> **Permission:** Admin
> 
> **Message:** `message.fail.permission`, `message.invalid.aid`, `message.invalid.page`, `message.invalid.order_name`, `message.invalid.order_type`, `message.fail.database`

> **URL:** `/api/attachment/rename`
>
> **Method:** `Post`
>
> **Params:** `<aid>`, `<new_name>`
>
> **Description:** Rename attachment
>
> **Permission:** Admin
> 
> **Message:** `message.invalid.aid`, `message.invalid.new_name`, `message.fail.permission`, `message.fail.database`

> **URL:** `/api/attachment/upload`
>
> **Method:** `Post` (src: multipart/form-data)
>
> **Params:** `<file>`
>
> **Description:** Upload attachment
>
> **Permission:** Admin
> 
> **Message:** `message.fail.permission`, `message.fail.upload`, `message.fail.database`

### Authorize API
#### Controller
com.vmark.backend.controller.AuthController

#### Method
> **URL:** `/api/auth/captcha`
> 
> **Method:** `Get` (dst: image/gif)
> 
> **Params:** (None)
> 
> **Description:** Generate new captcha for current session
> 
> **Permission:** Everyone
> 
> **Message:** (None)

> **URL:** `/api/auth/info`
> 
> **Method:** `Get`
> 
> **Params:** (None)
> 
> **Description:** Get current user infomation
> 
> **Permission:** Logined
> 
> **Message:** `message.auth.no_login`

> **URL:** `/api/auth/login`
> 
> **Method:** Post
> 
> **Params:** `<account>`, `<password>`, `<captcha>`
> 
> **Permission:** Everyone
> 
> **Message:** `message.invalid.account`, `message.invalid.password`, `message.invalid.captcha`, `message.auth.already_login`, `message.fail.login`

> **URL:** `/api/auth/logout`
> 
> **Method:** Post
> 
> **Params:** (None)
> 
> **Permission:** Everyone
> 
> **Message:** (None)
