# Geo-server

This is a test task.

## Task description

Design and implement a geo-server. It must provide the following REST API.

* Method to check that a user is nearby to a label or not by the following rule:
if a distance between a current user coordinates and the label coordinates, calculated by [Haversine formula](https://en.wikipedia.org/wiki/Haversine_formula),
is less than an error, the user is nearby, otherwise is not.

* Method to get a count of labels in a given area (labels are distributed in bound areas).

* ~~Method to change the coordinates (lat, lon) of a user label.~~

## Generate data.

The following data should be generated in csv format.

Labels table:

user id      | lat    | lon
------------ | ------ | -------
 long        | double | double

"Grid" table:

error*       | lat    | lon
------------ | ------ | -------
 int         | double | double

error* - distance error

## Requirements

* java8
* maven3

## Build

Build with tests (you must fix the path to data files, before running the command):
```
mvn clean install
```

Build the jar with all dependencies:
```
mvn -DskipTests=true package spring-boot:repackage
```

Run the app:
```
java -jar app/target/app-0.0.1-SNAPSHOT.jar  --spring.config.location=path_to_your_config
```

## Implementation details

The data was generated in two steps.
At first, a user label data was generated randomly selecting a point in a radius of 10000 meters. All points (about 1 million) are located in the area of Moscow city.
Then, a "grid" table was generated based on user label data table by transformation of a lat,lon of the user label to a pair of values {geohash, a randomly select error from 0 to 500}.

A distribution of the generated data is close to [uniform](https://github.com/sherman/geo-server/blob/master/data/distribution_90.png) for a 90% of data.

Finally, it has two files user_data and distance_error in csv format.

I chose a [geohash](https://en.wikipedia.org/wiki/Geohash) for a representation of a cell index in the "grid" table because of two reasons:
* It's deadly simple (compared to r-tree, for example).
* It ideally fit into the task requirements (get a number of users in the cell).

### Complexity

I chose a scalable and concurrent version of a hash map data structure [ConcurrentHashMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.html).
A complexity of both methods is constant in the best case.

### Basic platform, frameworks.
* Spring boot (easy REST API, configuration, DI)
* Jetty server
* Guava
* Testng/mockito/springockito

### API (json format)

* Check a user coords: GET http://localhost:8080/api/geo/users/356865068291330092/near?lat=55.804&lon=37.637
```
{"result": true}
```

* Get segment size: GET http://localhost:8080/api/geo/hash/size/?lat=55.805&lon=37.638
```
{"result": 1691}
```

### Known issues.

* Spring boot maven plugin doesn't correctly build a multi-module maven project (classes from another modules are not reachable).
Workaround: switch to a single-module maven project.

* A method for changing the data is not implemented (lack of time).





