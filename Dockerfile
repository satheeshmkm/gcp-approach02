FROM openjdk:11
# building from base image openjdk:11
ADD target/gcp-approach02.jar gcp02.jar
# adding jar in the path target/docker-spring-boot.jar to docker image with name helloapp.jar
ENTRYPOINT ["java","-jar","/gcp02.jar","--server.port=8090"]
# Instruction to docker on how to run the docker file