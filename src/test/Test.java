package test;

import java.util.ArrayList;

import cathedral.common.*;
import cathedral.agent.*;


public class Test {
	public int turn;
	public Board board;
	public static ArrayList<Building> buildingsLight;
	public static ArrayList<Building> buildingsDark;

	public static void main(String[] args) {
		Board board = new Board();
		int turn = 0;
		
		buildingsLight = new ArrayList<Building>();
		for(int b : Definitions.BUILDINGS_LIGHT) {
			buildingsLight.add(new Building(
					Definitions.BUILDING_NAME[b],
					b, Definitions.LIGHT,
					Definitions.BUILDING_SHAPE[b],
					Definitions.BUILDING_PRIME_SQUARE[b]
			));		
		}
	
		buildingsDark = new ArrayList<Building>();
		for(int b : Definitions.BUILDINGS_DARK) {
			buildingsDark.add(new Building(
					Definitions.BUILDING_NAME[b],
					b, Definitions.DARK,
					Definitions.BUILDING_SHAPE[b],
					Definitions.BUILDING_PRIME_SQUARE[b]
			));		
		}
	
		Building cathedral = new Building(
				Definitions.BUILDING_NAME[Definitions.CATHEDRAL],
				Definitions.CATHEDRAL, Definitions.CATHEDRAL,
				Definitions.BUILDING_SHAPE[Definitions.CATHEDRAL],
				Definitions.BUILDING_PRIME_SQUARE[Definitions.CATHEDRAL]
		);
		

/*		AIPlayer p2 = new AIPlayer(board, Definitions.DARK, buildingsDark, game);
		APlayer p1 = new APlayer(board, Definitions.LIGHT, buildingsLight);
		
		while(!p1.makeRandomMove(cathedral, board));
		
		for (int i = 0; i < 28 ; i++){
			 if (turn%2 == 1){
				p1.setBuilding(p1.getConsoleInput());
				System.out.println("player1: " + board.getScore(Definitions.LIGHT));
			 }else{
				p2.setBuilding(p2.findBestMove());
				System.out.println("player2: " +board.getScore(Definitions.DARK));
			 }
			 turn++;
			 for (int y = 0; y < Definitions.SIZE_Y; y++) {
					for (int x = 0; x < Definitions.SIZE_X; x++) {
						System.out.print(board.freeArea.get(x+y*Definitions.SIZE_Y)? "_ " : board.occupiedAreaDark.get(x+y*Definitions.SIZE_Y) ? "D " : board.occupiedAreaLight.get(x+y*Definitions.SIZE_Y) ? "L " : "+ ");
					}
					System.out.println("");
			 }
		}
		System.out.println("player1: " + board.getScore(Definitions.LIGHT) + " player2: " +board.getScore(Definitions.DARK));
*/	}
}
