FROM alpine as newrelic
ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip /tmp/
WORKDIR /tmp
RUN unzip newrelic-java.zip

FROM gradle:jdk11 as dependencies
ENV GRADLE_USER_HOME /cache
COPY build.gradle settings.gradle ./
RUN gradle --no-daemon dependencies --stacktrace

FROM gradle:jdk11 as build
COPY --from=dependencies /cache /home/gradle/.gradle
COPY build.gradle settings.gradle ./
COPY src/ src/
RUN gradle --no-daemon war

FROM jetty:9.4.31-jre11-slim
COPY --from=build /home/gradle/build/libs/newrelic-kafka-playground-producer.war /var/lib/jetty/webapps/ROOT.war
COPY --from=newrelic /tmp/newrelic /run/jetty/newrelic
ENV NEW_RELIC_APP_NAME="newrelic-kafka-playground-producer"
ENV NEW_RELIC_LOG_FILE_NAME=STDOUT
CMD ["java", "-jar", "/usr/local/jetty/start.jar", "-javaagent:/run/jetty/newrelic/newrelic.jar"]