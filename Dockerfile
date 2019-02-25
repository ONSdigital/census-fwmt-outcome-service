FROM openjdk:11-jdk-slim
ARG jar
RUN groupadd -g 986 censusfeedbacksvc && \
     useradd -r -u 986 -g censusfeedbacksvc censusfeedbacksvc
USER censusfeedbacksvc
COPY $jar /opt/censusfeedbacksvc.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "java",  "-jar", "/opt/censusfeedbacksvc.jar" ]
