# spark-api

Java RESTful API developed using [spark](https://github.com/perwendel/spark) - a simple expressive microframework for Java. The API allows for adding and fetching films and film reviews from a database. 

### API Description

The API includes following endpoints:
- `/films` getting all films or adding a new film
- `/films/{filmId}` getting a film with specific `filmId`
- `/films/{filmId}/reviews` getting all reviews for given a film or adding a review for a film
- `/reviews` getting all reviews 

The project uses `sql2o` to execute sql statements against JDBC compliant database. For the purpose of the project, a local H2 database was used. The database is initialized using `init.sql` script.

### Built With

Java 14

### Libraries and Frameworks

Spark, Sql2o, Gson, H2

### Tested With

JUnit 5

### Authors

Radoslaw Soltan
