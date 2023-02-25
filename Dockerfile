#
# BUILD STAGE
#
FROM maven:3.9.0-amazoncorretto-17 AS build
# Set the time zone for the container
ENV TZ=Europe/Warsaw
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY src /home/app/src
COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml clean package
RUN mvn -T 1C -f /home/app/pom.xml --batch-mode --update-snapshots verify -Dmaven.test.skip -DskipTests
#
# run app
#
FROM amazoncorretto:17
COPY --from=build /home/app/target/schronisko-0.0.1-SNAPSHOT.jar /usr/local/lib/schronisko.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/schronisko.jar"]
