FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY common-lib ./common-lib
COPY post-service ./post-service

RUN mvn -q -f common-lib/pom.xml clean install -DskipTests
RUN mvn -q -f post-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /workspace/post-service/target/*.jar app.jar
USER spring
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app/app.jar"]
