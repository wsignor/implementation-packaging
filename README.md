# implementation-packaging

## Getting started
### How to run

 - Check out this branch 
 
	`git checkout https://github.com/wsignor/implementation-packaging.git`

 - Run the following command to fetch the dependencies and run the tests.    

	`mvn clean install`

 - Run the following command to run the app

	`java -jar target/implementation-1.0-SNAPSHOT.jar`



### Code
The project was structured and designed using the SOLID principles, aiming to make it understandable, flexible and maintainable. The concerns were distributed in methods.



### Tests
The tests were created using Jupiter/JUnit. The unit tests were created by method, isolating the methods and testing each one in particular.
