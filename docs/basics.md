# Basic Setup

This project demonstrates the usage of Traefik and Docker Compose in combination with Quarkus services. It is meant as an example for the Mobile Computing 2 course at University of Applied Sciences Erfurt during 2020.

In contrast to the Dockerfiles from Quarkus project templates, the ones found in this example use multi-stage builds to simplify the process. Thus, one no longer needs to run Maven or GraalVM locally to build the project and afterwards create those containers. Instead, everything is performed during Docker image creation. Beside being easier during development, this should also help with CI/CD pipelines.

---- 

## Branch: Version 1 Quarkus

This is the first version of the example. It demonstrates a basic setup consisting of the following parts/containers:

- *Quarkus* service with *Kotlin* as main language and access to a database: 
    - *PostgreSQL* in production
    - *H2* during tests
- *PostgreSQL* database for storage
- *Traefik* reverse proxy to handle incoming traffic and routing
- A *Docker Compose* configuration to start everything
- A *GitLab CI/CD* configuration which builds the Quarkus service, runs tests and finally creates a Docker image that is stored in the GitLab registry (currently not used)

See code comments for further details. 

----

## Quarkus Development

Have a look at the README.md in `quarkus-web-starter` folder. Or a look at the Quarkus webpage: <https://quarkus.io/get-started/> 

----

## Run the example

You need Docker and Docker Compose installed on your system. 

1. Clone the repo and run the following command from root folder:

    `docker-compose up`

2. Access Traefik Dashboard by pointing your browser to <http://localhost>

3. Access Hello Endpoint from Quarkus service: <http://localhost/web/hello>
    
    Access DB Test Endpoint from Quarkus service: <http://localhost/web/db-test>

4. Run in detached mode (e.g. in background):

    `docker-compose up -d`

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

- Traefik: <https://traefik.io>
- Docker Compose: <https://docs.docker.com/compose/>
- Docker Compose file: <https://docs.docker.com/compose/compose-file/>
- Quarkus: <https://quarkus.io>
- Quarkus Database Access with Panache: <https://quarkus.io/guides/hibernate-orm-panache>
- Quarkus Database Access with Panache & Kotlin: <https://quarkus.io/guides/hibernate-orm-panache-kotlin>
- Maven Docker Image: <https://hub.docker.com/_/maven>
- Postgres Docker Image: <https://hub.docker.com/_/postgres>
