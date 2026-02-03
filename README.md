# 📝 Todo-App (Java)

Eine konsolenbasierte **Todo-App in Java 21**, die Aufgaben (**Tasks**) und **Kategorien** verwaltet.  
Die Anwendung ist klar in **Domain-, Service-, Persistence- und Presentation-Schicht** gegliedert und unterstützt sowohl **In-Memory-** als auch **SQLite-Persistenz**.  
Die Business-Logik ist mit **JUnit 5** getestet.

---

## ✨ Features

- Aufgaben (**Tasks**) anlegen, anzeigen, bearbeiten und löschen
- Aufgabenstatus verwalten (`OPEN`, `DONE`, …)
- Kategorien für Tasks
- Persistenz:
  - **In-Memory** (für Tests & Entwicklung)
  - **SQLite** (produktive Speicherung)
- Konsolenbasierte Benutzeroberfläche
- Saubere Trennung von Fachlogik, Persistenz und Ein-/Ausgabe
- Unit-Tests für die Service-Schicht

---

## 🧱 Architektur

Die Anwendung folgt einer **Schichtenarchitektur** mit klaren Zuständigkeiten:


### Domain (`domain`)
Fachliche Modelle ohne Abhängigkeiten:
- `Task`
- `Category`
- `TaskStatus`

### Service (`service`)
Geschäftslogik und Use-Cases:
- Auflisten von Tasks
- Abschließen von Tasks
- Fachliche Regeln

### Persistence (`persistence`)
Zugriff auf den Speicher:
- SQLite-Repositories
- In-Memory-Repositories (für Tests)
- Reader / Writer / Updater / Deleter

### Presentation (`presentation`)
Konsolenbasierte Benutzeroberfläche:
- `ConsoleApp`
- `ConsoleInput` / `ConsoleOutput`
- Abstraktionen `Input` / `Output`

### Einstiegspunkt
- `Main.java`

---

## 📁 Projektstruktur


---

## 🛠️ Tech-Stack

- **Java:** 21  
- **Build-Tool:** Maven  
- **Datenbank:** SQLite  
- **Persistenz:** JDBC (`sqlite-jdbc`)  
- **Tests:** JUnit 5  

---

## ✅ Voraussetzungen

- JDK 21
- Maven

Überprüfen:
```bash
java -version
mvn -version
