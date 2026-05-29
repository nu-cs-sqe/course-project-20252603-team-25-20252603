package integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.locale.LocaleManager;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Integration-style coverage for the locale feature: real bundles from
 * {@code src/main/resources} on the test classpath, discovered through
 * {@link LocaleManager#getInstance()}.
 */
@Tag("integration")
class LocaleClasspathIntegrationTest {

    private static final Locale EN = new Locale("en");
    private static final Locale ES = new Locale("es");

    @AfterEach
    void tearDown() {
        LocaleManager.resetForTesting();
    }

    @Test
    void it_providesEnglishAndSpanishFromClasspath() {
        LocaleManager manager = LocaleManager.getInstance();

        assertTrue(manager.getAvailableLocales().contains(EN));
        assertTrue(manager.getAvailableLocales().contains(ES));
        assertEquals("Start Game", manager.get("setup.start"));
    }

    @Test
    void it_switchingLocaleChangesResolvedStrings() {
        LocaleManager manager = LocaleManager.getInstance();
        manager.setActiveLocale(ES);

        assertEquals("Empezar partida", manager.get("setup.start"));
    }
}
