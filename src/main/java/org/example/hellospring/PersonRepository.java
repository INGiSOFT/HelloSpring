package org.example.hellospring;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;

/**
 * Das <b>Repository</b> ist für das Speichern und Laden von Personen zuständig.
 * <p>
 * Der Controller muss so nicht wissen, <i>wie</i> und <i>wo</i> gespeichert wird.
 * In dieser Demo liegen die Personen einfach in einer Liste im Arbeitsspeicher –
 * <b>nach einem Neustart der Anwendung sind sie also wieder weg.</b>
 * Später könnte man diese Klasse durch eine Variante mit echter Datenbank ersetzen
 * (z.&nbsp;B. mit Spring Data JPA), ohne den Controller ändern zu müssen.
 * <p>
 * {@code @Repository} sagt Spring: "Erzeuge von dieser Klasse genau <i>ein</i> Objekt
 * und gib es allen, die es brauchen." Alle Benutzerinnen und Benutzer der Webseite
 * teilen sich also dieselbe Liste.
 */
@Repository
public class PersonRepository {

    /**
     * Die gespeicherten Personen.
     * <p>
     * Ein Webserver bearbeitet mehrere Anfragen gleichzeitig (in verschiedenen
     * <i>Threads</i>). Eine normale {@code ArrayList} könnte dabei durcheinander
     * geraten. Die {@link CopyOnWriteArrayList} ist dagegen <i>thread-sicher</i>.
     */
    private final List<Person> persons = new CopyOnWriteArrayList<>();

    /**
     * Speichert eine neue Person.
     *
     * @param person die zu speichernde Person
     */
    public void save(Person person) {
        persons.add(person);
    }

    /**
     * Liefert alle gespeicherten Personen in der Reihenfolge, in der sie erfasst wurden.
     * <p>
     * {@code List.copyOf(..)} gibt eine <i>unveränderliche Kopie</i> zurück. So kann
     * niemand von aussen versehentlich unsere interne Liste verändern – neue Personen
     * kommen nur über {@link #save(Person)} hinein.
     *
     * @return unveränderliche Liste aller Personen (nie {@code null}, evtl. leer)
     */
    public List<Person> findAll() {
        return List.copyOf(persons);
    }
}
