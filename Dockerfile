FROM maven:3.9.6-amazoncorretto-21 AS build

COPY . /app
WORKDIR /app

RUN mvn clean package -DskipTests

FROM amazoncorretto:21

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]