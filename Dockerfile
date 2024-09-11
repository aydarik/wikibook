FROM openjdk:17-slim

COPY ./build/libs/wikibook-0.3-all.jar /wikibook.jar

ENTRYPOINT ["java", "-jar", "/wikibook.jar"]
EXPOSE 8080
