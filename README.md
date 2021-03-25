[![Java CI with Maven](https://github.com/worldjoe/boiler-app/actions/workflows/maven.yml/badge.svg)](https://github.com/worldjoe/boiler-app/actions/workflows/maven.yml)

This is a Java Spring Boot starter that implements a REST api for a simple CRUD data store. It uses JPA for datastorage, currently hooked up to an in memeory H2 database.

For the front end, you can use this sample [React Application](https://github.com/worldjoe/boiler)

This repo has been setup with [Github Actions](https://github.com/worldjoe/boiler-app/actions) to automatically build and test pr's and pushes to master.

This was generated using the Spring Initializer:
https://start.spring.io/#!type=maven-project&language=java&platformVersion=2.4.3.RELEASE&packaging=jar&jvmVersion=11&groupId=net.singleclick&artifactId=boiler-app&name=boiler-app&description=Crud%20Demo%20project%20for%20Spring%20Boot&packageName=net.singleclick.boiler-app&dependencies=web,lombok,devtools,data-jpa,h2

Generate keystore for ssl:
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore tomcat.p12 -validity 3650 -storepass password -ext "SAN:c=DNS:localhost,IP:127.0.0.1"

Be sure to tell your browser to trust the ssl cert by visiting https://localhost:8443/v1/documents

H2 database:
Connect to debug/view:
https://localhost:8443/h2-ui/
JDBC URL: jdbc:h2:mem:testdb

There is a [Postman](https://www.postman.com/) collection in /resources/ This can be used to test the CRUD REST endpoint without the need for a frontend application.

Jenkins setup
```docker pull jenkins/jenkins:lts
docker run -p 8081:8080  -p 50000:50000  --name my-jenkins -v jenkins_data:/var/jenkins_home jenkins/jenkins:lts
```
Then Load http://localhost:8081 and paste in the password from the output of the last command.
Be sure to create a password for the admin user.
Open a terminal connected to your docker, we need to upgrade the JDK
used to compile the service.


Install the suggested plugins, then go to "Manage Jenkins", then "Manage
plugins", then click on Available and find "AdoptOpenJDK installer "
andd install it and select the options to install without restart.
Then goto http://localhost:8081/configureTools/ and Add JDK and use
the adopt openjdk selecting version 13.0.2+8 


Then create a Pipeline job and under Advanced Project Options select
"Pipeline Script from SCM" in the dropdown, select "Git", and put this github url  https://github.com/worldjoe/boiler-app.git

Docker Setup
```docker build -t springio/gs-spring-boot-docker .```
Then run it:
```docker run -p 8443:8443 springio/gs-spring-boot-docker```
Debugging the docker:
```docker run -e "JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" -p 8443:8443 -p 5005:5005 -t springio/gs-spring-boot-docker```