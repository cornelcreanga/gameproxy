FROM openjdk:11.0.3-jdk-stretch
VOLUME /tmp
ADD target/realtime-dispatcher.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]