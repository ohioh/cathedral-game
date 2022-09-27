package cathedral.gui;

import java.awt.*;
import cathedral.common.*;

public class GUIStatus extends Panel {

	static final long serialVersionUID = 0xca75ed01;

	private Label left;
	private Label center;
	private Label right;

	public GUIStatus(String l, String c, String r) {
		setLayout(new BorderLayout());

		left = new Label(l);
		add(left, BorderLayout.WEST);

		center = new Label(c);
		center.setAlignment(Label.CENTER);
		add(center, BorderLayout.CENTER);

		right = new Label(r);
		add(right, BorderLayout.EAST);
	}
	
	public void printMove(Game g, Building b) {
		String[] directions = {"north", "east", "south", "west"};
		char[] xFields = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
		String msg = new String();

		if(b.getPlayer() != Definitions.CATHEDRAL) {
			msg = b.getPlayer() == Definitions.LIGHT ? "light, " : "dark, ";
		}
		msg = msg + b.getName() + ", " + directions[b.getDirection()] + ", "
		          + xFields[b.getPosX()] + (b.getPosY()+1);
		
		center.setText(msg);
		left.setText("Light (" + g.getScore(Definitions.LIGHT) + ")");
		right.setText("Dark (" + g.getScore(Definitions.DARK) + ")");
	}
	
	public void printResult(Game g) {
		String msg = new String();
		int light = g.getScore(Definitions.LIGHT);
		int dark = g.getScore(Definitions.DARK);
		
		if(light<dark) {
			msg = "light won";
		} else if (dark<light) {
			msg = "dark won";
		} else {
			msg = "draw";
		}

		center.setText(msg);
	}
}
