# 📖 Bachelorarbeit – Testpyramide vs. Honeycomb-Modell: Ein Vergleich von Teststrategien im Kontext einer Webanwendung

## 📌 Übersicht
Dies ist die Webanwendung für meine Bachelorarbeit mit dem Fokus auf Teststrategien (**Testpyramide vs. Honeycomb-Modell**) und dem Vergleich von **Datenbanken** (**H2 vs. produktionsnahe Datenbanken mit Testcontainers**).
Hier ist ein Design Entwurf ing Figma. Hier ist der Link:

https://www.figma.com/design/luRuvzkRmb6EM30SW5EKuq/BA-User-Journey?node-id=0-1&t=W6ESECmhGxaol9qV-1
## 🎯 Ziel der Arbeit
- Vergleich der **Testpyramide** und des **Testing-Honeycomb-Modells** hinsichtlich Fehlererkennungsrate und Kosten-Nutzen-Verhältnis.
- Analyse der **H2-Datenbank** vs. **MySQL (Testcontainers)** in Bezug auf Laufzeit und Komplexität.
- Untersuchung der Auswirkungen auf eine **monolithische Webanwendung**.

## 🏗️ Architektur
Die Anwendung basiert auf einer **3-Schichten-Architektur**:

1. **Web Layer**: Enthält die REST-Controller zur Kommunikation mit dem Frontend.
2. **Service Layer**: Enthält die Geschäftslogik.
3. **Repository Layer**: Verwaltet den Datenzugriff mit JPA-Repositories.

### 📂 Verzeichnisstruktur
```
/backend
  ├── src/honeycomb
  ├── src/main
  │   ├── java/org.venow.backend
  │   │   ├── config
  │   │   ├── domain.model
  │   │   ├── infrastructure
  │   │   ├── service
  │   │   ├── web
  │   │   ├── BackendApplication.java
  │   ├── resources
  ├── src/testpyramid
/frontend
  ├── src/components
  ├── src/pages
  ├── tailwind.config.js
  ├── package.json
```

## 🚀 Setup & Installation

### Voraussetzungen
- **Java 21+** (empfohlen über SDKMAN!)
- **Node.js** (für das Frontend) -> v20.10.0
- **Docker** (für Testcontainers)
- **MySQL** (optional, falls nicht über Testcontainers genutzt)

### Datenbank und Backend starten
```bash
cd backend
docker-compose up -d
```

```bash
./gradlew bootRun
```
 Falls **Testcontainers** genutzt wird, startet die Datenbank automatisch.

### Frontend starten
```bash
cd frontend
npm install
npm run dev
```
### Testuser für die Anwendung
```bash
ORGANIZER (Veranstalter):
email: luu@example.com
password: password123

EXHIBITOR (Schausteller):
email: showman@example.com
password: password123
```

## 🔍 Tests ausführen

### Alle Tests starten (Honeycomb & Testpyramide) mit JaCoCo & Pitest
```bash
./gradlew build
```
 Führt alle Tests aus und erstellt Code Coverage-Berichte mit JaCoCo sowie Mutationstests mit Pitest.

### Alternativ: Tests mit Gradle Check ausführen
```bash
gradlew check
```
Führt die Tests sowie die zugehörigen Qualitätsanalysen aus.

### Unit- & Integrationstests (Testpyramide)
```bash
gradlew test
```
Führt standardmäßig die Unit- und Integrationstests für die Testpyramide aus.

### Honeycomb-Tests separat ausführen
```bash
./gradlew honeycombTest
``` 
Führt die Tests für das Honeycomb-Testmodell aus.

### Testpyramide-Tests separat ausführen mit Testcontainern
```bash
./gradlew testPyramidTest
```
Führt die Tests für das Honeycomb-Testmodell aus.


### Testpyramide mit H2-Datenbank ausführen
```bash
./gradlew testPyramidTest -PactiveProfile=testpyramid-h2
```
Setzt das aktive Spring-Profil auf `testpyramid-h2` und führt die Tests der Testpyramide mit H2 als Datenbank aus.

### Mutation Testing für Testpyramid
```bash
./gradlew pitest
```
Führt Mutationstests mit PIT für die Testpyramide aus.

### Mutation Testing für Honeycomb
```bash
./gradlew pitestHoneycomb
```
Führt Mutationstests mit PIT für das Honeycomb-Testmodell aus.

### JaCoCo Test Coverage für Testpyramid
```bash
./gradlew jacocoTestPyramidReport
```
Erstellt einen JaCoCo Coverage-Report für die Testpyramide.

### JaCoCo Test Coverage für Honeycomb
```bash
./gradlew jacocoHoneycombReport
```
Erstellt einen JaCoCo Coverage-Report für das Honeycomb-Testmodell.


Die Reports von Jacoco und Pitest sind im Verzeichnis `build/reports` zu finden.
Davon im jacoco und im pitest Ordner `build/reports/jacoco` und `build/reports/pitest`.
Davon die index.html öffnen

## 📊 Evaluation
- **Laufzeit** der Tests (H2 vs. MySQL mit Testcontainers)
- **Fehlererkennungsrate** zwischen Testpyramide und Honeycomb-Modell
- **Vergleich der Datenbank-Performance**





