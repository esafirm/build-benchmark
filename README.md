```mermaid
graph TD
    App --> Login
    App --> Toaster
    Toaster --> Login-API
    Toaster --> Infra
    Login --> Login-API
    Login --> Infra
```
