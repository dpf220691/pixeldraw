package ui;

import bus.CanvasController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;
import bus.Point;
import bus.shape.IShape;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author dpf
 */
class Canvas extends JPanel implements CanvasController {

	public static final Color BACKGROUND_COLOR = Color.WHITE;
	public static final Color GRID_COLOR = Color.GRAY;
	public static final Color HIGHLIGHT_COLOR = Color.RED;
	public static final Color SELECTION_COLOR = Color.BLUE;

	private List<IShape> shapes;

	private final Pixel matrix[][];
	private final int pixelSize;

	public Canvas(int height, int width, int pixelSize, Gui gui) {
		this.shapes = new ArrayList<>();
		this.pixelSize = pixelSize;
		this.matrix = new Pixel[height][width];
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				gbc.gridx = row;
				gbc.gridy = col;
				Point px = new Point(row, col);
				Pixel pixel = new Pixel(px, gui);
				matrix[row][col] = pixel;
				pixel.setBorder(new MatteBorder(1, 1, (col == width - 1 && row <= height - 1) ? 1 : 0, (row == height - 1 && col <= width - 1) ? 1 : 0, GRID_COLOR));
				add(pixel, gbc);
			}
		}
	}

	@Override
	public List<IShape> getShapes() {
		return shapes;
	}

	@Override
	public void refresh() {
		/*
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				matrix[row][col].paint(BACKGROUND_COLOR);
			}
		}
		*/
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++) {
				if (matrix[row][col].getBackground() != BACKGROUND_COLOR) {
					matrix[row][col].paint(BACKGROUND_COLOR);
				}
			}
		}
		
		for (IShape s : shapes) {
			for (Point p : s.compute()) {
				try {
					matrix[p.row][p.col].paint(s.getColor());
				} catch (IndexOutOfBoundsException e) {
				}
			}
		}
	}

	@Override
	public void selectPoint(Point p) {
		try{
			matrix[p.row][p.col].paint(SELECTION_COLOR);
		}catch(ArrayIndexOutOfBoundsException e){
			
		}
	}

	private class Pixel extends JPanel {

		private Color currentBackground;

		public Pixel(Point px, Gui gui) {
			setBackground(BACKGROUND_COLOR);

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					currentBackground = getBackground();
					setBackground(HIGHLIGHT_COLOR);
					gui.inspectPixel(px);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					setBackground(currentBackground);
				}

				@Override
				public void mouseClicked(MouseEvent e) {
					switch (e.getButton()) {
						case MouseEvent.BUTTON1:
							gui.leftClickPixel(px);
							break;
						case MouseEvent.BUTTON3:
							gui.rightClickPixel(px);
							break;
					}
				}
			});

		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(pixelSize, pixelSize);
		}

		private void paint(Color color) {
			setBackground(color);
			currentBackground = getBackground();
		}

	}

}
