# BT_coding_exercise
This application processes a log file containing user session start and end times, and outputs the number of sessions and minimal total duration for each user.
## Log file format
The application expects the log file to be in the following format:
>`<timestamp> <username> <action>`

where:
- \<timestamp> is a valid time in the format HH:mm:ss
- \<username> is an alphanumeric string
- \<action> is either START or END

If the log file does not comply with this format, the application will skip the invalid lines.

## Running locally without docker
### Prerequisites
- Java 17
- Maven
### Building the Application
1. Clone the repository to your local machine.
2. Navigate to the project directory in your terminal/command prompt.
3. Run the following command to build the application: 
>`mvn clean install`

This will create an executable JAR file in the target directory.
### Running the Application
1. To run the application, use the following command: 
> `java -jar target/BT_coding_exercise-1.0-SNAPSHOT-jar-with-dependencies.jar <path_to_log_file>`

Replace <path_to_log_file> with the absolute path to the log file you want to process.

## Running locally with docker
### Prerequisites
- Docker
### Build and Run
1. Clone the repository to your local machine.
2. Copy the log file into the following folder of the:
> `src/main/resources/`

3. Build the Docker image using the provided Dockerfile:
> `docker build -t app . `

4. Run the Docker container, passing the log file as an argument:
>`docker run app "src/main/resources/<log_file_name>"`
> 
Replace <log_file_name> with the log file name you have copied in step 2.

## Logging validation errors
The application does not log any errors however for debugging purposes any invalid lines are logged at a debug level.
The application uses Log4j2 and the following can be added to log4j2.properties to enable debug logs:

    # Console appender
    appender.stdout.type = Console
    appender.stdout.name = STDOUT
    appender.stdout.layout.type = PatternLayout
    appender.stdout.layout.pattern = %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n
    
    # Logger-specific configurations
    logger.myapp.name = org.example
    logger.myapp.level = DEBUG
    logger.myapp.additivity = false
    logger.myapp.appenderRefs = stdout
    logger.myapp.appenderRef.stdout.ref = STDOUT

    rootLogger=debug, STDOUT

If log4j2.properties does not exist create it in `src/main/resources/`. This will require a rebuild.