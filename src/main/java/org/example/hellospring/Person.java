package org.example.hellospring;

/**
 * Das <b>Model</b> (das "M" in MVC): eine Person mit Vorname und Alter.
 * <p>
 * Das ist eine ganz einfache Java-Klasse ohne Spring-Annotationen, ein sogenanntes
 * <i>POJO</i> (Plain Old Java Object) bzw. eine <i>JavaBean</i>.
 * <p>
 * <b>Wichtig:</b> Die Getter und Setter sind nötig, damit Spring und Thymeleaf mit
 * dem Objekt arbeiten können:
 * <ul>
 *     <li>Beim <b>Absenden des Formulars</b> erzeugt Spring ein neues {@code Person}-Objekt
 *         und füllt es über die Setter ({@code setFirstName(..)}, {@code setAge(..)}).
 *         Der Name des Eingabefelds im HTML ({@code firstName}) muss dazu zum Namen
 *         der Eigenschaft passen.</li>
 *     <li>Beim <b>Anzeigen</b> liest Thymeleaf die Werte über die Getter.
 *         Der Ausdruck {@code ${p.firstName}} im Template ruft also in Wahrheit
 *         {@code p.getFirstName()} auf.</li>
 * </ul>
 * Ausserdem braucht die Klasse einen Konstruktor ohne Parameter. Weil wir keinen
 * eigenen Konstruktor schreiben, erzeugt Java diesen automatisch.
 */
public class Person {

    /** Vorname der Person. Pflichtfeld – wird im {@link PersonController} geprüft. */
    private String firstName;

    /**
     * Alter in Jahren. Optional.
     * <p>
     * Wir verwenden {@code Integer} (Objekt) statt {@code int} (primitiver Typ), weil
     * {@code Integer} auch {@code null} sein kann. So können wir unterscheiden
     * zwischen "kein Alter angegeben" ({@code null}) und "Alter 0".
     */
    private Integer age;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
