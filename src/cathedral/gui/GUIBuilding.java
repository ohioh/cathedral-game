package cathedral.gui;

import cathedral.common.*;

import java.awt.*;
import java.awt.event.*;

public class GUIBuilding extends Button implements MouseListener, MouseMotionListener {
	
	static final long serialVersionUID = 0xca75ed04;

	public Building building;
	private int size;
	private int direction;
	private boolean active;
	private Color enabledColor;
	private Color disabledColor;
	private GUIFrame frame;
	
	public GUIBuilding(Building b, GUIFrame frame) {
		building = b;

		if(building.getSizeX() < building.getSizeY()) {
			size = building.getSizeY();
		} else {
			size = building.getSizeX();
		}
		if(size < 2) size = 2;
		if(building.getWeigth() > 3 && size < 3) size = 3;

		direction = Definitions.NORTH;
		active = false;
		
		switch(building.getPlayer()) {
		default:
		case Definitions.CATHEDRAL:
			enabledColor = new Color(GUIDefinitions.COLOR_CATHEDRAL);
			disabledColor = new Color(GUIDefinitions.COLOR_CATHEDRAL);
			break;
		case Definitions.LIGHT:
			enabledColor = new Color(GUIDefinitions.COLOR_LIGHT);
			disabledColor = new Color(GUIDefinitions.COLOR_LIGHT_DISABLED);
			break;
		case Definitions.DARK:
			enabledColor = new Color(GUIDefinitions.COLOR_DARK);
			disabledColor = new Color(GUIDefinitions.COLOR_DARK_DISABLED);
		}
		setBackground(SystemColor.control);
		
		this.frame = frame;

		addMouseListener(this);
		frame.board.addMouseListener(this);
	}

	public int getType() {
		return building.getType();
	}
	
	public int getFieldSize() {
		return size * size;
	}
	
	public Dimension getMinimumSize() {
		return new Dimension(
			size * 2 + 2*GUIDefinitions.BUTTON_PADDING,
			size * 2 + 2*GUIDefinitions.BUTTON_PADDING
		);
	}

	public Dimension getMaximumSize() {
		return new Dimension(
			size * 100 + 2*GUIDefinitions.BUTTON_PADDING,
			size * 100 + 2*GUIDefinitions.BUTTON_PADDING
		);
	}

	public Dimension getPreferredSize() {
		return new Dimension(
			GUIDefinitions.BUTTON_EDGE_SIZE * size + 2*GUIDefinitions.BUTTON_PADDING,
			GUIDefinitions.BUTTON_EDGE_SIZE * size + 2*GUIDefinitions.BUTTON_PADDING
		);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Dimension d = getSize();
		int edgeSizeX = (int) Math.floor((d.width - 2*GUIDefinitions.BUTTON_PADDING) / size);
		int edgeSizeY = (int) Math.floor((d.height - 2*GUIDefinitions.BUTTON_PADDING) / size);
		g.setColor(isEnabled() && (building.getPlayer() == Definitions.LIGHT ?
				frame.playerLight.isHuman() : frame.playerDark.isHuman()) ? 
				enabledColor : disabledColor);

		for(int x=0; x<size; x++) {
			for(int y=0; y<size; y++) {
				if(isSet(x,y)) {
					g.fillRect(GUIDefinitions.BUTTON_PADDING + x*edgeSizeX ,
							GUIDefinitions.BUTTON_PADDING + y*edgeSizeY, edgeSizeX, edgeSizeY);
				} else {
					g.clearRect(GUIDefinitions.BUTTON_PADDING + x*edgeSizeX ,
							GUIDefinitions.BUTTON_PADDING + y*edgeSizeY, edgeSizeX, edgeSizeY);
				}
			}
		}
	}

	public boolean isSet(int x, int y) {
		return isSet(x, y, direction);
	}

	public boolean isSet(int x, int y, int direction) {
		if(
			(direction == Definitions.WEST || direction == Definitions.EAST) &&
			(x >= building.getSizeY() || y >= building.getSizeX()) ||
			(direction != Definitions.WEST && direction != Definitions.EAST) &&
			(x >= building.getSizeX() || y >= building.getSizeY())
		) {
			return false;
		}

		boolean[][] shape = building.getShape();

		switch(direction) {
		default:
		case Definitions.NORTH:
			return shape[x][y];
		case Definitions.EAST:
			return shape[y][building.getSizeY()-x-1];
		case Definitions.SOUTH:
			return shape[building.getSizeX()-x-1][building.getSizeY()-y-1];
		case Definitions.WEST:
			return shape[building.getSizeX()-y-1][x];
		}
	}

	public void setActive(boolean status) {
		if(!active && status && available()) frame.board.setActiveBuilding(this);
		active = status;
		if(active && available()) {
			frame.board.addMouseMotionListener(this);
		} else {
			frame.board.removeMouseMotionListener(this);
		}
	}

	public boolean available() {
		if(building.getPlayer() == Definitions.LIGHT ||
				(building.getType() == Definitions.CATHEDRAL &&
				Definitions.FIRST_TURN == Definitions.LIGHT)) {
			return frame.game.buildingsLight.contains(building);
		} else {
			return frame.game.buildingsDark.contains(building);
		}
			
	}

	public void setEnabled(boolean status) {
		super.setEnabled(status && available());
	}
	
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

	public void mouseClicked(MouseEvent e) {
		//rotate on rightclick if active
		if(active && e.getModifiers() == InputEvent.BUTTON3_MASK) {
			direction = (direction < 3) ? direction + 1 : 0;
			building.setDirection(direction);
			frame.board.repaint();
			repaint();
		}
		
		//set building
		else if(active && e.getSource() == frame.board && e.getModifiers() == InputEvent.BUTTON1_MASK) {
			if(frame.board.fits()) {
				ActionEvent setBuilding = new ActionEvent(
						this, (int) Math.round(Math.random()), "setBuilding");
				for(ActionListener l : getActionListeners()) {
					l.actionPerformed(setBuilding);
				}
			}
		}
	}

	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {
		Dimension d = ((Component) e.getSource()).getSize();
		int x = (int) Math.floor((e.getPoint().x * Definitions.SIZE_X) / d.width );
		int y = (int) Math.floor((e.getPoint().y * Definitions.SIZE_Y) / d.height);

		if(x != building.getPosX() || y != building.getPosY()) {
			building.setPosition(x, y, direction);
			frame.board.repaint();
		}
	}

}
