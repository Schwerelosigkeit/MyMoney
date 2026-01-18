FROM maven:3.8.6-amazoncorretto-17 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

FROM amazoncorretto:17
COPY --from=build /app/target/*.jar /app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]