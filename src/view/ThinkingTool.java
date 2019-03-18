package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import es.ucm.fdi.tp.base.Utils;

public class ThinkingTool extends JToolBar{
	
	private static final long serialVersionUID = 1L;
	private static final int N = 4; 
	boolean thinking;
	@SuppressWarnings("rawtypes")
	private GameViewController gameViewCtrl;
	JLabel pensando;
	JButton stop;
	JSpinner selectorHilos;
	JSpinner selectorTiempo;
	
	private static final String CLOCK = "es/ucm/fdi/tp/images/icon1.png", CEREBRO = "es/ucm/fdi/tp/images/icon2.png",
			STOP = "es/ucm/fdi/tp/images/icon3.png";
	
	public ThinkingTool(@SuppressWarnings("rawtypes") GameViewController gameViewCtrl) {
		super();
		this.gameViewCtrl = gameViewCtrl;
		this.thinking = false;
		this.initGUI();
	}
	
	private void initGUI(){
		this.pensando = new JLabel(new ImageIcon(Utils.loadImage(CEREBRO)));
		pensando.setBackground(Color.YELLOW);
		this.add(pensando);
		this.addSeparator(new Dimension(5,0));
		this.selectorHilos = new JSpinner();
		SpinnerNumberModel nm = new SpinnerNumberModel();
	    nm.setMaximum(N);
	    nm.setMinimum(1);
	    nm.setValue(1);
	    selectorHilos.setModel(nm);
		this.add(selectorHilos);
		this.addSeparator(new Dimension(5,0));
	    JLabel  threads = new JLabel("threads");
	    this.add(threads);
	    this.addSeparator(new Dimension(5,0));
		JLabel tiempo = new JLabel(new ImageIcon(Utils.loadImage(CLOCK)));
		this.add(tiempo);
		this.addSeparator(new Dimension(5,0));
		this.selectorTiempo = new JSpinner();
		SpinnerNumberModel nt = new SpinnerNumberModel();
	    nt.setMaximum(5000);
	    nt.setMinimum(500);
	    nt.setStepSize(500);
	    nt.setValue(500);
	    selectorTiempo.setModel(nt);
		this.add(selectorTiempo);
		this.addSeparator(new Dimension(5,0));
		JLabel  ms = new JLabel("ms.");
	    this.add(ms);
		this.addSeparator(new Dimension(5,0));
		this.stop = new JButton();
		stop.setToolTipText("Pulsa para cancelar la busqueda de jugada");
		stop.setIcon(new ImageIcon(Utils.loadImage(STOP)));
		stop.setBorder(null);
		this.stop.setEnabled(this.thinking);
		stop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ThinkingTool.this.gameViewCtrl.cancelSmartMove();
			}
		});
		this.add(stop);
	}
	
	public void setThinking(boolean b) {
		// TODO Auto-generated method stub
		this.thinking = b;
		this.pensando.setOpaque(this.thinking);
		this.stop.setEnabled(this.thinking);
		this.repaint();
	}

	public int getNThreads() {
		return (int) this.selectorHilos.getValue();
	}
	
	public int getTime() {
		return (int) this.selectorTiempo.getValue();
	}
}
