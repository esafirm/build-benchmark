## Description

This is a mini project to find out the behavior of build related code.

### Module Structure

```mermaid
graph TD
    App --> Login
    App --> Toaster
    Toaster --> Login-API
    Toaster --> Infra
    Login --> Login-API
    Login --> Infra
```
