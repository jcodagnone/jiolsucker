/*
 * Creada el Apr 13, 2005
 */
package ar.com.leak.iolsucker.container.swing;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * Static convenience methods for GUIs which eliminate code duplication.
 * 
 * @author <a href="http://www.javapractices.com/">javapractices.com </a>
 */
public final class UiUtil {
    
    /** Creates the UiUtil. */
    private UiUtil() {
        // utility class
    }
    
    /**
     * @param aWindow window to center in the screen, and to show
     */
    public static void centerAndShow(final Window aWindow) {
        //note that the order here is important

        aWindow.pack();
        /*
         * If called from outside the event dispatch thread (as is the case upon
         * startup, in the launch thread), then in principle this code is not
         * thread-safe: once pack has been called, the component is realized,
         * and (most) further work on the component should take place in the
         * event-dispatch thread. In practice, it is exceedingly unlikely that
         * this will lead to an error, since invisible components cannot receive
         * events.
         */
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension window = aWindow.getSize();
        //ensure that no parts of aWindow will be off-screen
        if(window.height > screen.height) {
            window.height = screen.height;
        }
        if(window.width > screen.width) {
            window.width = screen.width;
        }
        int xCoord = (screen.width / 2 - window.width / 2);
        int yCoord = (screen.height / 2 - window.height / 2);
        aWindow.setLocation(xCoord, yCoord);

        aWindow.setVisible(true);
    }

    /**
     * A window is packed, centered with respect to a parent, and then shown.
     * <P>
     * This method is intended for dialogs only.
     * <P>
     * If centering with respect to a parent causes any part of the dialog to be
     * off screen, then the centering is overidden, such that all of the dialog
     * will always appear fully on screen, but it will still appear near the
     * parent.
     * 
     * @param aWindow
     *            must have non-null result for <code>aWindow.getParent</code>.
     */
    public static void centerOnParentAndShow(final Window aWindow) {
        aWindow.pack();

        Dimension parent = aWindow.getParent().getSize();
        Dimension window = aWindow.getSize();
        int xCoord = aWindow.getParent().getLocationOnScreen().x
                + (parent.width / 2 - window.width / 2);
        int yCoord = aWindow.getParent().getLocationOnScreen().y
                + (parent.height / 2 - window.height / 2);

        // Ensure that no part of aWindow will be off-screen
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int xOffScreenExcess = xCoord + window.width - screen.width;
        if(xOffScreenExcess > 0) {
            xCoord = xCoord - xOffScreenExcess;
        }
        if(xCoord < 0) {
            xCoord = 0;
        }
        int yOffScreenExcess = yCoord + window.height - screen.height;
        if(yOffScreenExcess > 0) {
            yCoord = yCoord - yOffScreenExcess;
        }
        if(yCoord < 0) {
            yCoord = 0;
        }

        aWindow.setLocation(xCoord, yCoord);
        aWindow.setVisible(true);
    }
}
