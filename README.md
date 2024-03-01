# Allgemein
Ein Mobile Computing 2 Projekt von:
- Anna-Lisa Merkel
- Erik Bünnig
- Molham Al-khodari

# Tools
## Verwendet
- IntelliJ
- Docker
- Gitlab
- Discord

## Nicht verwendet
### Kafka
Obwhol Kafka zunächst in Erwägung gezugen wurde, wurde es wegen seiner asynchronen Natur nicht
verwendet. Für die Kommunikation zwischen den Services war zwingend ein Request-Resposne-Modell
vonnöten. In der Git-History sind Überreste davon zu finden.

# Präsentationen
Präsentationen sind im Ordner [presentations][pres] zu finden.

# Testen
Der Postman [workspace][postman] kann importiert werden um die Endpunkte zu testen. Zum starten der
services muss lediglich
```sh
docker compose up -d --force-recreate --build --remove-orphans
```
ausgeführt werden. Für Änderungen welche Authentifizierung bennötigen sind in den workspace
Variablen UserId, Email und Passwort hinterlegt. Diese sind dem User-Service zu entnehmen.
Änderungen und Abfragen am Auth-Service benöigen generell diese Werte oder ein JWT. Zugriffe welche
Änderungen an Entitäten anderer Services hervorrufen, benötigen ebenfalls ein JWT oder Email/
Passwort. Diese Werte werde bei den meisten Anfragen als header mit übergeben.

Da andere Sevices nicht gemockt sind, wurden unit tests vorerst gelöscht.

# Microservices
## Auth Service
Aufgaben:
- Datenhalten von sensitiven daten wie privater email adresse oder password
- Ausstellen und Prüfen von Token
- Auth-Check (JWT) bei Abfragen
- Auth-Check (Email/Passwort) bei Update
- Auth-Check (Email/Passwort) bei Löschen

## User Service
Aufgaben:
- Datenhaltung von Nutzerdaten
- Datenhaltung von Addressdaten
- Auth-Check (JWT) bei Update
- Auth-Check (Email/Passwort) bei Löschen

## Animals Service
Aufgaben:
- Datenhalteung von Tierdaten
- Ownership-Check (JWT) bei Update

## Media Service
Aufgaben:
- Haltung von Mediendaten (Bildern)
- Bereitstellen von Mediendaten (simples CDN)

[pres]: ./docs/presentations/
[postman]: ./postman-workspace.json
