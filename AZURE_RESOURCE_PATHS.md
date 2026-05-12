# Azure Resource Paths

## Live Application

- Frontend: `https://aiappmedia786.z1.web.core.windows.net/`
- Backend API: `https://ai-media-gateway-786.azurewebsites.net`
- Health check: `https://ai-media-gateway-786.azurewebsites.net/api/health`

The frontend is built with:

```text
VITE_API_BASE_URL=https://ai-media-gateway-786.azurewebsites.net
```

## Backend Routes

- Auth: `/api/auth`
- Users: `/api/users`
- Posts: `/api/posts`
- Interactions: `/api/interactions`
- Media upload: `/api/media/upload`
- AI Matrix: `/api/ai/suggestion-matrix`
- AI caption: `/api/ai/caption`
- AI content plan: `/api/ai/content-plan`

## Azure Services

- Resource group: `rg-ai-media-platform`
- Frontend storage account: `aiappmedia786`
- Static website endpoint: `https://aiappmedia786.z1.web.core.windows.net/`
- Blob endpoint: `https://aiappmedia786.blob.core.windows.net/`
- Media container: `media`
- Backend Web App: `ai-media-gateway-786`
- Azure Container Registry: `aimediaacr786`
- Backend image: `aimediaacr786.azurecr.io/aimedia-single-backend`
- MySQL Flexible Server: `aiappproject-server`
- Database: `ai_media_db`
- Azure AI Language: `ai-media-language-786`
- Redis resource reserved for caching: `ai-media-redis-786`

## Storage Design

Uploaded media files are stored in Azure Blob Storage. MySQL stores structured metadata such as user records, posts, media URLs, comments, ratings, and timestamps.

## CI/CD

- Backend Docker image workflow: `.github/workflows/azure-docker-images.yml`
- Live backend/frontend workflow: `.github/workflows/azure-live-java.yml`

Secrets and connection strings should be configured through GitHub Secrets and Azure App Settings. They should not be committed to the repository.
