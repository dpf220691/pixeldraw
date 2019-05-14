package ui;

import bus.Animation;
import bus.CanvasController;
import bus.Point;
import bus.Tool.Arg;
import bus.drawer.Drawer;
import bus.shape.IShape;
import bus.transformation.Transformation;
import pixelDraw.I;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author dpf
 */
public class Gui extends javax.swing.JFrame {

	private final static String COLORCHOOSER_TEXT = "Select draw color";
	private final static String SELECTED_TEXT = "Selected";
	private final static String SELECT_TOOL_TEXT = "Select a tool";
	private final static String SELECT_ANIMATION_TEXT = "No animation loaded";

	private Drawer drawer;
	private Transformation transformation;
	
	private Animation animation;

	private List<Drawer> drawers;
	private List<Transformation> transformations;

	private Color DEFAULT_COLOR = Color.BLACK;
	private Color currentColor;

	private CanvasController cc;
	private int height, width;

	/**
	 * Creates a graphic user interface
	 *
	 */
	public Gui(int height, int width, int pixelSize, List<Drawer> drawers, List<Transformation> transformations) {
		initComponents();
		// Setting up variables
		this.currentColor = DEFAULT_COLOR;
		this.colorPanel.setBackground(currentColor);
		this.height = height;
		this.width = width;
		this.drawers = drawers;
		this.transformations = transformations;

		JMenuItem menuItem;
		// Adding tools
		for (Drawer d : drawers) {
			menuItem = new JMenuItem(d.getName());
			menuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					transformation = null;
					drawer = d;
					log(drawer.getName() + " " + SELECTED_TEXT);
					drawer.onSelect();
					useTool();
				}
			});
			menuDraw.add(menuItem);
		}
		// Adding transformations
		for (Transformation t : transformations) {
			menuItem = new JMenuItem(t.getName());
			menuItem.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					drawer = null;
					transformation = t;
					log(transformation.getName() + " " + SELECTED_TEXT);
					transformation.onSelect();
					useTool();
				}
			});
			menuTransform.add(menuItem);
		}

		// Modifying focus on TextFields, setting up layout and adding the canvas
		xTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(final FocusEvent pE) {
			}

			@Override
			public void focusGained(final FocusEvent pE) {
				xTextField.selectAll();
			}
		});
		yTextField.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(final FocusEvent pE) {
			}

			@Override
			public void focusGained(final FocusEvent pE) {
				yTextField.selectAll();
			}
		});
		backgroundPanel.setLayout(new BorderLayout());
		Canvas canvas = new Canvas(height, width, pixelSize, this);
		cc = canvas;
		backgroundPanel.add(canvas);
		clearLog();

		this.setFocusTraversalPolicy(new FocusTraversalPolicy() {
			@Override
			public Component getComponentAfter(Container aContainer, Component aComponent) {
				if (aComponent == xTextField) {
					return yTextField;
				} else if (aComponent == yTextField) {
					return addButton;
				} else {
					return xTextField;
				}
			}

			@Override
			public Component getComponentBefore(Container aContainer, Component aComponent) {
				return xTextField;
			}

			@Override
			public Component getFirstComponent(Container aContainer) {
				return addButton;
			}

			@Override
			public Component getLastComponent(Container aContainer) {
				return addButton;
			}

			@Override
			public Component getDefaultComponent(Container aContainer) {
				return addButton;
			}
		});

		this.pack();
		// Centering the window in the screen and title
		this.setTitle(I.APP_NAME);
		this.setLocationRelativeTo(null);
		// Showing the frame
		this.setVisible(true);
	}

	private void useTool() {
		if (drawer != null) {
			if (drawer.hasNextArg()) {
				log(drawer.getNextMessage());
				if (drawer.nextArg() == Arg.VALUE) {
					Double d = null;
					String input;
					do {
						input = JOptionPane.showInputDialog(this, drawer.getNextMessage(), "", JOptionPane.QUESTION_MESSAGE);
						try {
							d = Double.parseDouble(input);
						} catch (Exception e) {
						}
					} while (d == null);
					drawer.input(d);
					useTool();
				}
			} else {
				IShape shape = drawer.computeShape(currentColor);
				log(drawer.getNextMessage());
				cc.getShapes().add(shape);
				cc.refresh();
				drawer.onSelect();
				useTool();
			}
		} else if (transformation != null) {
			if (transformation.hasNextArg()) {
				log(transformation.getNextMessage());
				if (transformation.nextArg() == Arg.VALUE) {
					Double d = null;
					String input;
					do {
						input = JOptionPane.showInputDialog(this, transformation.getNextMessage(), "", JOptionPane.QUESTION_MESSAGE);
						try {
							d = Double.parseDouble(input);
						} catch (Exception e) {
						}
					} while (d == null);
					transformation.input(d);
					useTool();
				}
			} else {
				transformation.compute(cc.getShapes());
				cc.refresh();
				log(transformation.getNextMessage());
				transformation.onSelect();
				transformation = null;
			}
		}
	}

	private void usePoint(Point px) {
		if (drawer != null) {
			if (drawer.hasNextArg() && drawer.nextArg() == Arg.POINT) {
				drawer.input(px);
				log(px.toStringCenter0_0(height, width));
				cc.selectPoint(px);
				useTool();
			}
		} else if (transformation != null) {
			if (transformation.hasNextArg() && transformation.nextArg() == Arg.POINT) {
				transformation.input(px);
				log(px.toStringCenter0_0(height, width));
				cc.selectPoint(px);
				useTool();
			}
		} else {
			log(SELECT_TOOL_TEXT);
		}
	}

	public void leftClickPixel(Point px) {
		usePoint(px);
	}

	public void rightClickPixel(Point px) {
		// TODO
	}

	public void inspectPixel(Point px) {
		px = Point.toCenter0_0(px, height, width);
		xTextField.setText("" + px.row);
		yTextField.setText("" + px.col);
	}

	private void clearLog() {
		int s = 5;
		for (int i = 0; i < s; i++) {
			log("");
		}
	}

	public void log(String text) {
		logTextArea.setText(logTextArea.getText() + "\n" + text);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backgroundPanel = new javax.swing.JPanel();
        logTextPanel = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        downPanel = new javax.swing.JPanel();
        xLabel = new javax.swing.JLabel();
        xTextField = new javax.swing.JTextField();
        yLabel = new javax.swing.JLabel();
        yTextField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        colorPanel = new javax.swing.JTextField();
        menuBar = new javax.swing.JMenuBar();
        menuEdit = new javax.swing.JMenu();
        editChangeColor = new javax.swing.JMenuItem();
        editClear = new javax.swing.JMenuItem();
        menuDraw = new javax.swing.JMenu();
        menuTransform = new javax.swing.JMenu();
        menuAnimation = new javax.swing.JMenu();
        animationLoad = new javax.swing.JMenuItem();
        animationRun = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.PAGE_AXIS));

        javax.swing.GroupLayout backgroundPanelLayout = new javax.swing.GroupLayout(backgroundPanel);
        backgroundPanel.setLayout(backgroundPanelLayout);
        backgroundPanelLayout.setHorizontalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        backgroundPanelLayout.setVerticalGroup(
            backgroundPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1, Short.MAX_VALUE)
        );

        getContentPane().add(backgroundPanel);

        logTextArea.setEditable(false);
        logTextArea.setColumns(20);
        logTextArea.setLineWrap(true);
        logTextArea.setRows(5);
        logTextPanel.setViewportView(logTextArea);

        getContentPane().add(logTextPanel);

        xLabel.setText("x:");

        yLabel.setText("y:");

        addButton.setText("add");
        addButton.setPreferredSize(new java.awt.Dimension(60, 20));
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        colorPanel.setEditable(false);
        colorPanel.setPreferredSize(new java.awt.Dimension(15, 15));

        javax.swing.GroupLayout downPanelLayout = new javax.swing.GroupLayout(downPanel);
        downPanel.setLayout(downPanelLayout);
        downPanelLayout.setHorizontalGroup(
            downPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(downPanelLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(xLabel)
                .addGap(5, 5, 5)
                .addComponent(xTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(yLabel)
                .addGap(5, 5, 5)
                .addComponent(yTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 182, Short.MAX_VALUE)
                .addComponent(colorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );
        downPanelLayout.setVerticalGroup(
            downPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, downPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(downPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xLabel)
                    .addComponent(yLabel)
                    .addComponent(yTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(downPanel);

        menuEdit.setText("Edit");

        editChangeColor.setText("Change color");
        editChangeColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editChangeColorActionPerformed(evt);
            }
        });
        menuEdit.add(editChangeColor);

        editClear.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        editClear.setText("Clear");
        editClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editClearActionPerformed(evt);
            }
        });
        menuEdit.add(editClear);

        menuBar.add(menuEdit);

        menuDraw.setText("Draw");
        menuBar.add(menuDraw);

        menuTransform.setText("Transform");
        menuBar.add(menuTransform);

        menuAnimation.setText("Animation");

        animationLoad.setText("Load");
        animationLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                animationLoadActionPerformed(evt);
            }
        });
        menuAnimation.add(animationLoad);

        animationRun.setText("Run");
        animationRun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                animationRunActionPerformed(evt);
            }
        });
        menuAnimation.add(animationRun);

        menuBar.add(menuAnimation);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editClearActionPerformed
		cc.getShapes().clear();
		cc.refresh();
		clearLog();
    }//GEN-LAST:event_editClearActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
		Point px = validateTF();
		if (px != null) {
			usePoint(px);
		}
    }//GEN-LAST:event_addButtonActionPerformed

    private void editChangeColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editChangeColorActionPerformed
		// This way transparency is removed
		Color c = JColorChooser.showDialog(null, COLORCHOOSER_TEXT, currentColor);
		currentColor = new Color(c.getRed(), c.getGreen(), c.getBlue());
		colorPanel.setBackground(currentColor);
		log("Selected color RGB = [" + currentColor.getRed() + ", " + currentColor.getGreen() + ", " + currentColor.getBlue() + "]");
    }//GEN-LAST:event_editChangeColorActionPerformed

    private void animationLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_animationLoadActionPerformed
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnValue = jfc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			animation = new Animation(this, cc, drawers, transformations, jfc.getSelectedFile());
			log(jfc.getSelectedFile().getAbsolutePath()+" Loaded");
		}
    }//GEN-LAST:event_animationLoadActionPerformed

    private void animationRunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_animationRunActionPerformed
		if(animation != null){
			animation.start();
			animation = null;
		}else{
			log(SELECT_ANIMATION_TEXT);
		}
    }//GEN-LAST:event_animationRunActionPerformed

	private Point validateTF() {
		String xtf = xTextField.getText();
		String ytf = yTextField.getText();
		int x, y;
		try {
			x = Integer.parseInt(xtf);
			y = Integer.parseInt(ytf);
		} catch (NumberFormatException e) {
			return null;
		}
		Point px = Point.fromCenter0_0(new Point(x, y), height, width);
		if (px.col > width || px.row > height) {
			return null;
		}
		return px;
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JMenuItem animationLoad;
    private javax.swing.JMenuItem animationRun;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JTextField colorPanel;
    private javax.swing.JPanel downPanel;
    private javax.swing.JMenuItem editChangeColor;
    private javax.swing.JMenuItem editClear;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JScrollPane logTextPanel;
    private javax.swing.JMenu menuAnimation;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuDraw;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuTransform;
    private javax.swing.JLabel xLabel;
    private javax.swing.JTextField xTextField;
    private javax.swing.JLabel yLabel;
    private javax.swing.JTextField yTextField;
    // End of variables declaration//GEN-END:variables

}
