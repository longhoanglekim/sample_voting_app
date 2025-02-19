# Sử dụng image Maven để build ứng dụng Spring Boot
FROM maven:latest AS build

# Set-up work directory
WORKDIR /app

# Sao chép file pom.xml để tải các dependency
COPY pom.xml /app/

# Tải các dependency mà không cần build
RUN mvn dependency:go-offline

# Sao chép toàn bộ mã nguồn
COPY src /app/src

# Build file JAR
RUN mvn clean package -DskipTests

# Sử dụng image OpenJDK để chạy ứng dụng
FROM openjdk:17-alpine

# Set-up work directory
WORKDIR /app

# Set-up the port
EXPOSE 8080

# Copy file JAR đã build từ build container vào container này
COPY --from=build /app/target/*.jar app.jar

# Run app
CMD ["java", "-jar", "app.jar"]


