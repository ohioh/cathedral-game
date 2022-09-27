package cathedral.agent;

import cathedral.common.*;

import java.lang.Math;
import java.util.ArrayList;

public class AIPlayer implements Player {

	protected int colour;
	protected Board board;
	protected Game game;
	protected ArrayList<Building> buildings;
	private int buildingsHash;
	private Area availableArea;
	private boolean end;

	public AIPlayer(Board b, int c,  ArrayList<Building> buildings, Game game) {
		this.board = b;
		this.game = game;
		this.colour = c;
		this.buildings = buildings;
	}

	
	public Building move() {
		game.printBuildings(colour);
		return findBestMove();
	}
	
	public boolean end() {
		int hash;
		ArrayList<Building> availableBuildings;
		
		Area a = new Area();
		a.add(board.freeArea);
		a.add(colour == Definitions.LIGHT ? 
				board.claimedAreaLight : board.claimedAreaDark);
		
		if(colour == Definitions.LIGHT) {
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

		availableBuildings = colour == Definitions.LIGHT ?
				game.buildingsLight : game.buildingsDark;

		if(availableBuildings.isEmpty()) {
			System.out.println("no buildings left");
			return end;
		}
		
		for(Building b : availableBuildings) {
			if(b.getWeigth() > availableArea.cardinality()) continue;
			if(b.findPossiblePosition(availableArea)) {
				System.out.println(availableArea.cardinality()+ ""+ b.getName() +" "+ 
						b.getPosX() +" "+b.getPosY() +" "+b.getDirection());
				end = false;
				return end;
			}
		}
	
		return end;
	}

	
	public boolean makeRandomMove(Building b, Board board) {
		int x, y, d;
		x = (int) (Math.random() * Definitions.SIZE_X);
		y = (int) (Math.random() * Definitions.SIZE_Y);
		d = (int) (Math.random() * 4);
		b.setPosition(x, y, d);
		return board.addBuilding(b);
	}
	
	public Building findBestPosition(Building b) {
		int points, highest = 0;
		int highX = 0, highY = 0, highD = 0;
		for (int x = 0; x < Definitions.SIZE_X; x++){
			for (int y = 0; y < Definitions.SIZE_Y; y++){
				for (int d = 0; d < 4; d++){
					b.setPosition(x, y, d);
					if (board.fits(b)){
						points = evaluatePosition(b);
						if (points >= highest){
							highest = points;
							highX = x;
							highY = y;
							highD = d;
						}
					}
				}
			}
		}
		b.setPosition(highX, highY, highD);
		return b;
	}	
	
	//never used
	/*
	public Building findGoodRandomPosition() {
		ArrayList<Building> testetBuildings = new ArrayList<Building>();
		int highX = 0, highY = 0, highD = 0;
		Building b = null;
		int points = 0, highest = 0;
		for (int i = 0; i < buildings.size(); i++) {
			int count = 0;
			b = buildings.get(i);
			while(count < 100){
				setRandomPos(b);
				count++;
				points = evaluatePosition(b);
				if (points >= highest && points > 0){
					highest = points;
					highX = b.getPosX();
					highY = b.getPosY();
					highD = b.getDirection();
					testetBuildings.add(0,b);
				}
			}
		}
		
			b = testetBuildings.get(0);
			b.setPosition(highX, highY, highD);
		
		return b;
	}
	*/
	
	public Building findBestMove() {
		ArrayList<Building> testetBuildings = new ArrayList<Building>();

		for (int i = 0; i < buildings.size(); i++) {
			testetBuildings.add(0,findBestPosition(buildings.get(i)));
		}
		return testetBuildings.get(0);
	}

	//
	public int evaluatePosition(Building b) {
		int points = 0;
		int oldScore = getScore(colour, board);
		Board simBoard = board.clone();
		if (simBoard.addBuilding(b)){
			simBoard.findClaimedAreas(colour);
			switch (colour) {
			case Definitions.LIGHT:
				points += getScore(Definitions.LIGHT, simBoard) - oldScore;
//				if (b.getArea().touches(simBoard.occupiedAreaDark)  && (b.touchesBorder() ||  b.getArea().touches(simBoard.occupiedAreaCathedral)))
//					points += 2;
//				if (b.getArea().touches(simBoard.occupiedAreaLight))
//					points += 1;
				if (!b.getArea().intersects(board.claimedAreaLight))
					points += 3;
				break;
	
			case Definitions.DARK:
				points += getScore(Definitions.DARK, simBoard) - oldScore;
//				if (b.getArea().touches(simBoard.occupiedAreaLight)  && (b.touchesBorder() ||  b.getArea().touches(simBoard.occupiedAreaCathedral)))
//					points += 2;
//				if (b.getArea().touches(simBoard.occupiedAreaDark))
//					points += 1;
				if (!b.getArea().intersects(board.claimedAreaDark))
					points += 3;
			}
//			if (b.touchesBorder())
//				points += 1;
	
			if (b.getArea().touches(simBoard.occupiedAreaCathedral))
				points += 1;
			simBoard.removeBuilding(b);
		}
		return points;
	}

	private boolean buildingNearCathedral(Building b){
		return false;
	}
	
	private boolean buildingSourroundable(Building b){
		return false;
	}

	public int getScore(int player, Board board) {
		if (player == Definitions.LIGHT) {
			return board.occupiedAreaLight.cardinality()
					+ board.claimedAreaLight.cardinality();

		} else {
			return board.occupiedAreaDark.cardinality()
					+ board.claimedAreaDark.cardinality();
		}
	}
	
	public boolean setBuilding(Building b) {
		System.out.println("player " + colour + " " + b.getName() + " " + b.getPosX() + ";" + b.getPosY()
				+ ";" + b.getDirection());
		return (board.addBuilding(b) && buildings.remove(b));
	}

	public void setRandomPos(Building b) {
		int x, y, d;
		x = (int) (Math.random() * Definitions.SIZE_X);
		y = (int) (Math.random() * Definitions.SIZE_Y);
		d = (int) (Math.random() * 4);
		b.setPosition(x, y, d);
	}
}
