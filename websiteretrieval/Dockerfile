FROM maven:3.9.5-eclipse-temurin-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM eclipse-temurin:17-jre
COPY --from=build /home/app/target/WebsiteRetrievalService.jar /WebsiteRetrievalService.jar
ENTRYPOINT ["java", "-jar", "/WebsiteRetrievalService.jar"]