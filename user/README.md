# User Service

Der User Service verfaltet Nutzer und deren öffentliche Attribute.

Nutzer können per ID angefragt werden und benötigen bis auf Bearbeitung oder Löschen keine
Authentifizierung.

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
            "id": 1,
            "postalCode": "12345",
            "street": "123 Main St"
        },
        "createdTimestamp": "2024-03-01T15:56:31.558431",
        "id": 1,
        "authId": 1,
        "phoneNumber": "0123456789",
        "username": "scott"
    },
    {
        "address": {
            "city": "Smallville",
            "createdTimestamp": "2024-03-01T15:56:31.562533",
            "id": 2,
            "postalCode": "67890",
            "street": "456 Oak St"
        },
        "createdTimestamp": "2024-03-01T15:56:31.563214",
        "id": 2,
        "authId": 2,
        "username": "jdoe"
    },
    {
        "address": {
            "city": "Gotham",
            "createdTimestamp": "2024-03-01T15:56:31.563728",
            "id": 3,
            "postalCode": "54321",
            "street": "789 Pine St"
        },
        "createdTimestamp": "2024-03-01T15:56:31.58065",
        "id": 3,
        "authId": 3,
        "phoneNumber": "9876543210",
        "username": "alice"
    },
    {
        "address": {
            "city": "Springfield",
            "createdTimestamp": "2024-03-01T15:56:31.58111",
            "id": 4,
            "postalCode": "11111",
            "street": "101 Elm St"
        },
        "createdTimestamp": "2024-03-01T15:56:31.581528",
        "email": "bob@example.com",
        "id": 4,
        "authId": 4,
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
        "id": 1,
        "postalCode": "12345",
        "street": "123 Main St"
    },
    "createdTimestamp": "2024-03-01T15:56:31.558431",
    "id": 1,
    "authId": 1,
    "phoneNumber": "0123456789",
    "username": "scott"
}
```

### Nutzer löschen (DELETE) (Passwort und Email für Owner bennötigt)

> http://localhost:80/user/users/<USERID>

Beispielantwort:
```json
{
    "address": {
        "city": "Metropolis",
        "createdTimestamp": "2024-03-01T15:56:31.284316",
        "id": 1,
        "postalCode": "12345",
        "street": "123 Main St"
    },
    "createdTimestamp": "2024-03-01T15:56:31.558431",
    "id": 1,
    "authId": 1,
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
    "password": "secret",
    "phoneNumber": "1234567890"
}
```

Beispielantwort:
```json
{
    "address": {
        "city": "",
        "createdTimestamp": "2024-03-01T16:03:45.722371715",
        "id": 5,
        "postalCode": "",
        "street": ""
    },
    "createdTimestamp": "2024-03-01T16:03:45.716518813",
    "id": 5,
    "authId": 5,
    "phoneNumber": "1234567890",
    "username": "example"
}
```

### Nutzer aktualisieren (PUT) (Token für Owner bennötigt)
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
        "id": 5,
        "postalCode": "99423",
        "street": "Döllstädtstraße 8"
    },
    "createdTimestamp": "2024-03-01T16:03:45.716519",
    "deleted": false,
    "id": 5,
    "authId": 5,
    "phoneNumber": "1234567890",
    "username": "example"
}
```
