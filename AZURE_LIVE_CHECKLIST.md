# Azure Live Checklist

## Current Live Resources

- Resource group: `rg-ai-media-platform`
- Frontend: Azure Storage Static Website
- Backend: Azure App Service Linux container
- Backend Web App: `ai-media-gateway-786`
- Container registry: `aimediaacr786`
- Storage account: `aiappmedia786`
- Blob container: `media`
- MySQL Flexible Server: `aiappproject-server`
- Database: `ai_media_db`
- Azure AI Language: `ai-media-language-786`
- Redis resource: `ai-media-redis-786`

## Required App Settings

Backend settings should be configured in Azure App Service:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
FRONTEND_ORIGIN
AZURE_STORAGE_CONNECTION_STRING
AZURE_STORAGE_CONTAINER
MEDIA_BASE_URL
AZURE_AI_LANGUAGE_ENDPOINT
AZURE_AI_LANGUAGE_KEY
```

Frontend build setting:

```text
VITE_API_BASE_URL=https://ai-media-gateway-786.azurewebsites.net
```

## Deployment Checks

- Backend health endpoint returns a successful response.
- Frontend loads from the Azure Storage static website URL.
- GitHub Actions completes the Docker build and deployment workflow.
- Azure Container Registry contains the latest backend image.
- Media upload stores files in the Blob container.
- MySQL stores metadata records.
- AI Matrix endpoint returns sentiment data from Azure AI Language.

## Future Scale Path

The current deployment is intentionally cost-aware. For larger traffic, the same backend image can be deployed to Azure Container Apps with autoscaling. Azure Front Door, CDN, Redis, Service Bus, Key Vault, and Application Insights can be added as production-grade services around the current design.
