# Media Service

Der Media Service kümmert sich um das Speichern beliebiger Bilddateien. 

Beim Hochladen und Speichern eines Bildes, erhält dieses zusätzlich einen Datenbankeintrag, in dem es mit einer ID, einem Speicherpfad und zusätzlichen Metadaten versehen wird, um es zukünftig einfach wiederzufinden.

Wird in der Anwendung beispielsweise zu einem neu-angelegte Tier ein Bild gespeichert, speichert das Tier in seinem Datenbank-Eintrag lediglich die ID des Bildes. Mit der ID kann das Bild später einfach per GET-Request abgerufen werden.

## REST Endpunkte

### Bild Abrufen via ID (GET)

> http://localhost:80/media/media/<MEDIAID>

Beispielantwort:

+ gespeicherte Bild-Datei

### Medien-Eintrag und Bild erstellen (POST)

> http://localhost:80/media/media/upload

Beispiel-Eingabe-Form-Data (File):

+ beliebige Bild-Datei

Beispielantwort:

```json
{
    "createdTimestamp": "2024-03-03T14:22:06.56146064",
    "deleted": false,
    "filePath": "82a92921-0c72-4738-aa3d-9e75cc3503ef",
    "id": 1,
    "lastChangeTimestamp": "2024-03-03T14:22:06.561478064",
    "mediaType": "image/jpeg"
}
```

### Medien-Eintrag und Bild löschen (DELETE)

> http://localhost:80/media/media/delete/<MEDIAID>

Beispielantwort:
```
Media with ID 1 deleted
```
