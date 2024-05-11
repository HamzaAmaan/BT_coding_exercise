FROM ubuntu:latest

RUN apt-get update && apt-get install -y bash maven

# Copy the application source code
COPY . /app
WORKDIR /app

# Build the application
RUN mvn clean install

# Set the entrypoint to the Java command
ENTRYPOINT ["java", "-jar", "target/BT_coding_exercise-1.0-SNAPSHOT-jar-with-dependencies.jar"]