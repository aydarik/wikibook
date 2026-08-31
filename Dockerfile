FROM eclipse-temurin:25-jre-alpine

COPY ./build/libs/wikibook-0.4-all.jar /wikibook.jar

ENTRYPOINT ["java", "-jar", "/wikibook.jar"]
EXPOSE 8080
