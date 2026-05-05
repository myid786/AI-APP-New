# AI Media Platform Backend with Eureka

Java Spring Boot microservices backend with Eureka Service Discovery.

## Services
- Discovery / Eureka Server: 8761
- API Gateway: 8080
- Auth Service: 8081
- User Service: 8082
- Post Service: 8083
- Interaction Service: 8084
- Media Service: 8085
- AI Service: 8086
- MySQL: 3306
- Redis: 6379

## Run infrastructure
```bash
docker compose up mysql redis -d
```

## Build common lib
```bash
mvn -f common-lib/pom.xml clean install
```

## Start order
1. discovery-service
2. auth-service
3. user-service
4. post-service
5. interaction-service
6. media-service
7. ai-service
8. api-gateway
9. frontend

## Build services
```bash
mvn -f discovery-service/pom.xml clean package -DskipTests
mvn -f auth-service/pom.xml clean package -DskipTests
mvn -f user-service/pom.xml clean package -DskipTests
mvn -f post-service/pom.xml clean package -DskipTests
mvn -f interaction-service/pom.xml clean package -DskipTests
mvn -f media-service/pom.xml clean package -DskipTests
mvn -f ai-service/pom.xml clean package -DskipTests
mvn -f api-gateway/pom.xml clean package -DskipTests
```

## URLs
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080
