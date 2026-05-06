# Azure Resource Paths

## Single App Link

- Frontend: `https://aiappmedia786.z1.web.core.windows.net/`

The frontend is built with:

```text
VITE_API_BASE_URL=https://ai-media-gateway-786.azurewebsites.net
```

## API Gateway

- Gateway root: `https://ai-media-gateway-786.azurewebsites.net`
- Auth routes: `https://ai-media-gateway-786.azurewebsites.net/api/auth`
- User routes: `https://ai-media-gateway-786.azurewebsites.net/api/users`
- Post routes: `https://ai-media-gateway-786.azurewebsites.net/api/posts`
- Interaction routes: `https://ai-media-gateway-786.azurewebsites.net/api/interactions`
- Media upload route: `https://ai-media-gateway-786.azurewebsites.net/api/media/upload`
- AI caption route: `https://ai-media-gateway-786.azurewebsites.net/api/ai/caption`
- AI content plan route: `https://ai-media-gateway-786.azurewebsites.net/api/ai/content-plan`

## Backend Web Apps

- Discovery: `https://ai-media-discovery-786.azurewebsites.net`
- Auth: `https://ai-media-auth-786.azurewebsites.net`
- User: `https://ai-media-user-786.azurewebsites.net`
- Post: `https://ai-media-post-786.azurewebsites.net`
- Interaction: `https://ai-media-interaction-786.azurewebsites.net`
- Media: `https://ai-media-media-786.azurewebsites.net`
- AI: `https://ai-media-ai-786.azurewebsites.net`
- Gateway: `https://ai-media-gateway-786.azurewebsites.net`

## Storage

- Storage account: `aiappmedia786`
- Static website endpoint: `https://aiappmedia786.z1.web.core.windows.net/`
- Blob endpoint: `https://aiappmedia786.blob.core.windows.net/`
- Public media container: `https://aiappmedia786.blob.core.windows.net/media`
- Deployment artifacts container: `deploy-artifacts`

Media files are stored in Blob Storage. Media metadata is stored in MySQL table `media_assets`.

## Database

- MySQL server: `aiappproject-server.mysql.database.azure.com`
- Databases:
  - `auth_db`
  - `user_db`
  - `post_db`
  - `interaction_db`
  - `media_db`

## Redis

- Redis resource: `ai-media-redis-786`
- Host: `ai-media-redis-786.redis.cache.windows.net`
- SSL port: `6380`

## Container Registry

- ACR name: `aimediaacr786`
- Login server: `aimediaacr786.azurecr.io`
- Docker image workflow: `.github/workflows/azure-docker-images.yml`

## CI/CD Workflows

- Java services and frontend: `.github/workflows/azure-live-java.yml`
- Docker images to ACR: `.github/workflows/azure-docker-images.yml`
- Container deployment template: `.github/workflows/azure-production-containers.yml`

Required GitHub secrets:

```text
AZURE_STORAGE_CONNECTION_STRING
ACR_USERNAME
ACR_PASSWORD
AZURE_WEBAPP_PUBLISH_PROFILE_DISCOVERY
AZURE_WEBAPP_PUBLISH_PROFILE_AUTH
AZURE_WEBAPP_PUBLISH_PROFILE_USER
AZURE_WEBAPP_PUBLISH_PROFILE_POST
AZURE_WEBAPP_PUBLISH_PROFILE_INTERACTION
AZURE_WEBAPP_PUBLISH_PROFILE_MEDIA
AZURE_WEBAPP_PUBLISH_PROFILE_AI
AZURE_WEBAPP_PUBLISH_PROFILE_GATEWAY
```
