# vMark-Backend
vMark E-commercial System Backend

## RESTful API
### Attachment API
#### Controller
com.vmark.backend.controller.AttachmentController

#### Method
> URL: /api/attachment/upload
>
> Method: Post (multipart/form-data)
>
> Params: \<file>
>
> Description: Upload attachment
>
> Permission: Admin

> URL: /api/attachment/rename
>
> Method: Get
>
> Params: \<aid>, \<new_name>
>
> Description: Rename attachment
>
> Permission: Admin

> URL: /api/attachment/delete
>
> Method: Get
>
> Params: \<aid>
>
> Description: Delete attachment
> 
> Permission: Admin

> URL: /api/attachment/get
> 
> Method: Get
> 
> Params: \<aid>
> 
> Description: Get attachment contents
> 
> Permission: Everyone

> URL: /api/attachment/info
> 
> Method: Get
> 
> Params: \[aid], \[s], \[p], \[on], \[ot]
> 
> Description: Get attachment infomation
> 
> Permission: Admin

### Authorize API
Controller: com.vmark.backend.controller.AuthController

| URL               | Method | Params                     | Description                               |
|:------------------|:-------|:---------------------------|:------------------------------------------|
| /api/auth/captcha | Get    | (Empty)                    | Generate new captcha for currrent session |
| /api/auth/login   | Post   | account, password, captcha | User login for current session            |
| /api/auth/logout  | Get    | (Empty)                    | User logout for current session           |
| /api/auth/info    | Get    | (Empty)                    | Get current user info                     |

