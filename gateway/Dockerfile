FROM amazoncorretto:11
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8081
COPY target/*.jar shareit-gateway-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/shareit-gateway-0.0.1-SNAPSHOT.jar"]