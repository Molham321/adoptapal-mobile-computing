# Auth service
Der Auth Service verwaltet sensitive Daten von Nutzern und stellt Authentifizierung von Anfragen für
andere Endpunkte zur Verfügung.

Ein Nutzer kann per ID abgefragt werden insofern der Anfragende Client dafür ein Token vorweisen
kann. Für das Bearbeiten oder Verändern von Nutzerdaten ist Passwortauthentifizierung vonnöten.

Tokens können per Paswortauthentifizierung ausgestellt und gelöscht werden. Einsehen von Tokens mit
ihrer ID kann mit Tokenauthentifizerung durchgeführt werden.

## REST-Endpunkte

### Nutzer abrufen via ID (GET) (Token für Nutzer bennötigt)

> http://localhost:80/auth/user/<USERID>

Beispielantwort:
```json
{
    "email": "json@javascript.com",
    "id": 5
}

```

### Nutzer löschen (DELETE) (Passwort und Email für Nutzer bennötigt)

> http://localhost:80/auth/users/<USERID>

Beispielantwort: OK - 200

### Nutzer erstellen (POST)

> http://localhost:80/auth/users

Beispiel-Eingabe-JSON:
```json
{
    "email": "json@javascript.com",
    "password": "typescript is better"
}
```

Beispielantwort:
```json
{
    "email": "json@javascript.com",
    "id": 5
}
```

### Nutzer aktualisieren (PUT) (Passwort und Email für Nutzer bennötigt)

> http://localhost:80/auth/user/<USERID>

Beispiel-Eingabe-JSON:
```json
{
    "email": "new@email.com"
}
```

Beispielantwort: OK - 200


### Logindaten validieren (GET) (Passwort und Email für Nutzer bennötigt)

> http://localhost:80/auth/user/<USERID>/valdiate

Beispielantwort: OK - 200


### Token anfordern (GET) (Token für Nutzer bennötigt)

> http://localhost:80/auth/token/<USERID>/<TOKENID>

Beispielantwort:
```json
{
    "expiresAt": 1709480328,
    "id": 4,
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhZG9wdGFwYWwvYXV0aCIsInN1YiI6ImFzaXVmaGdiYXFwaWZqIiwiaWF0IjoxNzA5NDc2NzI4LCJncm91cHMiOltdLCJleHAiOjE3MDk0ODAzMjgsInZhbGlkaXR5SWQiOjQsImp0aSI6IjdmMmZmNmQ0LWE5OWMtNDhkOS1hMjdlLTMzNThjZGEyMTUwYSJ9.eNJ9uk6AHG2fZeqIIj0b_iovSFvMPCcIfPZL1n2siwGcfelDNVvKbP1nETMkNvGqA-F4qS0xuLaQ1seD1nKXypxE-YvDg1yuEwAiMIfByqGmwpd_vIckcOOwYpx3JYWm3wua2wpWwjtiFCJ5WVtNI6MX2UiSTXLuxi-MxL2EsA9eSOVBGeRvfE3kFl-LfjZB_ZjvUl4aMvO9lq2uPgpVVSSUPQsxNp_XwDdx9XTgGp-KDeovwG3eoj__bPPJfUUuADlUOfPdA03RyZhar0rKCkZCtreDwumIzDLR8uUImQ4pXqYsOIRF9mlexVEKmvDyplo-mYi9ydDAP97CYIksvg"
}
```


### Alle Token anfordern (GET) (Token für Nutzer bennötigt)

> http://localhost:80/auth/token/<USERID>/all

Beispielantwort:
```json
[
    {
        "expiresAt": 1709479632,
        "id": 3,
        "userId": 1
    },
    {
        "expiresAt": 1709480635,
        "id": 5,
        "userId": 1
    },
    {
        "expiresAt": 1709480635,
        "id": 6,
        "userId": 1
    },
    {
        "expiresAt": 1709480636,
        "id": 7,
        "userId": 1
    }
]```


### Neues Token erstellen (GET) (PAsswort und Email für Nutzer bennötigt)

> http://localhost:80/auth/token/<USERID>/new

Beispielantwort:
```json
{
    "expiresAt": 1709480328,
    "id": 4,
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJhZG9wdGFwYWwvYXV0aCIsInN1YiI6ImFzaXVmaGdiYXFwaWZqIiwiaWF0IjoxNzA5NDc2NzI4LCJncm91cHMiOltdLCJleHAiOjE3MDk0ODAzMjgsInZhbGlkaXR5SWQiOjQsImp0aSI6IjdmMmZmNmQ0LWE5OWMtNDhkOS1hMjdlLTMzNThjZGEyMTUwYSJ9.eNJ9uk6AHG2fZeqIIj0b_iovSFvMPCcIfPZL1n2siwGcfelDNVvKbP1nETMkNvGqA-F4qS0xuLaQ1seD1nKXypxE-YvDg1yuEwAiMIfByqGmwpd_vIckcOOwYpx3JYWm3wua2wpWwjtiFCJ5WVtNI6MX2UiSTXLuxi-MxL2EsA9eSOVBGeRvfE3kFl-LfjZB_ZjvUl4aMvO9lq2uPgpVVSSUPQsxNp_XwDdx9XTgGp-KDeovwG3eoj__bPPJfUUuADlUOfPdA03RyZhar0rKCkZCtreDwumIzDLR8uUImQ4pXqYsOIRF9mlexVEKmvDyplo-mYi9ydDAP97CYIksvg"
}
```


### Token löschen (DELETE) (Passwort und Email für Nutzer bennötigt)

> http://localhost:80/auth/token/<USERID>/<TOKENID>

Beispielantwort: OK - 200


### Token validieren (GET) (Token für Nutzer bennötigt)

> http://localhost:80/auth/token/<USERID>/valdiate

Beispielantwort: OK - 200
