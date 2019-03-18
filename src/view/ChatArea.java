package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import es.ucm.fdi.tp.base.Utils;

//OPCIONAL
public class ChatArea extends JPanel {

	private static final long serialVersionUID = 1L;
	private GameController gameCtrl;
	private JScrollPane msgArea;
	private JTextArea textArea;
	private JTextArea chatArea;
	private int player;
	private static final String SEND = "es/ucm/fdi/tp/images/send.jpg";
	
	public ChatArea(GameController gameCtrl, int player) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.gameCtrl = gameCtrl;
		this.player = player;
		initGI();
	}
	
	void initGI(){
		this.chatArea = new JTextArea(20,10);
		this.chatArea.setLineWrap(true);
		this.chatArea.setWrapStyleWord(true);
		this.chatArea.setForeground(new Color(51, 0, 102));
		this.chatArea.setFont(new Font("Verdana", Font.BOLD, 12));
		this.chatArea.setEditable(false);
		this.msgArea = new JScrollPane(chatArea);
		this.msgArea.setBorder(BorderFactory.createTitledBorder("Chat"));
		this.msgArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(this.msgArea);
		this.textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setForeground(new Color(51, 0, 102));
		textArea.setFont(new Font("Verdana", Font.BOLD, 12));
		textArea.setEditable(true);
		JScrollPane scroll = new JScrollPane(textArea);
		JButton send = new JButton(new ImageIcon(Utils.loadImage(SEND)));
		send.setToolTipText("Pulsa para enviar un mensaje");
		send.setBorder(null);
		send.addActionListener(new SendActionListener());
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(scroll);
		panel.add(send);
		this.add(panel);
	}
	

	public void addContent(String msg) {
		// TODO Auto-generated method stub
		//String message = this.textArea.getText() + System.getProperty("line.separator") + msg;
		this.chatArea.append(msg + "\n");
		this.repaint();
	}

	public void setContent(String msg) {
		// TODO Auto-generated method stub
		this.textArea.setText(msg);
	}
	
	public JTextArea getTextArea(){
		return this.textArea;
	}
	
	private class SendActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String text = ChatArea.this.textArea.getText();
			text.trim();
			String []words = text.split(" +");
			 if (words.length != 0 && !text.equalsIgnoreCase("")) {		
				ChatArea.this.gameCtrl.sendMessage("Jugador " + player + ": " + text);
				ChatArea.this.textArea.setText("");
			}
		}
		
	}
}
