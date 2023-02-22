# Administration

The administration-service allows you to administrate triggers and webhooks. When a POST request is received from the dispatcher-service the administration-service selects the client-data for the transmitted event-name. This data includes the URL and  corresponding secret for each webhook that is registered for the respective event. The client-data is returned to the dispatcher-service via HTTP response.

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

### Webhook API

**Creating a webhook:**

`curl -H "Content-Type: application/json" -X POST -d '{"url":"https://example.com", "trigger": "eventA", "secret": "mySecret" }' http://localhost:8085/webhooks-service`

Please note that the specific event (in this case trigger:eventA) needs to be already stored in the database otherwise the request would fail. 

**Updating a webhook:**

`curl -H "Content-Type: application/json" -X PUT -d '{"id": 6, "url":"https://www.dummy.com/","trigger":"eventA","secret":"superSecret"}' http://localhost:8085/webhooks-service/6`

**Deleting a webhook:**

`curl -X DELETE "http://localhost:8085/webhooks-service/1"` 

### Accessing the Website

http://localhost:8085/webhooks-service

List Webhooks by Id: http://localhost:8085/webhooks-service/{id}

### Accessing the private Endpoint
http://localhost:8081/api/triggers

### Trigger API
The application's admin port can be used to administrate triggers. CRUD functions can be called.

**Creating a trigger:** 

`curl -H "Content-Type: application/json" -X POST -d '{"name":"eventA" }' http://localhost:8081/api/triggers`

**Deleting a trigger:**

`curl -H "Content-Type: application/json" -X DELETE -d '{"name": "eventA" }' http://localhost:8081/api/triggers/2`

Metrics
---

To see the application's metrics enter url `http://localhost:8081/metrics`
