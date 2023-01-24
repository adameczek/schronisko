FROM amazoncorretto:17
# Set the time zone for the container
ENV TZ=Europe/Warsaw
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# install some packages
RUN yum update
# install some packages
RUN yum install -y curl

RUN yum install shadow-utils.x86_64 -y
# run under a user. This makes the whole thing more secure
RUN groupadd normalgroup
RUN useradd -G normalgroup normaluser
USER normaluser:normalgroup
# run app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]