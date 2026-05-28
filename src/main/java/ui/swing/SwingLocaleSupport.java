package ui.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

/**
 * Applies a logical font family that renders Latin and CJK glyphs on typical
 * desktop JDK installs.
 */
public final class SwingLocaleSupport {

    private static final Font UI_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);

    private SwingLocaleSupport() {
    }

    /**
     * Sets {@link Font#SANS_SERIF} on {@code root} and every descendant component.
     *
     * @param root dialog or frame to update
     */
    public static void applyLocaleFont(Container root) {
        root.setFont(UI_FONT);
        for (Component child : root.getComponents()) {
            if (child instanceof Container) {
                applyLocaleFont((Container) child);
            } else {
                child.setFont(UI_FONT);
            }
        }
    }
}
