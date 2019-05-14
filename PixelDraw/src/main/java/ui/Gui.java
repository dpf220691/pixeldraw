package ui;

import pixeldraw.Coords;
import pixeldraw.I;
import tools.ITool;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

/**
 *
 * @author dpf
 */
public class Gui extends javax.swing.JFrame 
{
    private final static String COLORCHOOSER_TEXT = "Select draw color";
    private final static String INPUT_TEXT = "input";
    private final static String OUTPUT_TEXT = "output"; 
    private final static String REMOVED_TEXT = "removed";
    
    public static final Color BACKGROUND_COLOR = Color.WHITE; 
    public static final Color GRID_COLOR = Color.GRAY;
    public static final Color HIGHLIGHT_COLOR = Color.RED;
    public static final Color SELECTION_COLOR = Color.BLUE;

    private List<ITool> tools;
    private ITool activeTool;
    private List<Coords> inputPixels;
    
    private Color DEFAULT_COLOR = Color.BLACK;
    private Color currentColor;
    
    private Canvas canvas;
    private int rows, cols, pixelSize;

    /**
     * Creates a graphic user interface
     *
     * @param tools
     */
    public Gui(int rows, int cols, int pixelSize, List<ITool> tools) 
    {
        initComponents();
        // Setting up variables
        this.currentColor = DEFAULT_COLOR;
        this.colorPanel.setBackground(currentColor);
        this.rows = rows;
        this.cols = cols;
        this.pixelSize = pixelSize;
        this.inputPixels = new ArrayList<>();
        // Adding the tools
        this.tools = tools;
        JMenuItem toolItem;
        for (ITool t : this.tools) 
        {
            toolItem = new JMenuItem(t.getName());
            toolItem.addActionListener(new java.awt.event.ActionListener() 
            {
                public void actionPerformed(java.awt.event.ActionEvent evt) 
                {
                    activeTool = t;
                    inputPixels.clear();
                    log(t.getName());
                }
            });
            menuTools.add(toolItem);
        }
        // Modifying focus on TextFields, setting up layout and adding the canvas
        xTextField.addFocusListener(new FocusListener() {
            @Override public void focusLost(final FocusEvent pE) {}
            @Override public void focusGained(final FocusEvent pE) {
                xTextField.selectAll();
            }
        });
        yTextField.addFocusListener(new FocusListener() {
            @Override public void focusLost(final FocusEvent pE) {}
            @Override public void focusGained(final FocusEvent pE) {
                yTextField.selectAll();
            }
        });
        backgroundPanel.setLayout(new BorderLayout());
        canvas = new Canvas(rows, cols, this);
        backgroundPanel.add(canvas);
        clearLog();
        
        
        this.setFocusTraversalPolicy(new FocusTraversalPolicy() {
            @Override
            public Component getComponentAfter(Container aContainer, Component aComponent) {
                if(aComponent==xTextField){
                    return yTextField;
                }else if(aComponent==yTextField){
                    return addButton;
                }else{
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

    public void leftClickPixel(Coords px) 
    {
        if (activeTool != null) 
        {  
            // Selecting the pixel
            inputPixels.add(px);
            canvas.paintPixel(px, SELECTION_COLOR);
            log("[" +INPUT_TEXT+ "] " + px.toStringCenter0_0(rows, cols));
            // Using the tool if enough pixels selected
            if (activeTool.argsCount() == inputPixels.size()) 
            {
                // Get outputPixels
                List<Coords> outputPixels = activeTool.outputPixels(inputPixels);
                // Erase inputPixels
                for(Coords ipx: inputPixels)
                {
                    canvas.paintPixel(ipx, BACKGROUND_COLOR);
                }
                // Draw outputPixels
                String logging = "[" + OUTPUT_TEXT + "] ";
                for(Coords ipx: outputPixels)
                {
                    //canvas.paintPixel(ipx, I.DRAW_COLOR);
                    canvas.paintPixel(ipx, currentColor);
                    logging += " " + ipx.toStringCenter0_0(rows, cols) + ",";
                }
                log(logging.substring(0, logging.length()-1));
                // Clear inputPixels
                inputPixels.clear();
            }   
        }
    }

    public void rightClickPixel(Coords px)
    {
        inputPixels.remove(px);
        canvas.paintPixel(px, BACKGROUND_COLOR);
        log("[" +REMOVED_TEXT+ "] " + px.toStringCenter0_0(rows, cols));
    }
    
    public void inspectPixel(Coords px) 
    {
        px = Coords.toCenter0_0(px, rows, cols);
        xTextField.setText("" + px.col);
        yTextField.setText("" + px.row);
    }

    private void clearLog(){
        int s = 5;
        for (int i = 0; i<s; i++){
            log("");
        }
    }
    
    private void log(String text)
    {
        logTextArea.setText(logTextArea.getText() + "\n" + text);
    }
    
    private class Canvas extends JPanel 
    {
        
        private final Pixel matrix[][];

        public Canvas(int rows, int cols, Gui gui) 
        {
            this.matrix = new Pixel[rows][cols];
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints(); 
            for (int row = 0; row < rows; row++) 
                for (int col = 0; col < cols; col++)           
                {
                    gbc.gridx = row;
                    gbc.gridy = col;
                    Coords px = new Coords(row, col);
                    Pixel pixel = new Pixel(px, gui);
                    matrix[row][col] = pixel;
                    pixel.setBorder(new MatteBorder(1, 1, (col == cols-1 && row <= rows-1)?1:0, (row == rows-1 && col <= cols-1)?1:0, GRID_COLOR));
                    add(pixel, gbc);
                }
        }
        
        public void paintPixel(Coords px, Color c )
        {
            matrix[px.row][px.col].paint(c);
        }

        private class Pixel extends JPanel
        {
            private Color currentBackground;

            public Pixel(Coords px, Gui gui) 
            {       
                setBackground(BACKGROUND_COLOR);
                
                addMouseListener(new MouseAdapter()
                {
                    @Override
                    public void mouseEntered(MouseEvent e) 
                    {
                        currentBackground = getBackground();
                        setBackground(HIGHLIGHT_COLOR);
                        gui.inspectPixel(px);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) 
                    {
                        setBackground(currentBackground);
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) 
                    {
                        switch(e.getButton())
                        {
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
            public Dimension getPreferredSize() 
            {
                return new Dimension(pixelSize, pixelSize);
            }

            private void paint(Color color)
            {
                setBackground(color);
                currentBackground = getBackground();
            }
        }
        
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
        menuTools = new javax.swing.JMenu();

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

        menuTools.setText("Tools");
        menuBar.add(menuTools);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editClearActionPerformed
        for (int row = 0; row < rows; row++) 
            for (int col = 0; col < cols; col++)           
            {
                canvas.paintPixel(new Coords(row, col), BACKGROUND_COLOR);
            }
        clearLog();  
    }//GEN-LAST:event_editClearActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        Coords px = validateTF();
        if(px != null)
        {
            leftClickPixel(px);
        }   
    }//GEN-LAST:event_addButtonActionPerformed

    private void editChangeColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editChangeColorActionPerformed
        // This way transparency is removed
        Color c = JColorChooser.showDialog(null, COLORCHOOSER_TEXT, currentColor);
        currentColor = new Color(c.getRed(), c.getGreen(), c.getBlue());
        colorPanel.setBackground(currentColor);
        log("Selected color RGB = ["+ currentColor.getRed()+", "+currentColor.getGreen()+", "+currentColor.getBlue()+"]");
    }//GEN-LAST:event_editChangeColorActionPerformed

    private Coords validateTF()
    {
        String xtf = xTextField.getText();
        String ytf = yTextField.getText();
        int x,y;
        try
        {
            x = Integer.parseInt(xtf);
            y = Integer.parseInt(ytf);
        }
        catch(NumberFormatException e)
        {
            return null;
        }
        Coords px = Coords.fromCenter0_0(new Coords(x, y), rows, cols);
        if(px.col > cols || px.row > rows)
        {
            return null;
        }
        return px;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JTextField colorPanel;
    private javax.swing.JPanel downPanel;
    private javax.swing.JMenuItem editChangeColor;
    private javax.swing.JMenuItem editClear;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JScrollPane logTextPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuTools;
    private javax.swing.JLabel xLabel;
    private javax.swing.JTextField xTextField;
    private javax.swing.JLabel yLabel;
    private javax.swing.JTextField yTextField;
    // End of variables declaration//GEN-END:variables
    
}
