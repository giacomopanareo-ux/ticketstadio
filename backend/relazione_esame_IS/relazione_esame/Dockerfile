# ────────────────────────────────────────────────────────────
# Dockerfile per TicketStadio backend (Spring Boot 3.2)
#
# Multi-stage build:
#   stage 1: compila il JAR con Maven (immagine pesante con JDK + Maven)
#   stage 2: copia solo il JAR in un'immagine slim con solo JRE
#
# Build:    docker build -t ticketstadio-backend .
# Run:      docker run -p 8080:8080 ticketstadio-backend
# ────────────────────────────────────────────────────────────

# ─── Stage 1: build ──────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copia prima solo pom.xml per sfruttare la cache delle dipendenze:
# se solo il codice cambia, Maven non riscarica le dipendenze
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia il sorgente e compila (skip test per build veloce; per CI rimuovere -DskipTests)
COPY src ./src
RUN mvn clean package -DskipTests -B

# ─── Stage 2: runtime ────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crea un utente non-root per ragioni di sicurezza
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia il JAR dallo stage 1
COPY --from=build /app/target/ticketstadio-*.jar app.jar

# Espone la porta 8080 (default Spring Boot)
EXPOSE 8080

# Variabili d'ambiente (override al runtime con -e)
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Healthcheck (Spring Boot Actuator non incluso, usa la home page)
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/v3/api-docs || exit 1

# Avvio
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
