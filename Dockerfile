FROM openjdk:19
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN bash -c "touch /app.jar"
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
