# AI Media Platform

AI Media Platform is a cloud-deployed media sharing application built with a React frontend and a Java Spring Boot backend. It supports creator uploads, image/video storage, user interaction, content analytics, and an AI Matrix feature for content insight.

## Live Links

- Frontend: https://aiappmedia786.z1.web.core.windows.net/
- Backend API: https://ai-media-gateway-786.azurewebsites.net
- Health check: https://ai-media-gateway-786.azurewebsites.net/api/health

## Key Features

- Creator and consumer user flows
- Image and video upload support
- Azure Blob Storage for media files
- MySQL metadata storage for users, posts, media records, comments, and ratings
- AI Matrix endpoint for sentiment and content recommendation signals
- Dockerized backend deployment
- GitHub Actions CI/CD pipeline
- Azure Container Registry image publishing

## Cloud Architecture

The current live deployment uses a cost-aware single backend container pattern:

- React frontend hosted as an Azure Storage Static Website
- Spring Boot backend running as a Linux container on Azure App Service
- Azure Blob Storage container named `media` for uploaded images and videos
- Azure Database for MySQL Flexible Server for relational metadata
- Azure AI Language for sentiment analysis used by the AI Matrix feature
- Azure Container Registry for backend Docker images
- GitHub Actions for automated build and deployment

For future high-traffic growth, the containerized backend can move to Azure Container Apps with autoscaling. Azure Front Door, CDN, Redis, Service Bus, and Application Insights can be added around the current design without a full rewrite.

## Tech Stack

- Frontend: React, Vite, JavaScript, CSS
- Backend: Java 17, Spring Boot, Spring Security, JPA/Hibernate
- Database: Azure Database for MySQL
- Storage: Azure Blob Storage
- AI: Azure AI Language
- DevOps: Docker, Azure Container Registry, GitHub Actions
- Hosting: Azure Static Website, Azure App Service

## Repository Structure

```text
FrontEnd/                 React frontend
Backend/single-backend/   Production backend used by the live Azure deployment
Backend/*-service/        Microservice modules kept for local and architecture reference
.github/workflows/        CI/CD workflows
```

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

The frontend reads the API URL from `VITE_API_BASE_URL`. Backend secrets such as database, storage, JWT, and Azure AI settings should be supplied through environment variables or Azure App Settings.

## Deployment

The live backend is built as a Docker image and deployed through GitHub Actions. Media files are stored in Blob Storage while MySQL stores only structured metadata and media URLs. This keeps the database smaller and makes media delivery easier to scale.
