# Start with a base image containing Java runtime
FROM java:8

# Add Author info
LABEL maintainer="ssd018@gmail.com"

# Add a volume to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/*.jar
ARG KEY_FILE=./keystore.p12

# Add the application's jar to the container
ADD ${JAR_FILE} to-do-springboot.jar
ADD ${KEY_FILE} keystore.p12


# Run the jar file
ENTRYPOINT ["java","-jar","/to-do-springboot.jar"]