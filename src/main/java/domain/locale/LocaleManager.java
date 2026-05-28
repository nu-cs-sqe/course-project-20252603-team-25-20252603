package domain.locale;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Discovers locale bundles at runtime and resolves localized strings. New
 * locales are added by dropping {@code messages_<lang>.properties} on the
 * classpath; no existing source needs to change.
 */
public final class LocaleManager {

    private static final String BUNDLE_PREFIX = "messages_";
    private static final String BUNDLE_SUFFIX = ".properties";
    private static final Pattern BUNDLE_PATTERN =
        Pattern.compile("messages_([A-Za-z]{2,3}(?:_[A-Za-z]{2})?)\\.properties");
    private static final String NO_BUNDLES_MESSAGE =
        "No messages_<lang>.properties bundles were found. Add at least one "
            + "bundle to the classpath (for example, src/main/resources/messages_en.properties).";

    private static volatile LocaleManager instance;

    private final TreeMap<Locale, Properties> bundlesByLocale;
    private Locale activeLocale;

    /**
     * Loads every {@code messages_<lang>.properties} file in {@code bundleDir}
     * and sets the active locale to the lexicographically first one. Package
     * private so unit tests can inject an in-memory directory; production code
     * uses {@link #getInstance()} which discovers the directory from the
     * classpath.
     */
    LocaleManager(Path bundleDir) {
        Objects.requireNonNull(bundleDir, "bundleDir must not be null");
        this.bundlesByLocale = loadBundlesFrom(bundleDir);
        if (bundlesByLocale.isEmpty()) {
            throw new IllegalStateException(NO_BUNDLES_MESSAGE);
        }
        this.activeLocale = bundlesByLocale.firstKey();
    }

    /**
     * Returns the process-wide manager, lazily initialized from the first
     * classpath root that contains a {@code messages_*.properties} file.
     */
    public static LocaleManager getInstance() {
        LocaleManager local = instance;
        if (local == null) {
            synchronized (LocaleManager.class) {
                local = instance;
                if (local == null) {
                    instance = new LocaleManager(discoverDefaultBundleDir());
                    local = instance;
                }
            }
        }
        return local;
    }

    /**
     * Test hook: forget the cached singleton so the next
     * {@link #getInstance()} call re-discovers bundles. Not for production use.
     */
    public static void resetForTesting() {
        synchronized (LocaleManager.class) {
            instance = null;
        }
    }

    public List<Locale> getAvailableLocales() {
        return Collections.unmodifiableList(new ArrayList<>(bundlesByLocale.keySet()));
    }

    public Locale getActiveLocale() {
        return activeLocale;
    }

    public void setActiveLocale(Locale locale) {
        Objects.requireNonNull(locale, "locale must not be null");
        if (!bundlesByLocale.containsKey(locale)) {
            throw new IllegalArgumentException(
                "locale not available: " + locale.toLanguageTag());
        }
        this.activeLocale = locale;
    }

    public String get(String key) {
        Objects.requireNonNull(key, "key must not be null");
        String value = bundlesByLocale.get(activeLocale).getProperty(key);
        if (value == null) {
            throw new MissingResourceException(
                "Missing key '" + key + "' in locale " + activeLocale.toLanguageTag(),
                BUNDLE_PREFIX + activeLocale.toLanguageTag() + BUNDLE_SUFFIX,
                key);
        }
        return value;
    }

    public String get(String key, Object... args) {
        String pattern = get(key);
        if (args == null || args.length == 0) {
            return pattern;
        }
        return MessageFormat.format(pattern, args);
    }

    private static TreeMap<Locale, Properties> loadBundlesFrom(Path bundleDir) {
        TreeMap<Locale, Properties> result =
            new TreeMap<>(Comparator.comparing(Locale::toLanguageTag));
        if (!Files.isDirectory(bundleDir)) {
            return result;
        }
        try (DirectoryStream<Path> stream =
                 Files.newDirectoryStream(bundleDir, BUNDLE_PREFIX + "*" + BUNDLE_SUFFIX)) {
            for (Path file : stream) {
                Matcher matcher = BUNDLE_PATTERN.matcher(file.getFileName().toString());
                if (!matcher.matches()) {
                    continue;
                }
                Locale locale = parseLocaleTag(matcher.group(1));
                Properties props = new Properties();
                try (InputStream in = Files.newInputStream(file)) {
                    props.load(in);
                }
                result.put(locale, props);
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                "Failed to read locale bundles from " + bundleDir, e);
        }
        return result;
    }

    private static Locale parseLocaleTag(String rawTag) {
        String[] parts = rawTag.split("_");
        if (parts.length == 1) {
            return new Locale(parts[0].toLowerCase(Locale.ROOT));
        }
        return new Locale(parts[0].toLowerCase(Locale.ROOT), parts[1].toUpperCase(Locale.ROOT));
    }

    private static Path discoverDefaultBundleDir() {
        try {
            ClassLoader loader = LocaleManager.class.getClassLoader();
            Enumeration<URL> roots = loader.getResources("");
            while (roots.hasMoreElements()) {
                URL root = roots.nextElement();
                if (!"file".equals(root.getProtocol())) {
                    continue;
                }
                Path dir = Paths.get(root.toURI());
                if (containsAnyBundle(dir)) {
                    return dir;
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(
                "Failed to scan classpath for locale bundles", e);
        }
        throw new IllegalStateException(NO_BUNDLES_MESSAGE);
    }

    private static boolean containsAnyBundle(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            return false;
        }
        try (DirectoryStream<Path> stream =
                 Files.newDirectoryStream(dir, BUNDLE_PREFIX + "*" + BUNDLE_SUFFIX)) {
            return stream.iterator().hasNext();
        }
    }
}
