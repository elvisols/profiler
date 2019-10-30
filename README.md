# Getting Started

This is a simple (Java) Spring boot real-time user profiling application. It gives three specific insight:
- For a particular user, what the top-N amenities selected.
- For a particular user, what the top-K hotels clicked on the most.
- For a particular user, what the top-M hotel regions clicked on the most.

In order to have a scalable and highly available system, I have utilized the following tools -

- A messaging/Queuing system - Kafka: This caters for high volumes of data.
- A NOSQL sink - Elasticsearch: This will allow for speedy retrieval of our aggregated data, using Lucene Library. 
- Docker: For containerization.

##### Application Requirements:
- Internet Connection
- Java 8
- Maven 3+
- Docker p
- Docker Compose `docker-compose`

##### Application Run:
Step One:
> Building:
```
~$ mvn clean package docker:build
```
Step Two:
> Running:
```
~$ docker-compose up [-d]
```
Ensure to run the above command from the directory with the docker-compose.yml file. 

Kindly wait between 2 - 3 mins to have your containers up and running before hitting the system. 

Once the services are up, the application documentation can be accessed via

`./target/generated-docs/index.html`

It contains details about each insight endpoints the team are interested in.

> Data Inputs

After the application is started, an `inputsource` folder will be created. Drop all(selections.csv|clicks.csv) data here.

Please note that processing of these files start immediately after the files are dropped. Since we are using a single instance, 
this will depend on the capacity of the test machine.

Files will be automatically deleted once processing is complete.



### Assumptions
- Caller has access to the user's unique id.
- docker-compose is installed
- you have internet access.

### Reference Documentation
For further reference, how the webservice is consumed please visit the documentation page

* [Official User Profiler documentation](./target/guides/index.html)

###   Regards!
