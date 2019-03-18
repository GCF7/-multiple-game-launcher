package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessageArea extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane msgArea;
	private JTextArea textArea;
	public MessageArea() {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		initGI();
	}
	
	void initGI(){
		this.textArea = new JTextArea(20,10);
		this.textArea.setLineWrap(true);
		this.textArea.setWrapStyleWord(true);
		this.textArea.setForeground(new Color(51, 0, 102));
		this.textArea.setFont(new Font("Verdana", Font.BOLD, 12));
		this.textArea.setEditable(false);
		this.msgArea = new JScrollPane(textArea);
		this.msgArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.msgArea.setBorder(BorderFactory.createTitledBorder("Status Messages"));
		this.add(this.msgArea);
	}
	

	public void addContent(String msg) {
		// TODO Auto-generated method stub
		//String message = this.textArea.getText() + System.getProperty("line.separator") + msg;
		this.textArea.append(msg + "\n");
	}

	public void setContent(String msg) {
		// TODO Auto-generated method stub
		this.textArea.setText(msg);
	}
	
	public JTextArea getTextArea(){
		return this.textArea;
	}

}
