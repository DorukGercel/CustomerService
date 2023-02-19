FROM maven:3.8.3-openjdk-17
WORKDIR /home/customer-service-server
COPY . /home/customer-service-server
RUN mvn install -Dmaven.test.skip=true
ENTRYPOINT mvn spring-boot:run