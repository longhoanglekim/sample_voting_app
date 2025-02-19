# Sử dụng image với JDK và Maven để build Spring Boot application
FROM maven:latest AS build

# Set up work directory
WORKDIR /app

# Copy pom.xml và các file liên quan để tải dependency
COPY pom.xml /app/
RUN mvn dependency:go-offline

# Copy toàn bộ mã nguồn vào container
COPY src /app/src

# Build file JAR
RUN mvn clean package -DskipTests

# Sử dụng image openjdk để chạy ứng dụng (bước thứ hai trong multi-stage build)
FROM openjdk:17-alpine

# Set-up the work directory
WORKDIR /app

# Set-up the port
EXPOSE 8081

# Copy file JAR đã build từ container build sang container chạy ứng dụng
COPY --from=build /app/target/*.jar app.jar

# Run ứng dụng
CMD ["java", "-jar", "app.jar"]


