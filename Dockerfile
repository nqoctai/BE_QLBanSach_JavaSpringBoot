# Stage 1: Build the application
FROM maven:3.9.8-amazoncorretto-17-debian AS build
COPY --chown=maven:maven . /ngoctai/bookstore
WORKDIR /ngoctai/bookstore

# Skip tests during build
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-slim
EXPOSE 8080
COPY --from=build /ngoctai/bookstore/target/*.jar /ngoctai/spring-boot-book-store.jar
ENTRYPOINT ["java", "-jar", "/ngoctai/spring-boot-book-store.jar"]