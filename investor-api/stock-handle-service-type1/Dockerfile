FROM openjdk:13
EXPOSE 4011
EXPOSE 4029
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]