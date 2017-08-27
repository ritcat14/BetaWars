
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Entity implements ActionListener {

	private JTextField[] textFields;
	private JButton submit;
	private JFrame frame;
	private DatabaseManager dm;
	private String table;
	private Main main;
	
	public Entity(String title, DatabaseManager dm, Main m, int offset) {
		this.main = m;
		this.table = title;
		this.dm = dm;
		String[] fields = dm.getFields(title);
		textFields = new JTextField[fields.length - offset];

        SpringLayout layout = new SpringLayout();
        JPanel panel = new JPanel(layout);
		
		for (int i = offset; i < fields.length; i++) {
			textFields[i - offset] = new JTextField("", 15);
			JLabel label = new JLabel(fields[i]);
			panel.add(label);
			panel.add(textFields[i - offset]);
		}
		
		submit = new JButton("Submit");
		submit.addActionListener(this);
		panel.add(new JLabel(""));
		panel.add(submit);
		
		SpringUtilities.makeGrid(panel,
                (fields.length - offset)+1, 2, //rows, cols
                0, 0, //initialX, initialY
                0, 0);
		
		frame = new JFrame("Create " + title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String[] strings = new String[textFields.length];
		for (int i = 0; i < textFields.length; i++) {
			String s = textFields[i].getText();
			try {
				Integer.parseInt(s);
				strings[i] = s;
			} catch (Exception ex) {
				if (s.equals("false") || s.equals("true")) {
					strings[i] =s;
				} else {
					// If we are here, it is neither an int or a boolean, so it must be a string and we need to format it for SQL
					strings[i] = "'" + s + "'";
				}
			}
		}
		dm.add(table, strings);
		main.update(table);
		frame.dispose();
	}

}
