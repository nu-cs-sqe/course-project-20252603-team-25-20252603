package domain.locale;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocaleManagerTest {

    private static final Locale EN = new Locale("en");
    private static final Locale ES = new Locale("es");
    private static final Locale FR = new Locale("fr");

    private static void writeBundle(Path dir, String tag, String... keyValuePairs) throws IOException {
        StringBuilder body = new StringBuilder();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            body.append(keyValuePairs[i]).append('=').append(keyValuePairs[i + 1]).append('\n');
        }
        Files.writeString(dir.resolve("messages_" + tag + ".properties"), body.toString());
    }

    @Test
    void tc1_singleBundleDiscoveredAndDefaultActive(@TempDir Path dir) throws IOException {
        writeBundle(dir, "en", "app.title", "CATAN");

        LocaleManager mgr = new LocaleManager(dir);

        assertAll(
            () -> assertEquals(List.of(EN), mgr.getAvailableLocales()),
            () -> assertEquals(EN, mgr.getActiveLocale())
        );
    }

    @Test
    void tc2_twoBundlesDiscoveredInSortedOrder(@TempDir Path dir) throws IOException {
        writeBundle(dir, "es", "app.title", "CATAN");
        writeBundle(dir, "en", "app.title", "CATAN");

        LocaleManager mgr = new LocaleManager(dir);

        assertAll(
            () -> assertEquals(List.of(EN, ES), mgr.getAvailableLocales()),
            () -> assertEquals(EN, mgr.getActiveLocale())
        );
    }

    @Test
    void tc3_emptyBundleDirectoryRejected(@TempDir Path dir) {
        assertThrows(IllegalStateException.class, () -> new LocaleManager(dir));
    }

    @Test
    void tc4_nonMatchingFilesIgnored(@TempDir Path dir) throws IOException {
        writeBundle(dir, "en", "app.title", "CATAN");
        Files.writeString(dir.resolve("README.md"), "noise\n");
        Files.writeString(dir.resolve("messages.txt"), "noise\n");

        LocaleManager mgr = new LocaleManager(dir);

        assertEquals(List.of(EN), mgr.getAvailableLocales());
    }

    @Test
    void tc5_nullBundleDirectoryRejected() {
        assertThrows(IllegalArgumentException.class, () -> new LocaleManager(null));
    }

    @Test
    void tc6_availableLocalesIsUnmodifiable(@TempDir Path dir) throws IOException {
        writeBundle(dir, "en", "app.title", "CATAN");

        LocaleManager mgr = new LocaleManager(dir);

        assertThrows(UnsupportedOperationException.class,
            () -> mgr.getAvailableLocales().add(FR));
    }

    @Test
    void tc7_setActiveLocaleSwitchesActiveAndAffectsLookup(@TempDir Path dir) throws IOException {
        writeBundle(dir, "en", "setup.start", "Start Game");
        writeBundle(dir, "es", "setup.start", "Empezar partida");
        LocaleManager mgr = new LocaleManager(dir);

        mgr.setActiveLocale(ES);

        assertAll(
            () -> assertEquals(ES, mgr.getActiveLocale()),
            () -> assertEquals("Empezar partida", mgr.get("setup.start"))
        );
    }

    @Test
    void tc8_nullLocaleRejectedAndActiveUnchanged(@TempDir Path dir) throws IOException {
        writeBundle(dir, "en", "app.title", "CATAN");
        LocaleManager mgr = new LocaleManager(dir);

        assertThrows(IllegalArgumentException.class, () -> mgr.setActiveLocale(null));
        assertEquals(EN, mgr.getActiveLocale());
    }

    @Test
    void tc9_unknownLocaleRejectedAndActiveUnchanged(@TempDir Path dir) throws IOException {
        writeBundle(dir, "en", "app.title", "CATAN");
        LocaleManager mgr = new LocaleManager(dir);

        assertThrows(IllegalArgumentException.class, () -> mgr.setActiveLocale(FR));
        assertEquals(EN, mgr.getActiveLocale());
    }

    @Test
    void tc12_getReturnsValueFromActiveBundle(@TempDir Path dir) throws IOException {
        writeBundle(dir, "en", "app.title", "CATAN");
        LocaleManager mgr = new LocaleManager(dir);

        assertEquals("CATAN", mgr.get("app.title"));
    }

    @Test
    void tc14_nullKeyRejected(@TempDir Path dir) throws IOException {
        writeBundle(dir, "en", "app.title", "CATAN");
        LocaleManager mgr = new LocaleManager(dir);

        assertThrows(IllegalArgumentException.class, () -> mgr.get(null));
    }

    @Test
    void tc15_missingKeyThrowsMissingResource(@TempDir Path dir) throws IOException {
        writeBundle(dir, "en", "app.title", "CATAN");
        LocaleManager mgr = new LocaleManager(dir);

        assertThrows(MissingResourceException.class, () -> mgr.get("nope.key"));
    }

    @Test
    void tc16_getWithArgsSubstitutesPositionalArguments(@TempDir Path dir) throws IOException {
        writeBundle(dir, "en", "setup.player.name.prompt", "Player {0} name:");
        LocaleManager mgr = new LocaleManager(dir);

        assertEquals("Player 1 name:", mgr.get("setup.player.name.prompt", 1));
    }
}
