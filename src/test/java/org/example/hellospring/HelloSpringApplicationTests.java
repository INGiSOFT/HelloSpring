package org.example.hellospring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Einfachster möglicher Test für eine Spring-Boot-Anwendung.
 * <p>
 * {@code @SpringBootTest} startet die komplette Anwendung (ohne Browser) für den Test.
 * Der Test {@link #contextLoads()} ist leer – er prüft nur, dass dieser Start klappt.
 * Schlägt er fehl, ist meist die Konfiguration fehlerhaft, z.&nbsp;B. weil Spring eine
 * benötigte Bean nicht findet.
 * <p>
 * Ausführen: in IntelliJ auf den grünen Pfeil neben der Klasse klicken oder
 * {@code ./mvnw test} auf der Kommandozeile.
 */
@SpringBootTest
class HelloSpringApplicationTests {

    @Test
    void contextLoads() {
    }

}
