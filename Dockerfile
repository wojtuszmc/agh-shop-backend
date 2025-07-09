# Używamy OpenJDK 17 jako base image
FROM openjdk:17-jdk-slim

# Ustawiamy katalog roboczy
WORKDIR /app

# Kopiujemy pom.xml i src do kontenera
COPY pom.xml .
COPY src ./src

# Instalujemy Maven
RUN apt-get update && apt-get install -y maven

# Budujemy aplikację
RUN mvn clean package -DskipTests

# Otwieramy port 8080
EXPOSE 8080

# Uruchamiamy aplikację
CMD ["java", "-jar", "target/agh-shop-backend-1.0-SNAPSHOT.jar"]