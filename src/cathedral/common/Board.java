package cathedral.common;

import java.util.ArrayList;

public class Board {

	private Game game;
	private ArrayList<Building> buildings;
	public Area freeArea;
	public Area occupiedAreaCathedral;
	public Area occupiedAreaLight;
	public Area occupiedAreaDark;
	public Area claimedAreaLight;
	public Area claimedAreaDark;

	//constructor method
	public Board(Game game) {
		this();
		this.game = game;
	}
		
	public Board() {
		buildings = new ArrayList<Building>();
		freeArea = new Area();
		freeArea.set(0, Definitions.SIZE_Y * Definitions.SIZE_Y);
		occupiedAreaCathedral = new Area();
		occupiedAreaLight = new Area();
		occupiedAreaDark = new Area();
		claimedAreaLight = new Area();
		claimedAreaDark = new Area();
	}
	

	public Board clone(){
		Board newBoard = new Board(null);
		for(Building b : buildings) newBoard.addBuilding(b);
		newBoard.freeArea = (Area) this.freeArea.clone();
		newBoard.occupiedAreaCathedral = (Area) this.occupiedAreaCathedral.clone();
		newBoard.occupiedAreaLight = (Area) this.occupiedAreaLight.clone();
		newBoard.occupiedAreaDark = (Area) this.occupiedAreaDark.clone();
		newBoard.claimedAreaLight = (Area) this.claimedAreaLight.clone();
		newBoard.claimedAreaDark = (Area) this.claimedAreaDark.clone();
		
		return newBoard;
	}
	
	public boolean addBuilding(Building b) {
		Area bArea = b.getArea();
		if(b.isValid()) {
			buildings.add(b);
			switch(b.getPlayer()) {
			case Definitions.CATHEDRAL:
				if (freeArea.fits(bArea)){
					occupiedAreaCathedral.add(bArea);
					freeArea.andNot(bArea);
					return true;
				}
				break;
				
			case Definitions.LIGHT:
				if (freeArea.fits(bArea) || claimedAreaLight.fits(bArea)){
					occupiedAreaLight.add(bArea);
					if (freeArea.fits(bArea)) {
						freeArea.andNot(bArea);
						findClaimedAreas(Definitions.LIGHT);
					}
					if (claimedAreaLight.fits(bArea))
						claimedAreaLight.andNot(bArea);
					return true;
				}
				break;
				
			case Definitions.DARK:
				if (freeArea.fits(bArea) || claimedAreaDark.fits(bArea)){
					occupiedAreaDark.add(bArea);
					if (freeArea.fits(bArea)) {
						freeArea.andNot(bArea);
						findClaimedAreas(Definitions.DARK);
					}
					if (claimedAreaDark.fits(bArea))
						claimedAreaDark.andNot(bArea);
					return true;
				}
				break;		
			}	
		}
		return false;
	}

	public boolean fits(Building b) {
		Area bArea = b.getArea();
		if(b.isValid()) {
			switch(b.getPlayer()) {
			case Definitions.CATHEDRAL:
				return freeArea.fits(bArea);

			case Definitions.LIGHT:
				return (freeArea.fits(bArea) || claimedAreaLight.fits(bArea));
//						&& !occupiedAreaLight.intersects(bArea) && !occupiedAreaDark.intersects(bArea);
				
			case Definitions.DARK:
				return (freeArea.fits(bArea) || claimedAreaDark.fits(bArea));
//						&& !occupiedAreaLight.intersects(bArea) && !occupiedAreaDark.intersects(bArea);
			}	
		}
		return false;
		
	}
	
	public boolean removeBuilding(Building b) {
		Area bArea = b.getArea();
		if(buildings.contains(b)) {
			buildings.remove(b);

			switch(b.getPlayer()) {
			case Definitions.CATHEDRAL:
				occupiedAreaCathedral.remove(bArea);
				break;
			case Definitions.LIGHT:
				occupiedAreaLight.remove(bArea);
				claimedAreaLight.andNot(bArea);
				break;
			case Definitions.DARK:
				occupiedAreaDark.remove(bArea);
				claimedAreaDark.andNot(bArea);
				break;		
			}
			freeArea.add(bArea);
			return true;
		}
		return false;
	}
	
	
	public int getBuilding (int x, int y){
		for (int i = 0; i < buildings.size(); i++){
			if(buildings.get(i).getArea().isSet(x,y)){
				return buildings.get(i).getType();
			}
		}
		return 0;
	}
	
