FROM alpine as newrelic
ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip /tmp/
WORKDIR /tmp
RUN unzip newrelic-java.zip

FROM gradle:jdk11 as producerdependencies
ENV GRADLE_USER_HOME /cache
COPY --from=newrelic /tmp/newrelic/newrelic-api.jar newrelic-api.jar
COPY producer/build.gradle producer/settings.gradle ./
RUN gradle --no-daemon build -i --stacktrace

FROM gradle:jdk11 as build
COPY --from=producerdependencies /cache /home/gradle/.gradle
RUN mkdir -p /usr/local/app/common
COPY common/src /usr/local/app/common/src
RUN mkdir -p /usr/local/app/producer
COPY producer/src /usr/local/app/producer/src
WORKDIR /usr/local/app/producer
COPY --from=newrelic /tmp/newrelic/newrelic-api.jar newrelic-api.jar
COPY producer/build.gradle producer/settings.gradle ./
RUN gradle --no-daemon war

FROM jetty:9.4.31-jre11-slim
COPY --from=build /usr/local/app/producer/build/libs/newrelic-kafka-playground-producer.war /var/lib/jetty/webapps/ROOT.war
COPY --from=newrelic /tmp/newrelic /run/jetty/newrelic
COPY producer/config /var/webapp/config
CMD ["java", "-jar", "/usr/local/jetty/start.jar", "-javaagent:/run/jetty/newrelic/newrelic.jar"]