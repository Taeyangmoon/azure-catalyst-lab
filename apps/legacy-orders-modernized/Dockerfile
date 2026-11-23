# syntax=docker/dockerfile:1

#############################
# Stage 1: Build the WAR
#############################
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /build

# Leverage Docker layer caching for dependencies
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

#############################
# Stage 2: Runtime (Tomcat 10.1 / Java 17)
#############################
FROM tomcat:10.1-jre17

# Remove default webapps (manager, docs, examples, host-manager, ROOT)
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy the application as the ROOT context
COPY --from=build /build/target/orders.war /usr/local/tomcat/webapps/ROOT.war

# Configuration is supplied at runtime via environment variables
# (DB_URL/DB_HOST/DB_PORT/DB_NAME, DB_USER, DB_PASSWORD, DB_DRIVER, DB_SSL_MODE, UPLOAD_DIR).
# Application logging is already configured to write to stdout/console (see log4j.properties).

# Run as a non-root user for defense in depth
RUN chgrp -R 0 /usr/local/tomcat && chmod -R g=u /usr/local/tomcat
USER 1000:0

EXPOSE 8080

CMD ["catalina.sh", "run"]
