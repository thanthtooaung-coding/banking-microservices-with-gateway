# FROM maven:3.8.7-eclipse-temurin-17 as build
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# COPY src/main/proto ./src/main/proto
# RUN mvn -DskipTests package

# FROM eclipse-temurin:17-jre
# WORKDIR /app
# COPY --from=build /app/target/account-service-0.0.1-SNAPSHOT.jar app.jar
# EXPOSE 8082 9091
# ENTRYPOINT ["java","-jar","/app/app.jar"]


FROM maven:3.8.7-eclipse-temurin-17 as build
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
COPY src/main/proto ./src/main/proto
# RUN mvn -DskipTests package

FROM eclipse-temurin:17-jre
# WORKDIR /app
COPY ./target/account-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082 9091
ENTRYPOINT ["java","-jar","app.jar"]
