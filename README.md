# spring-boot-jwt
This is a very simple JWT based application with Java, Spring boot and MySql.
FrameWorks used: Hibernate, JPA, Spring Framework
Tools required to run it: Postman, java installed on system, an IDE and MySql

How to run and use it:
1) Run the application,
2) There are API's in this application, namely : register, authenticate and greeting.
3) Once the application up and running, open Postman and add url as : localhost:8080/register , localhost:8080/authenticate and localhost:8080/greeting. For the given APIs
4) You will notice, register and authenticate are public APIs where as greeting includes JWT authorization
5) For register API, in the body of the request, add:
    {
    "username" : "your_own_username",
    "password" : "your_own_password"
    }
6) After the API is successfully triggered, you will notice that a new DB is created with the name notesdb and which has a table called user which now contains the username and password that we had just passed
7) When we trigger authenticate API, our application will look in to the DB to see if the passed username and password exists in the DB or not
8) If yes, then a JWT token is retrieved, which can be passed in the header section of greeting API with key as Authorization and value as Bearer token
9) If the token is correct, then it will return or print a message on the postman saying welcome!    


NOTE: JAR is also present in the given project and it is present at root level in a folder called build-jar

Please feel free, to refer it and use this project as you wish, Just don't forget to either star it or fork it :D
