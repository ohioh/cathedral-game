package cathedral.agent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import cathedral.common.*;

public class APlayer {
	protected int colour;
	protected Board board;
	protected ArrayList<Building> buildings;

	public APlayer(Board b, int c, ArrayList<Building> buildings) {
		this.board = b;
		this.colour = c;
		this.buildings = buildings;
	}


	public void returnBuilding(Building b){
		buildings.add(b);
	}
	
	public boolean setBuilding(Building b) {
		System.out.println("player " + colour + " " + b.getName() + " " + b.getPosX() + ";" + b.getPosY()
				+ ";" + b.getDirection());
		return (board.addBuilding(b) && buildings.remove(b));
	}

	public boolean setBuilding(Building b, int x, int y, int d) {
		b.setPosition(x, y, d);
		return setBuilding(b);
	}

	public Building getConsoleInput() {
		Building b = null;
		String name = null;
		int x = 0, y = 0, d = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		do{
			System.out.println("Enter your Move (Building ENTER x ENTER y ENTER d ENTER):");
			try {
				name = br.readLine();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				x = Integer.parseInt(br.readLine());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				y = Integer.parseInt(br.readLine());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				d = Integer.parseInt(br.readLine());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < buildings.size(); i++) {
				if (buildings.get(i).getName().equals(name)) {
					b = buildings.get(i);
					b.setPosition(x, y, d);
				}
	
			}
		}while(b == null && !b.getArea().fits(board.freeArea) && !(b.getArea().fits(board.claimedAreaDark) || b.getArea().fits(board.claimedAreaLight)));
		return b;
	}

	public boolean makeRandomMove(Building b, Board board) {
		int x, y, d;
		x = (int) (Math.random() * Definitions.SIZE_X);
		y = (int) (Math.random() * Definitions.SIZE_Y);
		d = (int) (Math.random() * 4);
		b.setPosition(x, y, d);
		return board.addBuilding(b);
	}
	
	public int getColour() {
		return colour;
	}

}
