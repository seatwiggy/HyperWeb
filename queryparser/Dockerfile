FROM maven:3.9.4-eclipse-temurin-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM eclipse-temurin:17-jre
COPY --from=build /home/app/target/QueryParserService.jar /QueryParserService.jar
ENTRYPOINT ["java", "-jar", "/QueryParserService.jar"]