	public Building getIntersectingBuilding (Area a, int player){
		for (int i = 0; i < buildings.size(); i++){
			if(buildings.get(i).getArea().intersects(a) &&
			  (buildings.get(i).getPlayer() == player ||
			   buildings.get(i).getPlayer() == Definitions.CATHEDRAL)
			   ){
				return buildings.get(i);
			}
		}
		return null;
	}
	
	//counts intersecting buildings that are the ones of the specified player
	// PLUS THE CATHEDRAL (if intersecting)!!!!!!!!!!!!!!!
	public int countIntersectingBuildings (Area a, int player){
		int  count = 0;
		for (int i = 0; i < buildings.size(); i++){
			if(buildings.get(i).getArea().intersects(a) &&
			  (buildings.get(i).getPlayer() == player ||
			   buildings.get(i).getPlayer() == Definitions.CATHEDRAL)
			   ){
				count++;
			}
		}

		return count;
	}

	 
	//code duplication here in player-switch
	public void findClaimedAreas(int player){
		Area occupiedAreaMine, occupiedAreaOpponent;
		Area claimedAreaMine, claimedAreaOpponent, floodedArea;
		int opponent;
		
		switch (player) {
		default:
		case Definitions.LIGHT:
			opponent = Definitions.DARK;
			occupiedAreaMine = occupiedAreaLight;
			occupiedAreaOpponent = occupiedAreaDark;
			claimedAreaMine = claimedAreaLight;
			claimedAreaOpponent = claimedAreaDark;
			break;
		case Definitions.DARK:
			opponent = Definitions.LIGHT;
			occupiedAreaMine = occupiedAreaDark;
			occupiedAreaOpponent = occupiedAreaLight;
			claimedAreaMine = claimedAreaDark;
			claimedAreaOpponent = claimedAreaLight;
		}

		if (buildings.size() >= 3){
			for (int x = 0; x < Definitions.SIZE_X; x++){
				for (int y = 0; y < Definitions.SIZE_Y; y++){
						
					if(!freeArea.get(x+y*Definitions.SIZE_Y) && 
							!occupiedAreaOpponent.get(x+y*Definitions.SIZE_Y)) continue;
					floodedArea = new Area();
					floodedArea.floodArea(x, y, occupiedAreaMine);

					if (countIntersectingBuildings(floodedArea, opponent) == 1) {
						conquerBuilding(floodedArea, player);
						floodedArea.floodArea(x, y, occupiedAreaMine);
					}

					if (!floodedArea.touches(occupiedAreaOpponent) &&
							!floodedArea.touches(occupiedAreaCathedral) &&
							!floodedArea.intersects(occupiedAreaOpponent) &&
							!floodedArea.intersects(occupiedAreaCathedral)
					){
						claimedAreaMine.add(floodedArea);
						claimedAreaOpponent.andNot(floodedArea);
						freeArea.andNot(floodedArea);
					}	
				}				
			}
		}
	}
	
	// conquer, yeah!
	public void conquerBuilding(Area floodedArea, int player) {
		Building conqueredBuilding = null;
		conqueredBuilding = getIntersectingBuilding(floodedArea, player == Definitions.LIGHT ?
				Definitions.DARK : Definitions.LIGHT);

		if (game != null) {
			game.unsetBuilding(conqueredBuilding);
		} else {
			removeBuilding(conqueredBuilding);
		}
	}
	
	public void printArea(Area a) {
		 for (int y = 0; y < Definitions.SIZE_Y; y++) {
				for (int x = 0; x < Definitions.SIZE_X; x++) {
					System.out.print(a.get(x+y*Definitions.SIZE_Y) ? "+" : "-");
				}
				System.out.println("");
		 }

	}

}

