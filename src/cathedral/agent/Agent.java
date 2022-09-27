package cathedral.agent;

import java.util.ArrayList;

import cathedral.common.*;

public class Agent implements Player {
	
	public Game game;
	public int player;
	private int turn;
	
	private Strategy firstMove;
	private Strategy gainGround;
	private Strategy defendBuilding;
	private Strategy conquerBuilding;
	private Strategy constrainOpponent;
	private Strategy fillUp;

	private int buildingsHash;
	private Area availableArea;
	private boolean end;

	public Agent(Game game, int player) {
		this.game = game;
		this.player = player;
		this.turn = 0;
		
		firstMove = new StrategyFirstMove(this);
		gainGround = new StrategyGainGround(this);
	}

	public Building move() {
		/*
		 * verzweigen zwischen
		 * 
		 * first move
		 * gain ground
		 * defend building
		 * conquer building
		 * constrain opponent
		 * fill up
		 */
		Building b;
		
		if(turn == 0) {
			b = firstMove.move();
		} else {
			b = gainGround.move();
		}
		turn++;

		return b;
	}
	
	public boolean end() {
		int hash;
		ArrayList<Building> availableBuildings;
		
		Area a = new Area();
		a.add(game.board.freeArea);
		a.add(player == Definitions.LIGHT ? 
				game.board.claimedAreaLight : game.board.claimedAreaDark);
		
		if(player == Definitions.LIGHT) {
			hash = game.buildingsLight.hashCode();
		} else {
			hash = game.buildingsDark.hashCode();
		}
		
		if(a.equals(availableArea) && buildingsHash == hash) {
			return end;
		}
		
		availableArea = a;
		buildingsHash = hash;
		end = true;

		availableBuildings = player == Definitions.LIGHT ?
				game.buildingsLight : game.buildingsDark;

		if(availableBuildings.isEmpty()) {
			System.out.println("no buildings left");
			return end;
		}
		
		for(Building b : availableBuildings) {
			if(b.getWeigth() > availableArea.cardinality()) continue;
			if(b.findPossiblePosition(availableArea)) {
				end = false;
				return end;
			}
		}
		System.out.println("no possible positions found");
	
		return end;
	}
	
	public void printBuilding(Building b) {
		String[] directions = {"north", "east", "south", "west"};
		char[] xFields = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
		String msg = new String();

		if(b.getPlayer() != Definitions.CATHEDRAL) {
			msg = b.getPlayer() == Definitions.LIGHT ? "light, " : "dark, ";
		}
		msg = msg + b.getName() + ", " + directions[b.getDirection()] + ", "
		          + xFields[b.getPosX()] + (b.getPosY()+1);
		
		System.out.println(msg);
	}
}
