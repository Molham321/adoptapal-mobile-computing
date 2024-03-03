# animals

Der Animals Service kümmert sich um das Speichern und Bereitstellen von Tierdaten der zur Adoption verfügbaren Tiere.

Tiere können sowohl anhand ihrer ID als auch anhand der ID des Nutzers, der es hochgeladen hat, gefunden werden. Über entsprechende Anfragen an den Auth Service wird sichergestellt, dass nur angemeldete Nutzer Tierdaten hochladen können und nur der Besitzer des Tieres es löschen kann.

Desweiteren werden die Werte der Parameter "Colors" und "Animal Categories" in separaten Tabellen gespeichert, wodurch diese auch unabhängig von den Tierdaten abgefragt werden können.

## REST Endpunkte

**Alle Tiere abrufen (GET)**

> http://localhost:80/animals/animals

Beispielantwort:

    {}

**Tier abrufen via ID (GET)**

> http://localhost:80/animals/animals/1

Beispielantwort:

    {}

**Tier abrufen via OwnerID (GET)**

> http://localhost:80/animals/animals/owner/1

Beispielantwort:

**Tier erstellen (POST)**

> http://localhost:80/animals/animals

Beispiel-Eingabe-JSON:

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

Beispielantwort:

    {}

**Tier löschen (DELETE)**

> http://localhost:80/animals/animals/3

Beispielantwort:

    {}

**Alle Farben abrufen (GET)**

> http://localhost:80/animals/animals/colors

Beispielantwort:

    {}

**Farbe abrufen via ID (GET)**

> http://localhost:80/animals/animals/colors/1

Beispielantwort:

    {}

**Alle Tier-Kategorien abrufen (GET)**

> http://localhost:80/animals/animals/animalCategories

Beispielantwort:

    {}

**Tier-Kategorie abrufen via ID (GET)**

> http://localhost:80/animals/animals/animalCategories/1

Beispielantwort:

    {}
