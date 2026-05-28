package ui.swing;

import domain.locale.LocaleManager;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Locale;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * Modal dialog that lists every locale discovered by {@link LocaleManager}
 * as a button labeled in that locale's own language. Picking a button
 * invokes the supplied callback and closes the dialog.
 *
 * <p>Pure view: holds no game state and never reads from {@code LocaleManager}
 * beyond the available-locales list. The caller decides what to do with the
 * selection (typically: call {@link LocaleManager#setActiveLocale(Locale)}).
 */
public final class LocaleSelectionFrame extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final int BUTTON_GAP = 8;

    /**
     * Builds the dialog. Construct on the Swing event-dispatch thread.
     *
     * @param owner          parent frame, may be {@code null} for a top-level dialog
     * @param localeManager  manager that exposes the available locales
     * @param onSelected     invoked exactly once with the chosen locale before the dialog closes
     */
    public LocaleSelectionFrame(Frame owner,
                                LocaleManager localeManager,
                                Consumer<Locale> onSelected) {
        super(owner, true);
        if (localeManager == null) {
            throw new IllegalArgumentException("localeManager must not be null");
        }
        if (onSelected == null) {
            throw new IllegalArgumentException("onSelected must not be null");
        }
        setTitle(localeManager.get("locale.select.title"));
        setLayout(new GridLayout(0, 1, BUTTON_GAP, BUTTON_GAP));
        for (Locale locale : localeManager.getAvailableLocales()) {
            JButton button = new JButton(locale.getDisplayName(locale));
            button.addActionListener(event -> {
                onSelected.accept(locale);
                dispose();
            });
            add(button);
        }
        SwingLocaleSupport.applyLocaleFont(this);
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}
