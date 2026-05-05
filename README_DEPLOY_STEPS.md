# AI App Docker + Azure + GitHub Actions setup

## Local run

From project root:

```bash
cd Backend
docker compose up --build
```

Frontend: http://localhost:5173  
API Gateway: http://localhost:8080  
Eureka: http://localhost:8761

## GitHub Secrets required

For build/push:

```text
ACR_LOGIN_SERVER=yourregistry.azurecr.io
ACR_USERNAME=your-acr-username
ACR_PASSWORD=your-acr-password
VITE_API_BASE_URL=https://your-api-gateway-webapp.azurewebsites.net
```

For deploy workflow, add your Azure Web App names:

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

Add publish profile XML secrets for each web app:

```text
AZURE_WEBAPP_PUBLISH_PROFILE_DISCOVERY
AZURE_WEBAPP_PUBLISH_PROFILE_AUTH
AZURE_WEBAPP_PUBLISH_PROFILE_USER
AZURE_WEBAPP_PUBLISH_PROFILE_POST
AZURE_WEBAPP_PUBLISH_PROFILE_INTERACTION
AZURE_WEBAPP_PUBLISH_PROFILE_MEDIA
AZURE_WEBAPP_PUBLISH_PROFILE_AI
AZURE_WEBAPP_PUBLISH_PROFILE_GATEWAY
AZURE_WEBAPP_PUBLISH_PROFILE_FRONTEND
```

## Azure App Settings

Set these in each backend Web App Configuration.

### auth-service

```text
DB_URL=jdbc:mysql://<mysql-host>:3306/auth_db?useSSL=true&allowPublicKeyRetrieval=true
DB_USERNAME=<mysql-user>
DB_PASSWORD=<mysql-password>
JWT_SECRET=<long-secret-minimum-32-chars>
EUREKA_URL=http://<discovery-private-url>:8761/eureka
```

### user-service

```text
DB_URL=jdbc:mysql://<mysql-host>:3306/user_db?useSSL=true&allowPublicKeyRetrieval=true
DB_USERNAME=<mysql-user>
DB_PASSWORD=<mysql-password>
EUREKA_URL=http://<discovery-private-url>:8761/eureka
```

### post-service

```text
DB_URL=jdbc:mysql://<mysql-host>:3306/post_db?useSSL=true&allowPublicKeyRetrieval=true
DB_USERNAME=<mysql-user>
DB_PASSWORD=<mysql-password>
REDIS_HOST=<redis-host>
REDIS_PORT=6379
EUREKA_URL=http://<discovery-private-url>:8761/eureka
```

### interaction-service

```text
DB_URL=jdbc:mysql://<mysql-host>:3306/interaction_db?useSSL=true&allowPublicKeyRetrieval=true
DB_USERNAME=<mysql-user>
DB_PASSWORD=<mysql-password>
EUREKA_URL=http://<discovery-private-url>:8761/eureka
```

### media-service

```text
UPLOAD_DIR=/app/uploads
MEDIA_BASE_URL=https://your-media-webapp.azurewebsites.net/uploads
EUREKA_URL=http://<discovery-private-url>:8761/eureka
```

### api-gateway

```text
FRONTEND_ORIGIN=https://your-frontend-webapp.azurewebsites.net
EUREKA_URL=http://<discovery-private-url>:8761/eureka
```

## Important

For real production microservices, Azure Container Apps or AKS is better than many separate Web Apps because service discovery/private networking is easier. This zip still supports Docker Compose locally and ACR/GitHub Actions image build for Azure.
