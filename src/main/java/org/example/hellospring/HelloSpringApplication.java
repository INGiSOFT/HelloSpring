package org.example.hellospring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Startpunkt der Anwendung.
 * <p>
 * Die Annotation {@code @SpringBootApplication} fasst drei Dinge zusammen:
 * <ul>
 *     <li><b>Konfiguration</b> – diese Klasse darf Spring-Einstellungen enthalten.</li>
 *     <li><b>Auto-Konfiguration</b> – Spring Boot schaut, welche Bibliotheken im
 *         {@code pom.xml} stehen, und richtet sie automatisch ein (z.&nbsp;B. einen
 *         eingebauten Tomcat-Webserver und die Template-Engine Thymeleaf).</li>
 *     <li><b>Component-Scan</b> – Spring durchsucht dieses Package
 *         ({@code org.example.hellospring}) und alle Unter-Packages nach Klassen mit
 *         Annotationen wie {@code @Controller} oder {@code @Repository} und erzeugt
 *         davon automatisch je ein Objekt (eine sogenannte <i>Bean</i>).</li>
 * </ul>
 * Deshalb müssen wir {@link PersonController} und {@link PersonRepository} nirgends
 * selbst mit {@code new} erzeugen – das erledigt Spring für uns.
 */
@SpringBootApplication
public class HelloSpringApplication {

    /**
     * Ganz normale Java-{@code main}-Methode. Sie startet Spring, welches dann den
     * Webserver hochfährt. Danach ist die Seite unter
     * <a href="http://localhost:8080">http://localhost:8080</a> erreichbar.
     *
     * @param args Kommandozeilen-Argumente (werden an Spring weitergereicht,
     *             z.&nbsp;B. {@code --server.port=9090})
     */
    public static void main(String[] args) {
        SpringApplication.run(HelloSpringApplication.class, args);
    }

}
