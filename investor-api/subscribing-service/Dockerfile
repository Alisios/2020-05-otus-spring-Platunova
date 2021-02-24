FROM openjdk:13
EXPOSE 2090
EXPOSE 2091
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]