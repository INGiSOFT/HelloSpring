# HelloSpring – Personen erfassen mit Spring Boot

Eine kleine Demo-Webanwendung für Einsteigerinnen und Einsteiger.
Man kann Personen (Vorname und Alter) über ein Formular erfassen. Die Personen erscheinen danach in einer Liste daneben.

Die Demo zeigt die wichtigsten Grundbausteine einer Webanwendung mit **Spring Boot**:

- das **MVC-Muster** (Model – View – Controller)
- **Thymeleaf**-Templates, die HTML auf dem Server erzeugen
- **Formulare** mit Prüfung der Eingaben (Validierung) und Fehlermeldungen
- **Dependency Injection** (Spring erzeugt und verbindet die Objekte)
- eine eigene **Fehlerseite**
- modernes Aussehen mit **Tailwind CSS**, ohne eigene CSS-Datei

---

## Voraussetzungen

| Was | Version | Hinweis |
|-----|---------|---------|
| Java (JDK) | 25 | z. B. [Eclipse Temurin](https://adoptium.net/) |
| Maven | – | muss **nicht** installiert sein, das Projekt bringt den *Maven Wrapper* (`mvnw`) mit |
| IDE | beliebig | empfohlen: IntelliJ IDEA |

## Starten

**In IntelliJ:** Die Klasse `HelloSpringApplication` öffnen und auf den grünen Pfeil ▶ neben `main` klicken.

**Auf der Kommandozeile** (im Projektordner):

```bash
./mvnw spring-boot:run
```

Unter Windows (cmd/PowerShell) heisst es `mvnw.cmd spring-boot:run`.

Danach im Browser öffnen: **<http://localhost:8080>**

Beenden mit `Ctrl+C` (Kommandozeile) bzw. dem roten Stopp-Knopf (IntelliJ).

> **Tipp:** Änderungen an Java-Dateien oder Templates werden erst nach einem Neustart
> (bzw. einem *Build* in IntelliJ) sichtbar.

## Ausprobieren

1. Einen Vornamen und ein Alter eingeben und auf **Person hinzufügen** klicken.
2. Das Formular **ohne Vorname** absenden, dann erscheint eine rote Fehlermeldung.
3. Ein **negatives Alter** eingeben, auch dann erscheint eine Fehlermeldung.
4. Nach dem Hinzufügen **F5** drücken: Die Person wird *nicht* doppelt gespeichert (siehe [Post/Redirect/Get](#postredirectget)).
5. Eine Adresse aufrufen, die es nicht gibt, z. B. <http://localhost:8080/gibtesnicht>, dann erscheint die eigene Fehlerseite.
6. Das Browserfenster schmal machen: Erfassung und Liste stehen dann untereinander statt nebeneinander.

> ⚠️ Die Personen werden nur im **Arbeitsspeicher** gehalten. Nach einem Neustart sind sie wieder weg.

## Projektstruktur

```
HelloSpring
├── pom.xml                          Maven-Bauanleitung: Java-Version, Bibliotheken
├── mvnw / mvnw.cmd                  Maven Wrapper (Maven ohne Installation)
└── src
    ├── main
    │   ├── java/org/example/hellospring
    │   │   ├── HelloSpringApplication.java   Startpunkt (main-Methode)
    │   │   ├── Person.java                   Model: Vorname + Alter
    │   │   ├── PersonController.java         Controller: nimmt Anfragen entgegen
    │   │   └── PersonRepository.java         speichert die Personen (im Arbeitsspeicher)
    │   └── resources
    │       ├── application.properties        Einstellungen der Anwendung
    │       ├── messages.properties           alle Texte / Fehlermeldungen
    │       └── templates
    │           ├── index.html                View: Hauptseite (Formular + Liste)
    │           └── error.html                View: eigene Fehlerseite
    └── test/java/org/example/hellospring
        └── HelloSpringApplicationTests.java  prüft, ob die Anwendung startet
```

Jede Datei ist ausführlich kommentiert. Es lohnt sich, sie in der Reihenfolge oben zu lesen.

## So funktioniert's

### Das MVC-Muster

Die Anwendung ist in drei Rollen aufgeteilt:

| Rolle | Datei | Aufgabe |
|-------|-------|---------|
| **Model** | `Person.java` | die Daten |
| **View** | `index.html` | wie die Daten angezeigt werden |
| **Controller** | `PersonController.java` | nimmt Anfragen entgegen, steuert den Ablauf |

Dazu kommt das **Repository** (`PersonRepository.java`). Es kümmert sich ums Speichern, damit der Controller nicht wissen muss, *wo* die Daten liegen.

### Ablauf: Seite anzeigen (GET)

```
Browser                    PersonController               PersonRepository      index.html
   │  GET /index                 │                               │                   │
   │────────────────────────────>│  findAll()                    │                   │
   │                             │──────────────────────────────>│                   │
   │                             │<──────── Liste ───────────────│                   │
   │                             │  Model: person, persons       │                   │
   │                             │──────────────────────────────────────────────────>│
   │<──────────────────────────────────────── fertiges HTML ─────────────────────────│
```

1. Der Browser ruft `http://localhost:8080/index` auf.
2. Spring findet die passende Methode über `@GetMapping("/index")`, das ist `index(..)`.
3. Die Methode legt ein leeres `Person`-Objekt (für das Formular) und die Liste aller Personen ins **Model**.
4. Sie gibt `"index"` zurück. Spring verwendet daraufhin `templates/index.html`.
5. Thymeleaf ersetzt alle `th:`-Attribute durch echte Werte und schickt reines HTML an den Browser.

### Ablauf: Formular absenden (POST)

1. Klick auf **Person hinzufügen** schickt die Felder `firstName` und `age` per `POST` an `/index`.
2. Spring ruft `addPerson(..)` auf und füllt ein neues `Person`-Objekt automatisch mit den Werten (`@ModelAttribute`).
3. Die Eingaben werden geprüft. Fehler landen im `BindingResult`.
4. **Bei Fehlern:** Die Seite wird erneut angezeigt, mit Fehlermeldungen und den bereits eingegebenen Werten.
5. **Ohne Fehler:** Die Person wird gespeichert, und der Browser wird zu `/index` weitergeleitet.

### Post/Redirect/Get

Nach erfolgreichem Speichern gibt der Controller `"redirect:/index"` zurück statt direkt `"index"`.
Der Browser lädt dann die Seite neu per `GET`. Drückt man danach **F5**, wird nur die Seite neu geladen. Das Formular wird nicht ein zweites Mal abgeschickt, und es entsteht keine doppelte Person.

### Fehlermeldungen und Texte

Der Controller meldet einen Fehler nur mit einem **Code**, z. B.:

```java
bindingResult.rejectValue("firstName", "required");
```

Der eigentliche Text steht in `messages.properties`:

```properties
required.person.firstName=Bitte einen Vornamen eingeben.
```

So bleiben Java-Code und Texte getrennt. Will man die Anwendung übersetzen, braucht es nur eine weitere Datei, z. B. `messages_en.properties`.

### Dependency Injection

Nirgends im Code steht `new PersonController(..)` oder `new PersonRepository()`.
Spring findet beim Start alle Klassen mit `@Controller` bzw. `@Repository`, erzeugt sie und übergibt das Repository automatisch an den Konstruktor des Controllers.

## Thymeleaf – kleiner Spickzettel

| Ausdruck | Bedeutung | Beispiel aus `index.html` |
|----------|-----------|---------------------------|
| `${...}` | Variable aus dem Model | `${persons}` |
| `*{...}` | Feld des Formular-Objekts (`th:object`) | `*{firstName}` |
| `@{...}` | Link innerhalb der Anwendung | `@{/index}` |
| `th:text` | ersetzt den Inhalt eines Elements | `th:text="${p.firstName}"` |
| `th:each` | Schleife | `th:each="p : ${persons}"` |
| `th:if` / `th:unless` | Element nur unter einer Bedingung anzeigen | `th:if="${#lists.isEmpty(persons)}"` |
| `th:field` | verbindet ein Eingabefeld mit dem Objekt | `th:field="*{age}"` |
| `th:errors` | zeigt die Fehlermeldung eines Feldes | `th:errors="*{age}"` |

## Tests ausführen

```bash
./mvnw test
```

Der vorhandene Test startet die Anwendung einmal und prüft, dass dabei kein Fehler auftritt.

## Verwendete Technologien

- [Spring Boot 4](https://spring.io/projects/spring-boot): Grundgerüst der Anwendung, inkl. Webserver
- [Spring Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html): Controller, Formulare, Validierung
- [Thymeleaf](https://www.thymeleaf.org/): HTML-Templates
- [Tailwind CSS](https://tailwindcss.com/): Gestaltung, eingebunden als [WebJar](https://www.webjars.org/)
- [Maven](https://maven.apache.org/): Build und Verwaltung der Bibliotheken

## Ideen zum Weitermachen

- Weitere Felder hinzufügen, z. B. Nachname oder E-Mail (in `Person.java`, `index.html` und `messages.properties`)
- Einen **Löschen**-Knopf pro Person einbauen (neue `@PostMapping`-Methode)
- Die Validierung mit **Bean Validation** (`@NotBlank`, `@Min(0)`) statt von Hand umsetzen
- Die Personen in einer echten **Datenbank** speichern (z. B. H2 + Spring Data JPA), damit sie einen Neustart überleben
- Einen Test schreiben, der das Formular absendet (`MockMvc`)
