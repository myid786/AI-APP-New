# Azure live checklist

## Current Azure resources found

- Resource group: `rg-ai-media-platform`
- Web App: `aiappproject`
- MySQL Flexible Server: `aiappproject-server.mysql.database.azure.com`
- Blob Storage account: `aiappmedia786`
- Blob Storage container: `media`
- MySQL server public network access: disabled
- Web App VNet integration: enabled

## Databases created

- `auth_db`
- `user_db`
- `post_db`
- `interaction_db`
- `media_db`

The Spring services already read Azure App Settings through these environment variables:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `EUREKA_URL`
- `REDIS_HOST`
- `REDIS_PORT`
- `FRONTEND_ORIGIN`
- `MEDIA_BASE_URL`
- `AZURE_STORAGE_CONNECTION_STRING`
- `AZURE_STORAGE_CONTAINER`

## Important deployment note

The current Azure-generated workflow deploys only `Backend/ai-service` to the single Web App named `aiappproject`.

For the full application to be live, deploy these as separate container apps or Web Apps:

- `discovery-service`
- `auth-service`
- `user-service`
- `post-service`
- `interaction-service`
- `media-service`
- `ai-service`
- `api-gateway`
- `frontend`

The repo now includes `.github/workflows/azure-production-containers.yml` as a manual production workflow for that model.

## GitHub secrets needed for the production container workflow

Azure login:

```text
AZURE_CLIENT_ID
AZURE_TENANT_ID
AZURE_SUBSCRIPTION_ID
AZURE_RESOURCE_GROUP=rg-ai-media-platform
```

Azure Container Registry:

```text
ACR_LOGIN_SERVER
ACR_USERNAME
ACR_PASSWORD
```

Azure Web App names:

```text
AZURE_WEBAPP_DISCOVERY
AZURE_WEBAPP_AUTH
AZURE_WEBAPP_USER
AZURE_WEBAPP_POST
AZURE_WEBAPP_INTERACTION
AZURE_WEBAPP_MEDIA
AZURE_WEBAPP_AI
AZURE_WEBAPP_GATEWAY
AZURE_WEBAPP_FRONTEND
```

Database and service settings:

```text
DB_HOST=aiappproject-server.mysql.database.azure.com
DB_USERNAME=<mysql-admin-or-app-user>
DB_PASSWORD=<mysql-password>
JWT_SECRET=<long-random-secret>
EUREKA_URL=https://<discovery-webapp>.azurewebsites.net/eureka
VITE_API_BASE_URL=https://<gateway-webapp>.azurewebsites.net
FRONTEND_ORIGIN=https://<frontend-webapp>.azurewebsites.net
MEDIA_BASE_URL=https://aiappmedia786.blob.core.windows.net/media
AZURE_STORAGE_CONNECTION_STRING=<storage-connection-string>
AZURE_STORAGE_CONTAINER=media
```

Redis is still needed for `post-service`:

```text
REDIS_HOST=<azure-cache-host>
REDIS_PORT=6380
```

## Azure App Settings per service

### auth-service

```text
DB_URL=jdbc:mysql://aiappproject-server.mysql.database.azure.com:3306/auth_db?useSSL=true&requireSSL=true&serverTimezone=UTC
DB_USERNAME=<mysql-user>
DB_PASSWORD=<mysql-password>
JWT_SECRET=<long-random-secret>
EUREKA_URL=https://<discovery-webapp>.azurewebsites.net/eureka
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false
WEBSITES_PORT=8081
```

### user-service

```text
DB_URL=jdbc:mysql://aiappproject-server.mysql.database.azure.com:3306/user_db?useSSL=true&requireSSL=true&serverTimezone=UTC
DB_USERNAME=<mysql-user>
DB_PASSWORD=<mysql-password>
EUREKA_URL=https://<discovery-webapp>.azurewebsites.net/eureka
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false
WEBSITES_PORT=8082
```

### post-service

```text
DB_URL=jdbc:mysql://aiappproject-server.mysql.database.azure.com:3306/post_db?useSSL=true&requireSSL=true&serverTimezone=UTC
DB_USERNAME=<mysql-user>
DB_PASSWORD=<mysql-password>
REDIS_HOST=<azure-cache-host>
REDIS_PORT=6380
EUREKA_URL=https://<discovery-webapp>.azurewebsites.net/eureka
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false
WEBSITES_PORT=8083
```

### interaction-service

```text
DB_URL=jdbc:mysql://aiappproject-server.mysql.database.azure.com:3306/interaction_db?useSSL=true&requireSSL=true&serverTimezone=UTC
DB_USERNAME=<mysql-user>
DB_PASSWORD=<mysql-password>
EUREKA_URL=https://<discovery-webapp>.azurewebsites.net/eureka
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false
WEBSITES_PORT=8084
```

### api-gateway

```text
FRONTEND_ORIGIN=https://<frontend-webapp>.azurewebsites.net
EUREKA_URL=https://<discovery-webapp>.azurewebsites.net/eureka
WEBSITES_PORT=8080
```

### media-service

```text
DB_URL=jdbc:mysql://aiappproject-server.mysql.database.azure.com:3306/media_db?useSSL=true&requireSSL=true&serverTimezone=UTC
DB_USERNAME=<mysql-user>
DB_PASSWORD=<mysql-password>
AZURE_STORAGE_CONNECTION_STRING=<storage-connection-string>
AZURE_STORAGE_CONTAINER=media
MEDIA_BASE_URL=https://aiappmedia786.blob.core.windows.net/media
EUREKA_URL=https://<discovery-webapp>.azurewebsites.net/eureka
JPA_DDL_AUTO=update
JPA_SHOW_SQL=false
WEBSITES_PORT=8085
```

### ai-service

```text
EUREKA_URL=https://<discovery-webapp>.azurewebsites.net/eureka
WEBSITES_PORT=8086
```

### frontend

```text
WEBSITES_PORT=80
```
