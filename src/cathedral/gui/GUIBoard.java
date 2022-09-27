package cathedral.gui;

import cathedral.common.*;

import java.awt.*;

public class GUIBoard extends Canvas {

	static final long serialVersionUID = 0xca75ed02;

	private Board board;
	private GUIBuilding activeBuilding;
	
	public GUIBoard(Board b) {
		board = b;
		setBackground(SystemColor.control);
	}

	public Dimension getMinimumSize() {
		return new Dimension(
			Definitions.SIZE_X * 4,
			Definitions.SIZE_Y * 4
		);
	}

	public Dimension getMaximumSize() {
		return new Dimension(
				Definitions.SIZE_X * 2000,
				Definitions.SIZE_Y * 2000
		);
	}

	public Dimension getPreferredSize() {
		return new Dimension(
				Definitions.SIZE_X * GUIDefinitions.BOARD_EDGE_SIZE + GUIDefinitions.BOARD_BORDER_SIZE,
				Definitions.SIZE_Y * GUIDefinitions.BOARD_EDGE_SIZE + GUIDefinitions.BOARD_BORDER_SIZE
		);
	}

	public Board getBoard() {
		return board;
	}
	
	public GUIBuilding getActiveBuilding() {
		return activeBuilding;
	}
	
	public void setActiveBuilding(GUIBuilding b) {
		activeBuilding = b;
		if(b != null) b.building.setPosition(3*Definitions.SIZE_X, 3*Definitions.SIZE_Y, Definitions.NORTH);
		repaint();
	}
	
	public boolean fits() {
		if(activeBuilding == null) return false;
		return board.fits(activeBuilding.building);
	}

	public void paint(Graphics g) {
		Dimension d = getSize();
		int edgeSizeX = (int) Math.floor(d.width / Definitions.SIZE_X);
		int edgeSizeY = (int) Math.floor(d.height / Definitions.SIZE_Y);

		for(int x=0; x<Definitions.SIZE_X; x++) {
			for(int y=0; y<Definitions.SIZE_Y; y++) {
				if(activeBuilding != null && activeBuilding.building.getArea().isSet(x, y))
					g.setColor(fits() ? 
						new Color(GUIDefinitions.COLOR_POSSIBLE) : 
						new Color(GUIDefinitions.COLOR_IMPOSSIBLE)
					);

				else if(board.freeArea.isSet(x, y))
					g.setColor(new Color(GUIDefinitions.COLOR_FREE));
	
				else if(board.occupiedAreaLight.isSet(x, y))
					g.setColor(new Color(GUIDefinitions.COLOR_LIGHT));

				else if(board.occupiedAreaDark.isSet(x, y))
					g.setColor(new Color(GUIDefinitions.COLOR_DARK));

				else if(board.occupiedAreaCathedral.isSet(x, y))
					g.setColor(new Color(GUIDefinitions.COLOR_CATHEDRAL));

				else if(board.claimedAreaLight.isSet(x, y))
					g.setColor(new Color(GUIDefinitions.COLOR_LIGHT_CLAIMED));

				else if(board.claimedAreaDark.isSet(x, y))
					g.setColor(new Color(GUIDefinitions.COLOR_DARK_CLAIMED));
				
				g.fillRect(
					x * edgeSizeX + GUIDefinitions.BOARD_BORDER_SIZE,
					y*edgeSizeY + GUIDefinitions.BOARD_BORDER_SIZE,
					edgeSizeX - GUIDefinitions.BOARD_BORDER_SIZE, 
					edgeSizeY - GUIDefinitions.BOARD_BORDER_SIZE
				);
			}
		}

		g.setColor(new Color(GUIDefinitions.COLOR_BORDER));
		for(int x=0; x<=Definitions.SIZE_X; x++) {
			g.drawLine(x*edgeSizeX, 0, x*edgeSizeX, Definitions.SIZE_X*edgeSizeY);
		}
		for(int y=0; y<=Definitions.SIZE_X; y++) {
			g.drawLine(0, y*edgeSizeY, Definitions.SIZE_Y*edgeSizeX, y*edgeSizeY);
		}
	}

	
}
