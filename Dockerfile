FROM openjdk:11-jre-slim
EXPOSE 8000
ADD target/scraping-hardware.jar scraping-hardware.jar
ENTRYPOINT ["java", "-jar", "scraping-hardware.jar"]