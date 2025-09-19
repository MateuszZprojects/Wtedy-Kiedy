FROM openjdk:12-alpine

WORKDIR /serwer

COPY target/wtedykiedyserwer.jar /serwer/app.jar

EXPOSE 8081

CMD ["java", "-jar", "app.jar"]