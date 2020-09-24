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

FROM openjdk:11-slim
RUN mkdir /usr/local/app
COPY --from=build /home/gradle/build/distributions/newrelic-kafka-playground-consumer.tar /tmp/newrelic-kafka-playground-consumer.tar
RUN tar -xf /tmp/newrelic-kafka-playground-consumer.tar -C /usr/local/app
COPY ./config /usr/local/app/config
COPY --from=newrelic /tmp/newrelic /var/newrelic
WORKDIR /usr/local/app/newrelic-kafka-playground-consumer
ENV JAVA_OPTS="-javaagent:/var/newrelic/newrelic.jar"
CMD ["./bin/newrelic-kafka-playground-consumer"]