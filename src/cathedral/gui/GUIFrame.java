package cathedral.gui;

import cathedral.common.*;

import java.awt.*;
import java.awt.event.*;

public class GUIFrame extends Frame {

	static final long serialVersionUID = 0xca75ed00;

	public GUIStatus status;
	public GUIPlayer playerLight;
	public GUIPlayer playerDark;
	public GUIBoard board;
	public Game game;

	public GUIFrame() {
		setTitle("Cathedral");
		setLayout(new BorderLayout());
		setBackground(SystemColor.control);
		
		addWindowListener( new WindowAdapter(){ 
			public void windowClosing(WindowEvent we){
				setVisible(false); dispose(); 
				System.exit(0);
			}
		});
	}
	
	public void start(Game game) {
		this.game = game;
		
		status = new GUIStatus("Light (" + game.getScore(Definitions.LIGHT) + ")",
				"", "Dark (" + game.getScore(Definitions.DARK) + ")");
		add(status, BorderLayout.NORTH);

		board = new GUIBoard(game.board);
		add(board, BorderLayout.CENTER);

		playerLight = new GUIPlayer(Definitions.LIGHT, game.buildingsLight, this);
		add(playerLight, BorderLayout.WEST);
	
		playerDark = new GUIPlayer(Definitions.DARK, game.buildingsDark, this);
		add(playerDark, BorderLayout.EAST);
	
		pack();
		setVisible(true);
		setResizable(false);
	}

}
