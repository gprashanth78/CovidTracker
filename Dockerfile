FROM openjdk:17

COPY target/*.jar /usr/app/

WORKDIR /usr/app

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "CovidTracker-App-0.0.1-SNAPSHOT.jar"]