FROM openjdk:11.0.18
EXPOSE 8080
ARG APP_NAME="springBoot-apiGateWay
ARG APP_VERSION="0.0.1"
ARG JAR_FILE="/build/libs/${APP_NAME}-${APP_VERSION}.jar"
ARG JAR_FILE="/build/libs/${APP_NAME}-${APP_VERSION}.jar"
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar", "app.jar"]