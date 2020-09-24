FROM alpine as newrelic
ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip /tmp/
WORKDIR /tmp
RUN unzip newrelic-java.zip

FROM gradle:jdk11 as dependencies
ENV GRADLE_USER_HOME /cache
#COPY --from=newrelic /tmp/newrelic/newrelic-api.jar newrelic-api.jar
COPY build.gradle settings.gradle ./
RUN gradle --no-daemon build -i --stacktrace

FROM gradle:jdk11 as build
COPY --from=dependencies /cache /home/gradle/.gradle
COPY --from=newrelic /tmp/newrelic/newrelic-api.jar newrelic-api.jar
COPY build.gradle settings.gradle ./
COPY src/ src/
RUN gradle --no-daemon build

# FROM openjdk:11-slim
# COPY --from=build /home/gradle/build/libs/newrelic-kafka-playground-producer.war /var/lib/jetty/webapps/ROOT.war
# COPY --from=newrelic /tmp/newrelic /run/jetty/newrelic
# COPY ./config /var/webapp/config
# CMD ["java", "-jar", "/usr/local/jetty/start.jar", "-javaagent:/run/jetty/newrelic/newrelic.jar"]