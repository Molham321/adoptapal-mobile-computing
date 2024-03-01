# User Service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## REST-Endpunkte

### Alle Nutzer abrufen (GET)

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

> http://localhost:80/user/users

Beispiel-Eingabe-JSON:
```json
{
    "username": "example", 
    "email": "example@example2341.com", 
    "phoneNumber": "1234567890"
}
```

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

> http://localhost:80/user/users/<USERID>

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
