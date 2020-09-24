FROM alpine as newrelic
ADD https://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip /tmp/
WORKDIR /tmp
RUN unzip newrelic-java.zip

FROM gradle:jdk11 as consumerdependencies
ENV GRADLE_USER_HOME /cache
#COPY --from=newrelic /tmp/newrelic/newrelic-api.jar newrelic-api.jar
COPY consumer/build.gradle consumer/settings.gradle ./
RUN gradle --no-daemon build -i --stacktrace

FROM gradle:jdk11 as build
COPY --from=consumerdependencies /cache /home/gradle/.gradle
RUN mkdir -p /usr/local/app/common
COPY common/src /usr/local/app/common/src
RUN mkdir -p /usr/local/app/consumer
COPY consumer/src /usr/local/app/consumer/src
WORKDIR /usr/local/app/consumer
COPY --from=newrelic /tmp/newrelic/newrelic-api.jar newrelic-api.jar
COPY consumer/build.gradle consumer/settings.gradle ./
RUN gradle --no-daemon build

FROM openjdk:11-slim
RUN mkdir /usr/local/app
COPY --from=build /usr/local/app/consumer/build/distributions/newrelic-kafka-playground-consumer.tar /tmp/newrelic-kafka-playground-consumer.tar
RUN tar -xf /tmp/newrelic-kafka-playground-consumer.tar -C /usr/local/app
COPY consumer/config /usr/local/app/config
COPY --from=newrelic /tmp/newrelic /var/newrelic
WORKDIR /usr/local/app/newrelic-kafka-playground-consumer
ENV JAVA_OPTS="-javaagent:/var/newrelic/newrelic.jar"
CMD ["./bin/newrelic-kafka-playground-consumer"]