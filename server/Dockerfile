FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]