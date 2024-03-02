# Media Service

Der Media Service kümmert sich um das Speichern beliebiger Bilddateien. 

Beim Hochladen und Speichern eines Bildes, erhält dieses zusätzlich einen Datenbankeintrag, in dem es mit einer ID, einem Speicherpfad und zusätzlichen Metadaten versehen wird, um es zukünftig einfach wiederzufinden.

Wird in der Anwendung beispielsweise zu einem neu-angelegte Tier ein Bild gespeichert, speichert das Tier in seinem Datenbank-Eintrag lediglich die ID des Bildes. Mit der ID kann das Bild später einfach per GET-Request abgerufen werden.

## REST Endpunkte

**Bild Abrufen via ID (GET)**

> http://localhost:80/media/media/1

Beispielantwort:

+ gespeicherte Bild-Datei

**Medien-Eintrag und Bild erstellen (POST)**

> http://localhost:80/media/media/upload

Beispiel-Eingabe-Form-Data (File):

+ beliebige Bild-Datei

Beispielantwort:

    {}

**Medien-Eintrag und Bild löschen (DELETE)**

> http://localhost:80/media/media/delete/1

Beispielantwort:

    {}
