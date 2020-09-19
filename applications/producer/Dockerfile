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