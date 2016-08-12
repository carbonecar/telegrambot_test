FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD myfirstbot/target/bottest-0.0.1-SNAPSHOT.jar bottest-0.0.1.jar




RUN sh -c 'touch /bottest-0.0.1.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/bottest-0.0.1.jar"]
