package pixelDraw;

import ui.Gui;
import bus.drawer.Drawer;
import bus.transformation.Transformation;

/**
 *
 * @author dpf
 */
public class I {

	public final static int CANVAS_COLS = 161;
	public final static int CANVAS_ROWS = 81;
	public static final int PIXEL_SIZE = 10;

	public static final String APP_NAME = "Pixel Draw 2";

	public static void main(String[] args) {
		// Run UI
		new Gui(CANVAS_COLS, CANVAS_ROWS, PIXEL_SIZE, Drawer.getAll(), Transformation.getAll());
	}

}
