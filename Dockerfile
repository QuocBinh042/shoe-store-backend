# Stage 1: Build stage
FROM maven:3.8.4-openjdk-17 AS build

# Đặt thư mục làm việc là /app
WORKDIR /app

# Copy toàn bộ project vào thư mục làm việc trong container
COPY . .

# Biên dịch và đóng gói ứng dụng (bỏ qua test để tăng tốc quá trình build)
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy file .jar từ stage build trước đó sang
COPY --from=build /app/target/Server-0.0.1-SNAPSHOT.jar app.jar

# Cấu hình port cho ứng dụng
EXPOSE 8080

# Lệnh chạy ứng dụng Spring Boot
CMD ["java", "-jar", "app.jar"]
