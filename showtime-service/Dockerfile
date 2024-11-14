FROM gradle:jdk21-alpine AS builder

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle build --no-daemon

FROM eclipse-temurin:21.0.5_11-jre-ubi9-minimal

WORKDIR /app

COPY --from=builder /app/build/libs/showtime-service.jar ./

EXPOSE 8082

CMD ["java", "-jar", "showtime-service.jar"]
