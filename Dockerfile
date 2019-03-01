FROM openjdk:11-jdk-slim
ARG jar
RUN groupadd -g 986 censusoutcomesvc && \
     useradd -r -u 986 -g censusoutcomesvc censusoutcomesvc
USER censusoutcomesvc
COPY $jar /opt/censusoutcomesvc.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "java",  "-jar", "/opt/censusoutcomesvc.jar" ]
