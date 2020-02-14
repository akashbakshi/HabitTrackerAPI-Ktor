#Build out the JAR first
FROM gradle:4.10-jdk8-alpine AS build

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean jar --no-daemon

FROM openjdk:8-jre-alpine

RUN mkdir /app

WORKDIR /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app

EXPOSE 8080

CMD ["java", "-server", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-XX:InitialRAMFraction=2", "-XX:MinRAMFraction=2", "-XX:MaxRAMFraction=2", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=100", "-XX:+UseStringDeduplication", "-jar", "habittracker-0.1.jar"]