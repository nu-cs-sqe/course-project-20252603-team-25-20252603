package integration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.locale.LocaleManager;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Integration coverage for locale selection: real bundles from
 * {@code src/main/resources} on the test classpath, discovered through
 * {@link LocaleManager#getInstance()}.
 */
@Tag("integration")
class LocaleClasspathIntegrationTest {

    private static final Locale EN = new Locale("en");
    private static final Locale ES = new Locale("es");
    private static final Locale ZH = new Locale("zh");
    private static final Locale FR = new Locale("fr");
    private static final String ZH_SETUP_START = "开始游戏";

    @AfterEach
    void tearDown() {
        LocaleManager.resetForTesting();
    }

    @Test
    void it_discoversEnglishSpanishAndMandarinFromClasspath() {
        LocaleManager manager = LocaleManager.getInstance();

        assertAll(
            () -> assertTrue(manager.getAvailableLocales().contains(EN)),
            () -> assertTrue(manager.getAvailableLocales().contains(ES)),
            () -> assertTrue(manager.getAvailableLocales().contains(ZH)),
            () -> assertEquals("Start Game", manager.get("setup.start"))
        );
    }

    @Test
    void it_switchingToSpanishChangesResolvedStrings() {
        LocaleManager manager = LocaleManager.getInstance();
        manager.setActiveLocale(ES);

        assertEquals("Empezar partida", manager.get("setup.start"));
    }

    @Test
    void it_switchingToMandarinChangesResolvedStrings() {
        LocaleManager manager = LocaleManager.getInstance();
        manager.setActiveLocale(ZH);

        assertEquals(ZH_SETUP_START, manager.get("setup.start"));
    }

    @Test
    void it_rejectsUnavailableLocale() {
        LocaleManager manager = LocaleManager.getInstance();

        assertThrows(IllegalArgumentException.class, () -> manager.setActiveLocale(FR));
        assertEquals(EN, manager.getActiveLocale());
    }

    @Test
    void it_rejectsNullLocale() {
        LocaleManager manager = LocaleManager.getInstance();

        assertThrows(NullPointerException.class, () -> manager.setActiveLocale(null));
        assertEquals(EN, manager.getActiveLocale());
    }
}
