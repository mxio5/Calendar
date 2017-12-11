import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class createActionListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		// current date in mm/dd/yyyy form
		String currentDate = " 11/17/2017 ";
		JFrame createFrame = new JFrame("Create an Event");
		JPanel panel = new JPanel(new FlowLayout());
		JPanel panel2 = new JPanel(new FlowLayout());
		BoxLayout bl = new BoxLayout(createFrame.getContentPane(), BoxLayout.Y_AXIS);
		createFrame.setLayout(bl);
		//frame.setPreferredSize(new Dimension(100, 50));
		JLabel label = new JLabel("Event: ");
		JButton save = new JButton("Save");
		JTextField writeField = new JTextField(20);
		writeField.setEditable(true);

		JTextField date = new JTextField(currentDate);
		JLabel time = new JLabel("Time: ");
		JTextField start = new JTextField(4);
		JLabel to = new JLabel(" to ");
		JTextField end = new JTextField(4);
		
		
		ActionListener saveListener = new ActionListener(){
			String w = writeField.getText();
			String s = start.getText();
			int sint = Integer.parseInt(s);
			String e = end.getText();
			int eint = Integer.parseInt(e);
			public void actionPerformed(ActionEvent e) {
				//events.add(w, 11, 20, 2017, sint, eint);
				
			}
			
		};

		panel.add(label);
		panel.add(writeField);

		panel2.add(date);
		panel2.add(time);
		panel2.add(start);
		panel2.add(to);
		panel2.add(end);

		createFrame.add(panel);
		createFrame.add(panel2);
		createFrame.add(save);
		createFrame.pack();
		createFrame.setVisible(true);
	}

}
