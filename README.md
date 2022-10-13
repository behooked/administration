# Administration

How to start the Administration application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/administration-1.0.0-SNAPSHOT.jar server config.yml`

### Requirements
On your local machine, PostgresSQL has to be be installed and a database has to be created.
The application's configuration file "config.yml" will have to be adjusted accordingly:

 database:  
  driverClass: org.postgresql.Driver  
  user: ***USERNAME***  
  password: ***PASSWORD***  
  url: jdbc:postgresql: ***DATABASE_NAME***  
  properties:  
    ***hibernate.hbm2ddl.auto: create***    
    
The last line *hibernate.hbm2ddl.auto: create* guarantees that the database tables will be created automatically by Hibernate.

### Call CRUD Functions: 

-POST 

`curl -H "Content-Type: application/json" -X POST -d '{"url":"https://example.com", "trigger": "EventA", "secret": "mySecret" }' http://localhost:8085/webhooks-service`

Please note that the specific event (in this case trigger:EventA) needs to be already stored in the database otherwise the request would fail. 

-PUT

`curl -H "Content-Type: application/json" -X PUT -d '{"id": 6, "url":"https://www.dummy.com/","trigger":"EventB","secret":"superSecret"}' http://localhost:8085/webhooks-service/6`

-DELETE

`curl -X DELETE "http://localhost:8080/webhooks-service/1"` 

### Accessing the Website

http://localhost:8085/webhooks-service

List Webhooks by Id: http://localhost:8085/webhooks-service/{id}

### Accessing private Endpoint
The application's admin port can be used to administrate triggers. CRUD functions can be called.
http://localhost:8081/api/triggers

Metrics
---

To see the application's metrics enter url `http://localhost:8081/metrics`
