## ✨ Features

- Aufgaben anlegen und anzeigen
- Aufgaben als erledigt markieren
- Aufgaben löschen
- Aufgaben nach Kriterien filtern
- Kategorien anlegen und anzeigen
- Kategorien Aufgaben zuordnen
- Dauerhafte Speicherung in einer SQLite-Datenbank
- In-Memory-Repositories für Tests
- Konsolenbasierte Benutzeroberfläche
- Klare Trennung von Domain, Service, Persistence und Presentation
- Automatisierte Tests mit JUnit 5
- Automatische Prüfung durch GitHub Actions

---

## 🧱 Architektur

Die Anwendung folgt einer Schichtenarchitektur mit klaren Zuständigkeiten.

### Domain (`domain`)

Enthält die fachlichen Modelle:

- `Task`
- `Category`
- `TaskStatus`

### Service (`service`)

Enthält die Geschäftslogik und Anwendungsfälle:

- Aufgaben anlegen und auflisten
- Aufgaben als erledigt markieren
- Aufgaben löschen
- Kategorien verwalten
- Eingaben validieren

### Persistence (`persistence`)

Enthält den Zugriff auf die gespeicherten Daten:

- SQLite-Repositories für die dauerhafte Speicherung
- jOOQ für Datenbankabfragen und Datenbankoperationen
- In-Memory-Repositories für automatisierte Tests
- Reader-, Writer-, Updater- und Deleter-Schnittstellen

Die Verbindung zur SQLite-Datenbank wird über den SQLite-JDBC-Treiber hergestellt. Die SQL-Operationen werden über die jOOQ-API ausgeführt.

### Presentation (`presentation`)

Enthält die konsolenbasierte Benutzeroberfläche:

- `ConsoleApp`
- `ConsoleInput`
- `ConsoleOutput`
- Abstraktionen `Input` und `Output`

### Einstiegspunkt

- `Main.java`

---

## 🛠️ Tech-Stack

- **Java:** 21
- **Build-Tool:** Maven
- **Datenbank:** SQLite
- **Datenbankzugriff:** jOOQ
- **Datenbanktreiber:** SQLite JDBC
- **Tests:** JUnit 5
- **Continuous Integration:** GitHub Actions 

## 🚀 Anwendung starten

Das Projekt kann mit Maven gestartet werden:

```bash
mvn exec:java
```

Anschließend erscheint das Konsolenmenü:

```text
1) Aufgabe anlegen
2) Aufgaben anzeigen
3) Aufgabe erledigen
4) Aufgabe löschen
5) Kategorie anlegen
6) Kategorien anzeigen
0) Beenden
```

Die SQLite-Datenbank wird im Verzeichnis `data` gespeichert.

---

## 🧪 Tests ausführen

Alle automatisierten Tests werden mit folgendem Befehl ausgeführt:

```bash
mvn clean test
```

Aktuell enthält das Projekt sechs automatisierte Tests.

Ein erfolgreicher Testlauf endet beispielsweise mit:

```text
Tests run: 6, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Der vollständige Maven-Build kann so geprüft werden:

```bash
mvn clean verify
```

Zusätzlich führt GitHub Actions die Tests bei Änderungen am Repository automatisch aus.

