import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class Main extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private DatabaseManager dm;
	private JMenuBar menuBar;
	private JMenu menu, submenu;
	private JMenuItem menuItem;
	
	private JTable mapTable;
	private JTable playerTable;
	private JTable baseTable;
	private JTable vehicleTable;
	
	private JPanel mapPanel;
	private JPanel playerPanel;
	private JPanel basePanel;
	private JPanel vehiclePanel;
	
	private JTabbedPane tabbedPane;
	
	public Main() {
		super(new GridLayout());
		dm = new DatabaseManager();
		setBackground(Color.CYAN);
		
		menuBar = new JMenuBar();

		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription(
		        "File");
		menuBar.add(menu);

        //a submenu
        menu.addSeparator();
        submenu = new JMenu("Create");
        submenu.setMnemonic(KeyEvent.VK_C);

		//a group of JMenuItems
		menuItem = new JMenuItem("Map",
		                         KeyEvent.VK_M);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "Creates a map object");
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Base",
		                         KeyEvent.VK_B);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "Creates a base object");
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Player",
		                         KeyEvent.VK_P);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_3, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "Creates a player object");
		menuItem.addActionListener(this);
		submenu.add(menuItem);

		menuItem = new JMenuItem("Vehicle",
		                         KeyEvent.VK_P);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_4, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
		        "Creates a vehicle object");
		menuItem.addActionListener(this);
		submenu.add(menuItem);

        menu.add(submenu);

		//Build second menu in the menu bar.
		menu = new JMenu("View");
		menu.setMnemonic(KeyEvent.VK_V);
		menu.getAccessibleContext().setAccessibleDescription(
		        "view game item");
		menuBar.add(menu);
		
		tabbedPane = new JTabbedPane();
		ImageIcon icon = new ImageIcon("images/middle.gif");
		
		mapPanel = new JPanel(new GridLayout());
		tabbedPane.addTab("Map", icon, mapPanel,
		                  "Map Table");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

		playerPanel = new JPanel(new GridLayout());
		tabbedPane.addTab("Player", icon, playerPanel,
		                  "Player Table");
		tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

		basePanel = new JPanel(new GridLayout());
		tabbedPane.addTab("Base", icon, basePanel,
		                  "Base Table");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

		vehiclePanel = new JPanel(new GridLayout());
		tabbedPane.addTab("Vehicle", icon, vehiclePanel,
		                  "Vehicle Table");
		tabbedPane.setMnemonicAt(2, KeyEvent.VK_4);
		
		createTables();
		
		add(tabbedPane, BorderLayout.CENTER);

		setPreferredSize(new Dimension((int)baseTable.getPreferredSize().getWidth(), 500));
		
		frame = new JFrame("Server");
        frame.add(this);
        
        frame.setJMenuBar(menuBar);
        
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.requestFocus();
	}
	
	private DefaultTableModel getTableModel(String table, Object[][] tableData, String[] fields) {
		DefaultTableModel model = new DefaultTableModel(fields, tableData.length) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int col) {
            	switch(table) {
            	case "Player":
            	case "Base":
            		if (row == 0) return false;
            	}
                return true;
            }
        };
        for (Object[] o : tableData) {
        	model.addRow(o);
        }
        // Add data; note auto-boxing
        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
            	if (e.getSource() instanceof JTable) {
                	JTable jTable = (JTable)e.getSource();
                    dm.updateTable(table, (String)(jTable.getModel().getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn())), fields[jTable.getSelectedColumn()]);
            	}
            }
        });
        return model;
	}
	
	private void createTables() {
		playerTable = new JTable(getTableModel("Player", dm.getTableData("Player"), dm.getFields("Player")));
		playerPanel.add(new JScrollPane(playerTable));
		playerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		baseTable = new JTable(getTableModel("Base", dm.getTableData("Base"), dm.getFields("Base")));
		basePanel.add(new JScrollPane(baseTable));
		baseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
		mapTable = new JTable(getTableModel("Map", dm.getTableData("Map"), dm.getFields("Map")));
		mapPanel.add(new JScrollPane(mapTable));
		mapTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		vehicleTable = new JTable(getTableModel("Vehicle", dm.getTableData("Vehicle"), dm.getFields("Vehicle")));
		vehiclePanel.add(new JScrollPane(vehicleTable));
		vehicleTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
 
    // Returns just the class name -- no package info.
    protected String getClassName(Object o) {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex+1);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem)(e.getSource());
        String title = source.getText();
        switch (title) {
	        case "Player":
	        	new Entity(title, dm, this, 0);
	        	break;
	        case "Map":
	        	new Entity(title, dm, this, 0);
	        	break;
	        case "Base":
	        	new Entity(title, dm, this, 1);
	        	break;
	        case "Vehicle":
	        	new Entity(title, dm, this, 1);
	        	break;
        }
	}

	public void update(String table) {
		switch(table) {
		case "Player":
			DefaultTableModel model = (DefaultTableModel) playerTable.getModel();
			Object[][] data = dm.getTableData("Player");
			model.addRow(data[data.length - 1]);
			break;
		case "Map":
			model = (DefaultTableModel) mapTable.getModel();
			data = dm.getTableData("Map");
			model.addRow(data[data.length - 1]);
			break;
		case "Base":
			model = (DefaultTableModel) baseTable.getModel();
			data = dm.getTableData("Base");
			model.addRow(data[data.length - 1]);
			break;
		case "Vehicle":
			model = (DefaultTableModel) vehicleTable.getModel();
			data = dm.getTableData("Vehicle");
			model.addRow(data[data.length - 1]);
			break;
		}
		
		setPreferredSize(new Dimension((int)baseTable.getPreferredSize().getWidth(), 500));
		frame.pack();
		frame.repaint();
	}

	public static void main(String[] args) {
		new Main();
	}

}
