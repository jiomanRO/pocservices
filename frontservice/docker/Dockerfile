FROM openjdk
MAINTAINER jioman@gmail.com
COPY frontservice-0.0.1-SNAPSHOT.jar .
COPY application.properties .
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "frontservice-0.0.1-SNAPSHOT.jar"]
EXPOSE 8881
