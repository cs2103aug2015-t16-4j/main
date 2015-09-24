import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextInputPanel extends JPanel {
	public TextInputPanel() {
		Dimension size = getPreferredSize();
		size.height = 100;
		setPreferredSize(size);
		
		setBorder(BorderFactory.createTitledBorder("Input"));
		
		JLabel inputLabel = new JLabel("Input Box :");
		JTextField inputTextBox = new JTextField(28);
		
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints textPenalConstraint = new GridBagConstraints();
		
		//First Column
		
		textPenalConstraint.anchor = GridBagConstraints.LINE_START;
		
		textPenalConstraint.gridx = 0;
		textPenalConstraint.gridy = 0;
		add(inputLabel, textPenalConstraint);
		
		textPenalConstraint.anchor = GridBagConstraints.LINE_END;
		
		textPenalConstraint.gridx = 1;
		textPenalConstraint.gridy = 0;
		add(inputTextBox, textPenalConstraint);
	
		//Action perform
		inputTextBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String command = inputTextBox.getText();
				Command.processCommand(command);
				inputTextBox.setText("");
			}
		});
	}
}
