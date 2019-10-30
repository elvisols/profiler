FROM openjdk:8

VOLUME /tmp
ADD maven/profiler-0.0.1-SNAPSHOT.jar profiler-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /profiler-0.0.1-SNAPSHOT'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/profiler-0.0.1-SNAPSHOT.jar"]