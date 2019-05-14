package bus;

import bus.drawer.Drawer;
import bus.transformation.Transformation;
import java.io.File;
import java.util.List;
import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Paths;
import pixelDraw.I;
import ui.Gui;

/**
 *
 * @author dpf
 */
public class Animation extends Thread {

	private static final String WAIT = "wait";
	private static final String CLEAR = "clear";
	private static final String COLOR = "color";
	private static final String REMOVE = "remove";

	private static final String SEPARATOR = " ";
	private static final String COMMENT = "#";

	private static final Color DEFAULT_COLOR = Color.BLACK;

	private static final String ERROR = "ERROR";

	CanvasController cc;
	Gui parent;
	
	Color color;

	List<Drawer> drawers;
	List<Transformation> transformations;

	File animationFile;
	
	int lineCounter;
	int msgCounter;
	
	public Animation(Gui parent, CanvasController cc, List<Drawer> drawers, List<Transformation> transformations, File animationFile) {
		this.parent = parent;
		this.cc = cc;
		this.drawers = drawers;
		this.transformations = transformations;
		this.animationFile = animationFile;
		this.color = DEFAULT_COLOR;
	}	

	@Override
	public synchronized void start() {
		super.start(); //To change body of generated methods, choose Tools | Templates.
		lineCounter = 0;
		msgCounter = 1;
	}
	
	

	@Override
	public void run() {
		while(true){
			try {
				String line = Files.readAllLines(Paths.get(animationFile.getAbsolutePath())).get(lineCounter++);
				String msg = computeLine(line);
				if(msg != null){
					parent.log("Line "+ (msgCounter++) + " >>> " + msg);
				}
			} catch (Exception ex) {
				cc.refresh();
				break;
			}
		}
	}

	private String computeLine(String line) {
		String output = "";
		if (line.startsWith(COMMENT) || line.equals("")) {
			return null;
		}
		String[] instruction = line.split(SEPARATOR);
		boolean fbasic = true;
		instruction[0] = instruction[0].toLowerCase();
		switch (instruction[0]) {
			case WAIT:
				cc.refresh();
				int timeout = Integer.parseInt(instruction[1]);
				try {
					this.sleep(timeout);
				} catch (InterruptedException ex) {
					return ERROR;
				}
				return "Wait " + timeout + " ms";
			case CLEAR:
				cc.getShapes().clear();
				return "Canvas cleared";
			case COLOR:
				int r = Integer.parseInt(instruction[1]);
				int g = Integer.parseInt(instruction[2]);
				int b = Integer.parseInt(instruction[3]);
				this.color = new Color(r, g, b);
				return "Selected color  RGB[" +r+", " + g + ", " + b + "]";
			case REMOVE:
				int index = Integer.parseInt(instruction[1]);
				try{
					cc.getShapes().remove(index-1);
				}catch(IndexOutOfBoundsException e){
					return ERROR;
				}
				return "Removed shape #" + index;
			default:
				fbasic = false;
		}
		if (!fbasic) {
			Tool tool = null;
			Drawer drawer = null;
			Transformation transformation = null;
			int i = 1;
			for (Drawer d : drawers) {
				if (d.getName().toLowerCase().equals(instruction[0])) {
					drawer = d;
					break;
				}
			}
			for (Transformation t : transformations) {
				if (t.getName().toLowerCase().equals(instruction[0])) {
					transformation = t;
					break;
				}
			}
			if (drawer != null) {
				tool = drawer;
			} else if (transformation != null) {
				tool = transformation;
			} else {
				return ERROR;
			}
			tool.onSelect();
			while (tool.hasNextArg()) {
				try {
					switch (tool.nextArg()) {
						case POINT:
							tool.input(Point.fromCenter0_0(new Point(Integer.parseInt(instruction[i++]), Integer.parseInt(instruction[i++])), I.CANVAS_COLS, I.CANVAS_ROWS));
							break;
						case VALUE:
							tool.input(Double.parseDouble(instruction[i++]));
							break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return ERROR;
				}
			}
			output = tool.getNextMessage();

			if (drawer != null) {
				cc.getShapes().add(drawer.computeShape(color));
			} else if (transformation != null) {
				transformation.compute(cc.getShapes());
			} else {
				return ERROR;
			}

		}

		return output;
	}

}
