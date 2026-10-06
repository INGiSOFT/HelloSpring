package org.example.hellospring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Der <b>Controller</b> (das "C" in MVC – Model View Controller).
 * <p>
 * Er nimmt die Anfragen des Browsers entgegen, entscheidet, was passieren soll, und
 * wählt am Schluss die passende <b>View</b> (das HTML-Template) aus. Das Zusammenspiel:
 * <pre>
 *   Browser  --Anfrage-->  Controller  --liest/speichert-->  PersonRepository
 *                              |
 *                              +--Daten ins Model-->  View (index.html)  --HTML-->  Browser
 * </pre>
 * {@code @Controller} sagt Spring, dass diese Klasse Web-Anfragen beantwortet und dass
 * der zurückgegebene {@code String} der <i>Name eines Templates</i> ist
 * (z.&nbsp;B. {@code "index"} → {@code src/main/resources/templates/index.html}).
 */
@Controller
public class PersonController {

    private final PersonRepository personRepository;

    /**
     * Konstruktor mit <b>Dependency Injection</b>.
     * <p>
     * Wir erzeugen das Repository nicht selbst mit {@code new}. Stattdessen sehen wir
     * es als Parameter vor, und Spring übergibt beim Start automatisch das passende
     * Objekt (das es dank {@code @Repository} bereits erzeugt hat).
     *
     * @param personRepository wird von Spring übergeben
     */
    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Zeigt die Seite an. Wird aufgerufen, wenn der Browser die Seite <b>abruft</b>
     * (HTTP-Methode {@code GET}), z.&nbsp;B. durch Eingabe der Adresse oder einen Link.
     * <p>
     * Das {@link Model} ist wie ein Koffer mit Daten, den der Controller packt und an
     * das Template weitergibt. Im Template kann man die Daten dann über ihren Namen
     * ansprechen, z.&nbsp;B. {@code ${persons}}.
     *
     * @param model wird von Spring bereitgestellt; hier legen wir Daten für das Template ab
     * @return Name des Templates, das angezeigt werden soll
     */
    @GetMapping({"/", "/index"})
    public String index(Model model) {
        // Ein leeres Person-Objekt für das Formular (die Eingabefelder sind anfangs leer)
        model.addAttribute("person", new Person());
        // Alle bisher erfassten Personen für die Liste
        model.addAttribute("persons", personRepository.findAll());

        // Spring Boot sucht nun nach templates/index.html
        return "index";
    }

    /**
     * Verarbeitet das <b>abgeschickte Formular</b> (HTTP-Methode {@code POST}).
     * <p>
     * Ablauf:
     * <ol>
     *     <li>Spring erzeugt ein {@link Person}-Objekt und füllt es mit den Werten aus
     *         dem Formular ({@code @ModelAttribute}).</li>
     *     <li>Wir prüfen die Eingaben (<i>Validierung</i>). Fehler werden im
     *         {@link BindingResult} gesammelt.</li>
     *     <li>Bei Fehlern zeigen wir die Seite erneut an – mit Fehlermeldungen und den
     *         bereits eingegebenen Werten.</li>
     *     <li>Sonst speichern wir die Person und leiten den Browser zur Seite weiter.</li>
     * </ol>
     *
     * @param person        die Person mit den Werten aus dem Formular
     * @param bindingResult sammelt Fehler. Enthält schon Fehler, wenn Spring einen Wert
     *                      nicht umwandeln konnte (z.&nbsp;B. "abc" als Alter).
     *                      <b>Muss direkt nach dem {@code @ModelAttribute}-Parameter stehen!</b>
     * @param model         Daten für das Template, falls die Seite erneut angezeigt wird
     * @return Template-Name oder eine Weiterleitung ({@code "redirect:..."})
     */
    @PostMapping({"/", "/index"})
    public String addPerson(@ModelAttribute Person person, BindingResult bindingResult, Model model) {

        // --- Validierung ---
        // rejectValue(feld, code) merkt sich einen Fehler für ein bestimmtes Feld.
        // Der passende Text steht in messages.properties unter "<code>.person.<feld>",
        // z.B. "required.person.firstName". So bleiben Texte und Java-Code getrennt.
        if (person.getFirstName() == null || person.getFirstName().isBlank()) {
            bindingResult.rejectValue("firstName", "required");
        }
        if (person.getAge() != null && person.getAge() < 0) {
            bindingResult.rejectValue("age", "negative");
        }

        // Bei Fehlern das Formular mit Hinweisen erneut anzeigen.
        // Das "person"-Objekt (mit den Eingaben) liegt dank @ModelAttribute schon im
        // Model; die Liste müssen wir aber neu hinzufügen.
        if (bindingResult.hasErrors()) {
            model.addAttribute("persons", personRepository.findAll());
            return "index";
        }

        // Leerzeichen am Anfang und Ende entfernen, z.B. "  Anna " -> "Anna"
        person.setFirstName(person.getFirstName().strip());
        personRepository.save(person);

        // Post/Redirect/Get-Muster: Statt direkt "index" zurückzugeben, sagen wir dem
        // Browser "lade bitte /index neu" (GET). Drückt man danach F5, wird nur die
        // Seite neu geladen – und nicht das Formular ein zweites Mal abgeschickt.
        return "redirect:/index";
    }
}
