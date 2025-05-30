FROM gradle:8.14.1-jdk21 AS builder

WORKDIR /home/gradle/project

COPY --chown=gradle:gradle gradle/ gradle/
COPY --chown=gradle:gradle api/build.gradle.kts settings.gradle.kts gradlew ./

COPY --chown=gradle:gradle api/src/ src/

RUN chmod +x gradlew

RUN ./gradlew bootJar --no-daemon



FROM bellsoft/liberica-openjdk-alpine:23

LABEL org.opencontainers.image.title="BudgetMate API"
LABEL org.opencontainers.image.description="Back-End for BudgetMate"
LABEL org.opencontainers.image.version="1.0.0"
LABEL org.opencontainers.image.authors='Robert Sargsyan <robertsargsyan6@gmail.com>'
EXPOSE 8080

WORKDIR /api

COPY --from=builder /home/gradle/project/build/libs/*.jar ./api.jar

ENTRYPOINT ["java", "-jar", "api.jar"]