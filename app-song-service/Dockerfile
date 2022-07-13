FROM openjdk:17-jdk-alpine

COPY build/libs/app-song-service.jar app.jar

ENTRYPOINT ["java","--enable-preview","-jar","/app.jar"]
