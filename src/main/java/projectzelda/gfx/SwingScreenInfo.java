package projectzelda.gfx;

import projectzelda.*;
import projectzelda.engine.*;
import java.awt.*;

public class SwingScreenInfo implements ScreenInfo {
    private double width;
    private double height;

    public SwingScreenInfo() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.getWidth();
        height = screenSize.getHeight();
    }

    public int getWidth() {
        return (int)width;
    }

    public int getHeight() {
        return (int)height;
    }
}
