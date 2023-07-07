package stubAWT;

import java.awt.*;
import java.io.Serializable;

public abstract class Component implements ImageObserver, MenuContainer, Serializable {
    public Insets getInsets() {
        return null;
    }

    public Component getSize() {
        return null;
    }

    public Toolkit getToolkit() {
        return null;
    }

    public void setCursor(Cursor customCursor) {
    }
    // Implement methods with empty bodies or log statements
}