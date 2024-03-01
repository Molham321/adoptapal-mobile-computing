# User Service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## REST-Endpunkte

### Alle Nutzer abrufen (GET)

Beispielquery:
> http://localhost:80/user/users

Beispielantwort:
```json

[
    {
        "address": {
            "city": "Metropolis",
            "createdTimestamp": "2024-03-01T15:56:31.284316",
            "deleted": false,
            "id": 1,
            "lastChangeTimestamp": "2024-03-01T15:56:31.28437",
            "postalCode": "12345",
            "street": "123 Main St"
        },
        "createdTimestamp": "2024-03-01T15:56:31.558431",
        "deleted": false,
        "email": "boss@example.com",
        "id": 1,
        "lastChangeTimestamp": "2024-03-01T15:56:31.558457",
        "phoneNumber": "0123456789",
        "username": "scott"
    },
    {
        "address": {
            "city": "Smallville",
            "createdTimestamp": "2024-03-01T15:56:31.562533",
            "deleted": false,
            "id": 2,
            "lastChangeTimestamp": "2024-03-01T15:56:31.562547",
            "postalCode": "67890",
            "street": "456 Oak St"
        },
        "createdTimestamp": "2024-03-01T15:56:31.563214",
        "deleted": false,
        "email": "user@example.com",
        "id": 2,
        "lastChangeTimestamp": "2024-03-01T15:56:31.563228",
        "username": "jdoe"
    },
    {
        "address": {
            "city": "Gotham",
            "createdTimestamp": "2024-03-01T15:56:31.563728",
            "deleted": false,
            "id": 3,
            "lastChangeTimestamp": "2024-03-01T15:56:31.563742",
            "postalCode": "54321",
            "street": "789 Pine St"
        },
        "createdTimestamp": "2024-03-01T15:56:31.58065",
        "deleted": false,
        "email": "alice@example.com",
        "id": 3,
        "lastChangeTimestamp": "2024-03-01T15:56:31.580665",
        "phoneNumber": "9876543210",
        "username": "alice"
    },
    {
        "address": {
            "city": "Springfield",
            "createdTimestamp": "2024-03-01T15:56:31.58111",
            "deleted": false,
            "id": 4,
            "lastChangeTimestamp": "2024-03-01T15:56:31.581135",
            "postalCode": "11111",
            "street": "101 Elm St"
        },
        "createdTimestamp": "2024-03-01T15:56:31.581528",
        "deleted": false,
        "email": "bob@example.com",
        "id": 4,
        "lastChangeTimestamp": "2024-03-01T15:56:31.581545",
        "phoneNumber": "5555555555",
        "username": "bob"
    }
]
```

### Nutzer abrufen via ID (GET)

Beispielquery:
> http://localhost:80/user/users/<USERID>

Beispielantwort:
```json
{
    "address": {
        "city": "Metropolis",
        "createdTimestamp": "2024-03-01T15:56:31.284316",
        "deleted": false,
        "id": 1,
        "lastChangeTimestamp": "2024-03-01T15:56:31.28437",
        "postalCode": "12345",
        "street": "123 Main St"
    },
    "createdTimestamp": "2024-03-01T15:56:31.558431",
    "deleted": false,
    "email": "boss@example.com",
    "id": 1,
    "lastChangeTimestamp": "2024-03-01T15:56:31.558457",
    "phoneNumber": "0123456789",
    "username": "scott"
}
```

### Nutzer abrufen via email (GET)

Beispielquery:
> http://localhost:80/user/users/<EMAIL>

Beispielantwort:
```json
{
    "address": {
        "city": "Metropolis",
        "createdTimestamp": "2024-03-01T15:56:31.284316",
        "deleted": false,
        "id": 1,
        "lastChangeTimestamp": "2024-03-01T15:56:31.28437",
        "postalCode": "12345",
        "street": "123 Main St"
    },
    "createdTimestamp": "2024-03-01T15:56:31.558431",
    "deleted": false,
    "email": "boss@example.com",
    "id": 1,
    "lastChangeTimestamp": "2024-03-01T15:56:31.558457",
    "phoneNumber": "0123456789",
    "username": "scott"
}
```

### Nutzer löschen (DELETE)

Beispielquery:
> http://localhost:80/user/users/<USERID>

Beispielantwort:
```json
{
    "address": {
        "city": "Metropolis",
        "createdTimestamp": "2024-03-01T15:56:31.284316",
        "deleted": false,
        "id": 1,
        "lastChangeTimestamp": "2024-03-01T15:56:31.28437",
        "postalCode": "12345",
        "street": "123 Main St"
    },
    "createdTimestamp": "2024-03-01T15:56:31.558431",
    "deleted": false,
    "email": "boss@example.com",
    "id": 1,
    "lastChangeTimestamp": "2024-03-01T15:56:31.558457",
    "phoneNumber": "0123456789",
    "username": "scott"
}
```

### Nutzer erstellen (POST)

Beispiel-Eingabe-JSON:
```json
{
    "username": "example", 
    "email": "example@example2341.com", 
    "phoneNumber": "1234567890"
}
```
> http://localhost:80/user/users

Beispielantwort:
```json
{
    "address": {
        "city": "",
        "createdTimestamp": "2024-03-01T16:03:45.722371715",
        "deleted": false,
        "id": 5,
        "lastChangeTimestamp": "2024-03-01T16:03:45.722378715",
        "postalCode": "",
        "street": ""
    },
    "createdTimestamp": "2024-03-01T16:03:45.716518813",
    "deleted": false,
    "email": "example@example2341.com",
    "id": 5,
    "lastChangeTimestamp": "2024-03-01T16:03:45.716540914",
    "phoneNumber": "1234567890",
    "username": "example"
}
```

### Nutzer aktualisieren (PUT)

Beispiel-Eingabe-JSON:
```json
{
    "address": {
        "street": "Döllstädtstraße 8",
        "city": "Weimar",
        "postalCode": "99423"
    }
}
```
> http://localhost:80/user/users/<USERID>

Beispielantwort:
```json
{
    "address": {
        "city": "Weimar",
        "createdTimestamp": "2024-03-01T16:03:45.722372",
        "deleted": false,
        "id": 5,
        "lastChangeTimestamp": "2024-03-01T16:06:28.183173026",
        "postalCode": "99423",
        "street": "Döllstädtstraße 8"
    },
    "createdTimestamp": "2024-03-01T16:03:45.716519",
    "deleted": false,
    "email": "example@example2341.com",
    "id": 5,
    "lastChangeTimestamp": "2024-03-01T16:06:28.183136625",
    "phoneNumber": "1234567890",
    "username": "example"
}
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./gradlew build
```
It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./build/user-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Related Guides

- RESTEasy Reactive ([guide](https://quarkus.io/guides/resteasy-reactive)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Kotlin ([guide](https://quarkus.io/guides/kotlin)): Write your services in Kotlin

## Provided Code

### RESTEasy Reactive

Easily start your Reactive RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
