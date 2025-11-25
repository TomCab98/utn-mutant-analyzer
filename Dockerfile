FROM amazoncorretto:21-alpine-jdk

COPY build/libs/mutantes-0.0.2-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java" , "-jar" , "/app.jar"]