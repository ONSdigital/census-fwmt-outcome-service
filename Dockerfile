FROM openjdk:11-jdk-slim
ARG jar
COPY $jar /opt/censusfeedbacksvc.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "java",  "-jar", "/opt/censusfeedbacksvc.jar" ]
