FROM openjdk:13
EXPOSE 7091
EXPOSE 7111
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]