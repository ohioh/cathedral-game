package cathedral.gui;

import cathedral.common.*;

import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

public class GUIPlayer extends Panel implements ActionListener, Player {

	static final long serialVersionUID = 0xca75ed03;

	private int player;
	private boolean human;
	private int turn;
	
	private GUIFrame frame;
	private int buildingArea;
	private Container cathedralContainer;
	private Container buildingsContainer;
	private ArrayList<GUIBuilding> buildings;
	private Object lock;
	
	private int buildingsHash;
	private Area availableArea;
	private boolean end;
	
	public GUIPlayer(int player, ArrayList<Building> buildingList, GUIFrame frame) {
		GUIBuilding building;

		this.player = player;
		if(player == Definitions.LIGHT) {
			human = Definitions.LIGHT_AGENT == Definitions.HUMAN;
		} else {
			human = Definitions.DARK_AGENT == Definitions.HUMAN;
		}
		
		buildings = new ArrayList<GUIBuilding>();
	
		cathedralContainer = new Panel();
		cathedralContainer.setVisible(false);
		add(cathedralContainer);
		
		buildingsContainer = new Panel();
		add(buildingsContainer);

		for(Building b : buildingList) {
			addBuilding(building = new GUIBuilding(b, frame));
			building.addActionListener(this);
		}

		setEnabled(false);
		lock = new Object();
		this.frame = frame;
	}

	public Building move() {
		setEnabled(true);
		frame.board.setActiveBuilding(null);
		while(!frame.board.fits()) {
			synchronized (lock) {
			    try {
			        lock.wait();
			    }
			    catch (InterruptedException e) {}
			}
		}
		setEnabled(false);

		GUIBuilding b = frame.board.getActiveBuilding();
		b.setActive(false);
		frame.board.setActiveBuilding(null);
		
/*		if(b.getType() == Definitions.CATHEDRAL) {
			cathedralContainer.setVisible(false);
			buildingsContainer.setVisible(true);
			buildingsContainer.setPreferredSize(getPreferredSize());
			frame.pack();
		}
*/		
		return b.building;
	}

	public boolean end() {
		int hash;
		ArrayList<Building> availableBuildings;
		
		Area a = new Area();
		a.add(frame.board.getBoard().freeArea);
		a.add(player == Definitions.LIGHT ? 
				frame.board.getBoard().claimedAreaLight : frame.board.getBoard().claimedAreaDark);
		
		if(player == Definitions.LIGHT) {
			hash = frame.game.buildingsLight.hashCode();
		} else {
			hash = frame.game.buildingsDark.hashCode();
		}
		
		if(a.equals(availableArea) && buildingsHash == hash) {
			return end;
		}
		
		availableArea = a;
		buildingsHash = hash;
		end = true;

		availableBuildings = player == Definitions.LIGHT ?
				frame.game.buildingsLight : frame.game.buildingsDark;

		if(availableBuildings.isEmpty()) {
			return end;
		}
		
		for(Building b : availableBuildings) {
			if(b.getWeigth() > availableArea.cardinality()) continue;
			if(b.findPossiblePosition(availableArea)) {
				end = false;
				return end;
			}
		}
		
		return end;
	}

	public void hideCathedralContainer() {
		cathedralContainer.setVisible(false);
		buildingsContainer.setVisible(true);
		buildingsContainer.setPreferredSize(getPreferredSize());
		frame.pack();
		super.repaint();
	}
	
	public boolean isHuman() {
		return human;
	}
	
	public void addBuilding(GUIBuilding building) {
		if(building.getType() == Definitions.CATHEDRAL) {
			buildingsContainer.setVisible(false);
			cathedralContainer.add(building, BorderLayout.CENTER);
			cathedralContainer.setVisible(true);
		} else {
			buildingArea += building.getFieldSize();
			buildings.add(building);
			buildingsContainer.add(building);
		}
		buildingsContainer.setPreferredSize(getPreferredSize());
		cathedralContainer.setPreferredSize(getPreferredSize());
	}

	public Dimension getPreferredSize() {
		return new Dimension(
			6 * ( GUIDefinitions.BUTTON_EDGE_SIZE + 2*GUIDefinitions.BUTTON_PADDING ),
			( buildingArea / 6 ) * ( GUIDefinitions.BUTTON_EDGE_SIZE + 2*GUIDefinitions.BUTTON_PADDING )
		);
	}

	public void setEnabled(boolean status) {
		super.setEnabled(status);
		for(GUIBuilding b : buildings) {
			b.setEnabled(status);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "setBuilding") {
			synchronized (lock) {
				lock.notify();
			}
			return;
		}
		
		if(frame.board.getActiveBuilding() != null) {
			frame.board.getActiveBuilding().setActive(false);
		}
		if(e.getSource().getClass().getName() == "cathedral.gui.GUIBuilding") {
			((GUIBuilding) e.getSource()).setActive(true);
		}
	}
}
