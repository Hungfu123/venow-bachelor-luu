# venow-bachelor-luu
Figma Link für das Design: 
https://www.figma.com/design/luRuvzkRmb6EM30SW5EKuq/BA-User-Journey?node-id=0-1&t=OUGIlCaT1bQqkhsQ-1
📖 Bachelorarbeit – [Titel deines Projekts]

📌 Übersicht

Dies ist die Webanwendung für meine Bachelorarbeit mit dem Fokus auf Teststrategien (Testpyramide vs. Honeycomb-Modell) und dem Vergleich von Datenbanken (H2 vs. produktionsnahe Datenbanken mit Testcontainers).

🎯 Ziel der Arbeit

Vergleich der Testpyramide und des Testing-Honeycomb-Modells hinsichtlich Fehlererkennungsrate und Kosten-Nutzen-Verhältnis.

Analyse der H2-Datenbank vs. MySQL (Testcontainers) in Bezug auf Laufzeit und Komplexität.

Untersuchung der Auswirkungen auf eine monolithische Webanwendung.

🏗️ Architektur

Die Anwendung basiert auf einer 3-Schichten-Architektur:

Web Layer: Enthält die REST-Controller zur Kommunikation mit dem Frontend.

Service Layer: Enthält die Geschäftslogik.

Repository Layer: Verwaltet den Datenzugriff mit JPA-Repositories.

📂 Verzeichnisstruktur

/backend
  ├── src/main/java
  │   ├── com.example.project
  │   │   ├── controller  # Web Layer (REST API)
  │   │   ├── service  # Business-Logik
  │   │   ├── repository  # Datenbankzugriff (JPA)
  │   │   ├── model  # JPA-Entitäten
  ├── src/test/java  # Testpyramide
  ├── src/test/honeycomb  # Honeycomb-Modell
/frontend
  ├── src/components
  ├── src/pages
  ├── tailwind.config.js
  ├── package.json

🚀 Setup & Installation

Voraussetzungen

Java 17+ (empfohlen über SDKMAN!)

Node.js + npm (für das Frontend)

Docker (für Testcontainers)

MySQL (optional, falls nicht über Testcontainers genutzt)

Backend starten

cd backend
./mvnw spring-boot:run

Falls Testcontainers genutzt wird, startet die Datenbank automatisch.

Frontend starten

cd frontend
npm install
npm run dev

🔍 Tests ausführen

Unit- & Integrationstests (Testpyramide)

./mvnw test

Honeycomb-Tests separat ausführen

mvn test -P honeycomb

📊 Evaluation

Laufzeit der Tests (H2 vs. MySQL mit Testcontainers)

Fehlererkennungsrate zwischen Testpyramide und Honeycomb-Modell

Vergleich der Datenbank-Performance

🛠️ Mögliche Erweiterungen

Weitere Testfälle für komplexe Szenarien

CI/CD-Integration

Verbesserte Datenbank-Migration mit Flyway

📜 Lizenz
