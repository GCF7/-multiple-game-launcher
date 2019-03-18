package es.ucm.fdi.tp.view;


import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import javax.swing.JToolBar;


import es.ucm.fdi.tp.base.Utils;

public class Settings extends JToolBar {
	
	protected JButton randMove;
	protected JButton smartMove;
	protected JButton previosState;
	protected JButton pause;
	protected JButton power;
	protected JButton restart;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("rawtypes")
	private GameViewController gameViewCtrl;
	private static final String SMART = "es/ucm/fdi/tp/images/smart1.2.png", RAND = "es/ucm/fdi/tp/images/rand1.png",
			RESTART = "es/ucm/fdi/tp/images/restart1.png", POWER = "es/ucm/fdi/tp/images/power1.png",
			PREVIOUS = "es/ucm/fdi/tp/images/previous.png", PAUSE = "es/ucm/fdi/tp/images/pause.png";
	
	public Settings(@SuppressWarnings("rawtypes") GameViewController gameViewCtrl) {
		super();
		this.gameViewCtrl = gameViewCtrl;
		this.initGUI();
	}
	
	private void initGUI(){
		//BOTON MOVIMIENTO INTELIGENTE
		this.smartMove = new JButton();
		smartMove.setToolTipText("Pulsa para usar una jugada smart");
		smartMove.setIcon(new ImageIcon(Utils.loadImage(SMART)));
		smartMove.setBorder(null);
		smartMove.addActionListener(new SmartActionListener());
		smartMove.setEnabled(false);
		//BOTON MOVIMIENTO ALEATORIO
		this.randMove = new JButton();
		randMove.setToolTipText("Pulsa para usar una jugada rand");
		randMove.setIcon(new ImageIcon(Utils.loadImage(RAND)));
		randMove.setBorder(null);
		randMove.addActionListener(new RandActionListener());
		randMove.setEnabled(false);
		//BOTON REINICIAR
		this.restart = new JButton();
		restart.setToolTipText("Pulsa para reiniciar juego");
		restart.setIcon(new ImageIcon(Utils.loadImage(RESTART)));
		restart.setBorder(null);
		restart.addActionListener(new RestartActionListener());
		//BOTON APAGAR
		this.power = new JButton();
		power.setToolTipText("Pulsa para reiniciar juego");
		power.setIcon(new ImageIcon(Utils.loadImage(POWER)));
		power.setBorder(null);
		power.addActionListener(new PowerActionListener());
		//BOTON PREVIOUS
		this.previosState = new JButton();
		this.previosState.setToolTipText("Pulsa para deshacer la última jugada");
		this.previosState.setIcon(new ImageIcon(Utils.loadImage(PREVIOUS)));
		this.previosState.setBorder(null);
		this.previosState.addActionListener(new PreviousActionListener());
		this.previosState.setEnabled(false);
		//BOTON PAUSE
		this.pause = new JButton();
		this.pause.setToolTipText("Pulsa para parar el juego");
		this.pause.setIcon(new ImageIcon(Utils.loadImage(PAUSE)));
		this.pause.setBorder(null);
		this.pause.addActionListener(new PauseActionListener());
		this.pause.setEnabled(false);
		//MENSAJE PARA DESPLEGABLE
		JLabel mensaje = new JLabel("Player mode: ");
		//DESPLEGABLE TIPO DE JUGADOR
		JComboBox<PlayerMode> playerType = new JComboBox<PlayerMode>();
		playerType.addItem(PlayerMode.MANUAL);
		playerType.addItem(PlayerMode.RANDOM);
		playerType.addItem(PlayerMode.SMART);
		playerType.addActionListener(new PlayerTypeActionListener());
		//AJUESTE
		JLabel ajustar = new JLabel();
		ajustar.setBackground(this.getBackground());
		//ajustar.setMaximumSize(new Dimension(400000, 400));
		//AÑADIR TODOS LOS ELEMENTOS
		this.add(smartMove);
		this.addSeparator(new Dimension(10,0));
		this.add(randMove);
		this.addSeparator(new Dimension(10,0));
		this.add(restart);
		this.addSeparator(new Dimension(10,0));
		this.add(power);
		this.addSeparator(new Dimension(10,0));
		this.add(this.previosState);
		this.addSeparator(new Dimension(10,0));
		this.add(this.pause);
		this.addSeparator(new Dimension(10,0));
		this.add(mensaje);
		this.addSeparator(new Dimension(10,0));
		this.add(playerType);
		this.addSeparator(new Dimension(10,0));
		this.add(ajustar);
	}
	
	private class RestartActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Settings.this.gameViewCtrl.restartGame();
		}
		
	}
	
	private class RandActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Settings.this.gameViewCtrl.makeRandomMove();
		}
		
	}
	
	private class SmartActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Settings.this.gameViewCtrl.makeSmartMove();
		}
		
	}
	
	private class PowerActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Settings.this.gameViewCtrl.stopGame();
		}
		
	}
	
	private class PlayerTypeActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			@SuppressWarnings("unchecked")
			JComboBox<PlayerMode> cb = (JComboBox<PlayerMode>)e.getSource();
			PlayerMode name = (PlayerMode)cb.getSelectedItem();
			
            switch (name) {//check for a match
                case MANUAL:
                	Settings.this.gameViewCtrl.changePlayerMode(PlayerMode.MANUAL); 
                    break;
                case RANDOM:
                	Settings.this.gameViewCtrl.changePlayerMode(PlayerMode.RANDOM);
                    break;
                case SMART:
                	Settings.this.gameViewCtrl.changePlayerMode(PlayerMode.SMART);
                    break;                        
            }
        }
	}
	
	private class PreviousActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Settings.this.gameViewCtrl.previousState();
		}
		
	}
	
	private class PauseActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Settings.this.gameViewCtrl.pause();
		}
		
	}
	
	public void enableMoveAndPause(boolean b) {
		this.randMove.setEnabled(b);
		this.smartMove.setEnabled(b);
		this.pause.setEnabled(b);
		this.repaint();
	}
	
	public void enablePrevious(boolean b) {
		this.previosState.setEnabled(b);
		this.repaint();
	}
	
	
	public void enableAll(boolean b){
		this.randMove.setEnabled(b);
		this.smartMove.setEnabled(b);
		this.restart.setEnabled(b);
		this.power.setEnabled(b);
	}
}
