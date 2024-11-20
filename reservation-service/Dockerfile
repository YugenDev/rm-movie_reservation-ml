FROM gradle:jdk21-alpine AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradle.properties ./

COPY src ./src
RUN gradle build --no-daemon

FROM eclipse-temurin:21.0.5_11-jre-ubi9-minimal

WORKDIR /app

COPY --from=builder /app/build/libs/reservation-0.1-all.jar ./

EXPOSE 8083

CMD ["java", "-jar", "reservation-0.1-all.jar"]