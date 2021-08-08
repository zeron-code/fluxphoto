# Photo Gallery Sample Application

### Build
To build the Photo Gallery application follow the below steps

Make sure you have the following software installed:

* Apache Maven
* JDK 11
* MongoDB

Clone the source codes into your local system.

```
git clone https://github.com/zeron-code/
```

Move to the `bin` folder of your MongoDB installation and run the following command to start it up.
Make sure you do have the folder `..\data\db`, and if not then create it before running the below.

```
.\mongod.exe --dbpath=..\data\db
```

MongoDB will start in seconds. Make sure you see `Waiting for connections` on the console

### Run

* Open a terminal window and move the the folder where you cloned the application
* Run the application using the below maven command

```
mvn spring-boot:run
```

The application will launch and it can be accessed at `http://localhost:8080`
Note that this application has no GUI and only provides a REST API.
