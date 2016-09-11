# Geo-server

This is a test task.

## Description

Design and implement a geo-server. It must provide the following REST API.

* Method to check that a user is nearby to a label or not by the following rule:
if a distance between a current user coordinates and the label coordinates, calculated by [Haversine formula](https://en.wikipedia.org/wiki/Haversine_formula),
is less than an error, the user is nearby, otherwise is not.

* Method to get a count of labels in a given area (labels are distributed in bound areas).

* Method to change the coordinates (lat, lon) of a user label.

Generate data.

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
