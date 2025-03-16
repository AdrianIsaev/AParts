FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN ./mvnw package -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/AParts-0.0.1-SNAPSHOT.jar"]