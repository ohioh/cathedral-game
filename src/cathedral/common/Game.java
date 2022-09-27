package cathedral.common;

import cathedral.gui.*;
import cathedral.agent.*;
import java.util.ArrayList;

public class Game {
	public int turn;
	public Board board;
	public ArrayList<Building> buildingsLight;
	public ArrayList<Building> buildingsDark;
	public Player playerLight;
	public Player playerDark;

	public Game() {
		board = new Board(this);
	
		buildingsLight = new ArrayList<Building>();
		buildingsDark = new ArrayList<Building>();
		Building cathedral = new Building(
				Definitions.BUILDING_NAME[Definitions.CATHEDRAL],
				Definitions.CATHEDRAL, Definitions.CATHEDRAL,
				Definitions.BUILDING_SHAPE[Definitions.CATHEDRAL],
				Definitions.BUILDING_PRIME_SQUARE[Definitions.CATHEDRAL]
		);

		if(Definitions.FIRST_TURN == Definitions.LIGHT) {
			buildingsLight.add(cathedral);
		} else {
			buildingsDark.add(cathedral);
		}
		
		for(int b : Definitions.BUILDINGS_LIGHT) {
			buildingsLight.add(new Building(
					Definitions.BUILDING_NAME[b],
					b, Definitions.LIGHT,
					Definitions.BUILDING_SHAPE[b],
					Definitions.BUILDING_PRIME_SQUARE[b]
			));		
		}
	
		for(int b : Definitions.BUILDINGS_DARK) {
			buildingsDark.add(new Building(
					Definitions.BUILDING_NAME[b],
					b, Definitions.DARK,
					Definitions.BUILDING_SHAPE[b],
					Definitions.BUILDING_PRIME_SQUARE[b]
			));		
		}
	}

	public void setPlayerLight(Player player) {
		playerLight = player;
	}

	public void setPlayerDark(Player player) {
		playerDark = player;
	}

	public boolean setBuilding(Building b) {
		if(board.addBuilding(b)) {
			buildingsLight.remove(b);
			buildingsDark.remove(b);
			return true;
		}
		return false;
	}

	public boolean unsetBuilding(Building b) {
		if(board.removeBuilding(b)) {
			switch(b.getPlayer()) {
			case Definitions.CATHEDRAL:
				break;
			case Definitions.LIGHT:
				buildingsLight.add(b);
//				buildingsDark.add(b);
				break;
			case Definitions.DARK:
//				buildingsLight.add(b);
				buildingsDark.add(b);
			}
			return true;
		}
		return false;
	}

	public int getScore(int player) {
		int score = 0;
		
		if (player == Definitions.LIGHT) {
			for(Building b : buildingsLight) {
				if(b.getType() != Definitions.CATHEDRAL) score += b.getWeigth();
			}
			return score;

		} else {
			for(Building b : buildingsDark) {
				if(b.getType() != Definitions.CATHEDRAL) score += b.getWeigth();
			}
			return score;
		}
	}
	
	public void run(GUIFrame gui) {
		Building b;
		
		if(!(playerLight.end() && playerDark.end()) &&
				Definitions.FIRST_TURN == Definitions.LIGHT) {
			b = playerLight.move();
			setBuilding(b);
			if(b.getType() == Definitions.CATHEDRAL) {
				gui.playerDark.hideCathedralContainer();
				gui.playerLight.hideCathedralContainer();
			}
			gui.status.printMove(this, b);
			gui.repaint();
		}

		while(!(playerLight.end() && playerDark.end())) {
			if(!playerDark.end()) {
				b = playerDark.move();
				setBuilding(b);
				if(b.getType() == Definitions.CATHEDRAL) {
					gui.playerDark.hideCathedralContainer();
					gui.playerLight.hideCathedralContainer();
				}
				gui.status.printMove(this, b);
				gui.repaint();
			}
			if(!playerLight.end()) {
				b = playerLight.move();
				setBuilding(b);
				gui.status.printMove(this, b);
				gui.repaint();
			}
		}
		gui.status.printResult(this);
	}
	
	public void printBuildings(int player) {
		ArrayList<Building> buildings = player == Definitions.LIGHT ?
				buildingsLight : buildingsDark;
		System.out.println(player == Definitions.LIGHT ? "Light" : "Dark");
		for(Building b : buildings) {
			System.out.println(b.getName());
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		GUIFrame gui = new GUIFrame();
		gui.start(game);
		
		game.setPlayerLight(Definitions.LIGHT_AGENT == Definitions.HUMAN ? 
				(Player) gui.playerLight : 
				new Agent(game, Definitions.LIGHT));
		game.setPlayerDark(Definitions.DARK_AGENT == Definitions.HUMAN ? 
				(Player) gui.playerDark : 
					new Agent(game, Definitions.DARK));
		game.run(gui);
	}
}
