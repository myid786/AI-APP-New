# Deployment Notes

## Local Development

Frontend:

```bash
cd FrontEnd
npm install
npm run dev
```

Backend:

```bash
cd Backend/single-backend
mvn spring-boot:run
```

## Docker Build

```bash
docker build -t ai-media-single-backend Backend/single-backend
```

## Required GitHub Secrets

```text
ACR_USERNAME
ACR_PASSWORD
AZURE_STORAGE_CONNECTION_STRING
```

Depending on the workflow used, publish profile or Azure login secrets may also be required.

## Azure App Settings

Configure these in the backend App Service:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
FRONTEND_ORIGIN
AZURE_STORAGE_CONNECTION_STRING
AZURE_STORAGE_CONTAINER=media
MEDIA_BASE_URL=https://aiappmedia786.blob.core.windows.net/media
AZURE_AI_LANGUAGE_ENDPOINT
AZURE_AI_LANGUAGE_KEY
```

## Production Notes

The live deployment uses a single backend container to keep the system cost-aware and easy to operate. The microservice modules remain in the repository as an architecture reference and can be split again for a larger deployment.
