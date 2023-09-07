FROM openjdk
ADD ./target/ms-one-0.0.1-SNAPSHOT.jar msone.jar
ENTRYPOINT ["java", "-jar", "msone.jar"]