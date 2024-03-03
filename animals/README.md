# animals

Der Animals Service kümmert sich um das Speichern und Bereitstellen von Tierdaten der zur Adoption verfügbaren Tiere.

Tiere können sowohl anhand ihrer ID als auch anhand der ID des Nutzers, der es hochgeladen hat, gefunden werden. Über entsprechende Anfragen an den Auth Service wird sichergestellt, dass nur angemeldete Nutzer Tierdaten hochladen können und nur der Besitzer des Tieres es löschen kann.

Desweiteren werden die Werte der Parameter "Colors" und "Animal Categories" in separaten Tabellen gespeichert, wodurch diese auch unabhängig von den Tierdaten abgefragt werden können.

## REST Endpunkte

### Alle Tiere abrufen (GET)

> http://localhost:80/animals/animals

Beispielantwort:

```json
[
    {
        "animalCategory": 1,
        "birthday": "2018-07-30",
        "color": 2,
        "createdTimestamp": "2024-03-03T14:19:33.169716",
        "description": "Albert die schwarze Katze",
        "id": 1,
        "image": 1,
        "male": true,
        "name": "Albert",
        "owner": 1,
        "weight": 2.5
    },
    {
        "animalCategory": 2,
        "birthday": "2020-12-09",
        "color": 7,
        "createdTimestamp": "2024-03-03T14:19:33.201726",
        "description": "Daisy die freundliche Dame",
        "id": 2,
        "image": 2,
        "male": false,
        "name": "Daisy",
        "owner": 1,
        "weight": 4.8
    }
]
```

### Tier abrufen via ID (GET)

> http://localhost:80/animals/animals/<ANIMALID>

Beispielantwort:

```json
{
    "animalCategory": 1,
    "birthday": "2018-07-30",
    "color": 2,
    "createdTimestamp": "2024-03-03T14:19:33.169716",
    "description": "Albert die schwarze Katze",
    "id": 1,
    "image": 1,
    "male": true,
    "name": "Albert",
    "owner": 1,
    "weight": 2.5
}
```

### Tier abrufen via OwnerID (GET)

> http://localhost:80/animals/animals/owner/<USERID>

Beispielantwort:

```json
[
    {
        "animalCategory": 1,
        "birthday": "2018-07-30",
        "color": 2,
        "createdTimestamp": "2024-03-03T14:19:33.169716",
        "description": "Albert die schwarze Katze",
        "id": 1,
        "image": 1,
        "male": true,
        "name": "Albert",
        "owner": 1,
        "weight": 2.5
    },
    {
        "animalCategory": 2,
        "birthday": "2020-12-09",
        "color": 7,
        "createdTimestamp": "2024-03-03T14:19:33.201726",
        "description": "Daisy die freundliche Dame",
        "id": 2,
        "image": 2,
        "male": false,
        "name": "Daisy",
        "owner": 1,
        "weight": 4.8
    }
]
```

### Tier erstellen (POST) (Token für Owner bennötigt)

> http://localhost:80/animals/animals

Beispiel-Eingabe-JSON:

```json
{
    "name": "Chom-Chom",
    "description": "merkwürdiger Wurm",
    "color": 11,
    "male": true,
    "animalCategory": 7,
    "birthday": "2023-12-31",
    "weight": 2.6,
    "owner": 1
}
```

Beispielantwort:

```json
{
    "animalCategory": 7,
    "birthday": "2023-12-31",
    "color": 11,
    "createdTimestamp": "2024-03-03T14:27:14.35532224",
    "description": "merkwürdiger Wurm",
    "id": 3,
    "image": 0,
    "male": true,
    "name": "Chom-Chom",
    "owner": 1,
    "weight": 2.6
}
```

### Tier löschen (DELETE) (Token für Owner bennötigt)

> http://localhost:80/animals/animals/<ANIMALID>

Beispielantwort:

```
Animal with ID 3 deleted
```

### Alle Farben abrufen (GET)

> http://localhost:80/animals/animals/colors

Beispielantwort:

```json
[
    {
        "createdTimestamp": "2024-03-03T14:19:33.125281",
        "id": 1,
        "name": "Weiß"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.163982",
        "id": 2,
        "name": "Schwarz"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.1645",
        "id": 3,
        "name": "Rot"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.16467",
        "id": 4,
        "name": "Grün"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.164836",
        "id": 5,
        "name": "Orange"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.164994",
        "id": 6,
        "name": "Gelb"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.165153",
        "id": 7,
        "name": "Braun"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.165311",
        "id": 8,
        "name": "Grau"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.165465",
        "id": 9,
        "name": "Blau"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.165626",
        "id": 10,
        "name": "Mehrfarbig"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.165809",
        "id": 11,
        "name": "Andere"
    }
]
```

### Farbe abrufen via ID (GET)

> http://localhost:80/animals/animals/colors/<COLORID>

Beispielantwort:

```json
{
    "createdTimestamp": "2024-03-03T14:19:33.125281",
    "id": 1,
    "name": "Weiß"
}
```

### Alle Tier-Kategorien abrufen (GET)

> http://localhost:80/animals/animals/animalCategories

Beispielantwort:

```json
[
    {
        "createdTimestamp": "2024-03-03T14:19:33.166851",
        "id": 1,
        "name": "Katze"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.168049",
        "id": 2,
        "name": "Hund"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.168417",
        "id": 3,
        "name": "Fisch"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.168756",
        "id": 4,
        "name": "Reptil"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.169036",
        "id": 5,
        "name": "Nagetier"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.169219",
        "id": 6,
        "name": "Vogel"
    },
    {
        "createdTimestamp": "2024-03-03T14:19:33.169371",
        "id": 7,
        "name": "Andere"
    }
]
```

### Tier-Kategorie abrufen via ID (GET)

> http://localhost:80/animals/animals/animalCategories/<CATEGORYID>

Beispielantwort:

```json
{
    "createdTimestamp": "2024-03-03T14:19:33.166851",
    "id": 1,
    "name": "Katze"
}
```
