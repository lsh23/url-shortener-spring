FROM openjdk:17
ENV	PROFILE local
COPY build/libs/*.jar url-shortener.jar
ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILE}","-jar","/url-shortener.jar"]