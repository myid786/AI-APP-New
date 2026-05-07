FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY Backend/common-lib ./Backend/common-lib
RUN mvn -f Backend/common-lib/pom.xml clean install -DskipTests

COPY Backend/single-backend ./Backend/single-backend
RUN mvn -f Backend/single-backend/pom.xml clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
ENV SERVER_PORT=8080
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=65 -XX:InitialRAMPercentage=20"
COPY --from=build /workspace/Backend/single-backend/target/*.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
