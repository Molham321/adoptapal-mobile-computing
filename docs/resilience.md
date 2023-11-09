# Example with an unreliable Service

This project demonstrates the usage of Traefik and Docker Compose in combination with Quarkus services. It is meant as an example for the Mobile Computing 2 course at University of Applied Sciences Erfurt during 2020.

In contrast to the Dockerfiles from Quarkus project templates, the ones found in this example use multi-stage builds to simplify the process. Thus, one no longer needs to run Maven or GraalVM locally to build the project and afterwards create those containers. Instead, everything is performed during Docker image creation. Beside being easier during development, this should also help with CI/CD pipelines.

---- 

## Branch: Version 2 Unreliable Service

This is the second version of the example. It's based on the first version and demonstrates how to cope with unreliable services using MicroProfile Fault Tolerance.

Be aware that this - in it's current form - is not a full, well-structured application. It just demonstrates the MicroProfile Fault Tolerance Spec in a more or less minimal design.

So, what has changed, what is new in this version?

1. We have a new service - `unreliable-quarkus-service` - that offers some faulty endpoints. For example, one endpoint will fail on roughly 50% of all invocations. Another one might take longer than we are willing to wait.

2. We extended our first Quarkus service - `quarkus-web-starter` - to access that second, unreliable service by using the Quarkus REST Client API and applying some of the measures provided by *MicroProfile Fault Tolerance* to handle the error cases (see next point).

3. We use *MicroProfile Fault Tolerance* API to cope with different error scenarios during remote communication. That API offers the following mechanisms (all applied via annotations - see `quarkus-web-starter` -> `UnreliableService.kt` and `Resource.kt`):

    - *Retry*: In case of an error, retry the call the given number of times.
    - *Fallback*: In case of an error, the fallback handler will answer the call.
    - *Timeout*: Issue a TimeoutException if the call takes to long. 
    - *Circuit Breaker*: Deny further invocations of a method if it failed a specified number of times before. Should decrease the chance that the called system is overloaded and breaks down completely.  

    Even if the most common use case for those mechanisms is network communication, one can use them in much more areas. For example, database access comes to mind. In this example, we applied them directly on our REST Client to handle those failures right there. However, in a more complex application, where network data retrieval is performed by a Repository class, that Repository might be a better place for an annotation like `@Fallback` because it should be able to answer the request with some local, cached data.

4. We renamed the `traefik` service to `gateway` in the Docker Compose file because we use it for internal routing between our Quarkus services, too. Therefore, the new name should better reflect that. 

See code comments for further details. 

New endpoints are marked with **New** below.

----

## Run the example

You need Docker and Docker Compose installed on your system. 

1. Clone the repo and run the following command from root folder:

    `docker-compose up`

2. Access Traefik Dashboard by pointing your browser to <http://localhost>

3. Access Hello Endpoint from Quarkus service: <http://localhost/web/hello>
    
    Access DB Test Endpoint from Quarkus service: <http://localhost/web/db-test>

    **New** Access Retry Endpoint from Quarkus service: <http://localhost/web/remote-retry-hello>

    **New** Access Fallback Endpoint from Quarkus service: <http://localhost/web/remote-fallback-hello>

    **New** Access Timeout Endpoint from Quarkus service: <http://localhost/web/remote-timeout-hello>

    **New** Access Circuit Breaker Endpoint from Quarkus service: <http://localhost/web/remote-circuit-breaker-hello>

4. Run in detached mode (e.g. in background):

    `docker-compose up -d`

    **New**: `docker-compose -p mc2 up`
    
    Option `-p` to set a project name. Without it, the parent folder name is used. Should lead to nicer service names in Traefik too.

5. Restart single service (in this case `quarkus`)

    `docker-compose up quarkus`

6. Rebuild images during startup:

    `docker-compose up --build`

7. Scale a singe service (in this case, start three instances of the quarkus service):

    `docker-compose up --scale quarkus=3`

    Traefik will pick up all instances and offer a simple loadbalancer. You can check this by invoking the `hello` endpoint - the response contains the containers IP address which should be different on subsequent invokations.

8. Stop the system:

    `docker-compose down`

For a full Docker Compose reference see: <https://docs.docker.com/compose/reference/overview/>

## Quarkus JVM or Native Mode

Have a look at `docker-compose.yml` lines 33-36 -- depending on the used Dockerfile, Docker Compose will either run the JVM or Native version. See those Dockerfiles for more information.

## References

- Quarkus Fault Tolerance Guide: <https://quarkus.io/guides/microprofile-fault-tolerance>
- MicroProfile Fault Tolerance Spec: <https://github.com/eclipse/microprofile-fault-tolerance>
- Examples on MicroProfile Fault Tolerance: <https://rieckpil.de/whatis-eclipse-microprofile-fault-tolerance/>

- Circuit Breaker Pattern: <https://microservices.io/patterns/reliability/circuit-breaker.html>

- Quarkus REST Client Guide: <https://quarkus.io/guides/rest-client>