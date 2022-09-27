package cathedral.agent;

import java.util.ArrayList;
import cathedral.common.*;

public class StrategyGainGround implements Strategy {

	private Agent agent;
	
	public StrategyGainGround(Agent agent) {
		this.agent = agent;
	}
	
	public Building move() {
		return findBestMove();
	}

	public Building findBestPosition(Building b) {
		int points, highest = 0;
		int highX = 0, highY = 0, highD = 0;

		for (int x = 0; x < Definitions.SIZE_X; x++){
			for (int y = 0; y < Definitions.SIZE_Y; y++){
				for (int d = 0; d < 4; d++){
					b.setPosition(x, y, d);
					if (b.isValid() && agent.game.board.fits(b)){
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
		if(b.isValid() && agent.game.board.fits(b)) {
			return b;
		}

		return null;
	}	
	
	public Building findBestMove() {
		Building b;
		ArrayList<Building> buildings;
		ArrayList<Building> testetBuildings = new ArrayList<Building>();

		buildings = agent.player == Definitions.LIGHT ?
				agent.game.buildingsLight : agent.game.buildingsDark;
		
		for (int i = 0; i < buildings.size(); i++) {
			if(i>0 && buildings.get(i-1).getType() == buildings.get(i).getType()) continue;
			b = findBestPosition(buildings.get(i));
			if(b != null) {
				//agent.printBuilding(b);
				testetBuildings.add(0, b);
			}
		}
		
		return testetBuildings.get(0);
	}

	//
	public int evaluatePosition(Building b) {
		int points = 0;
		int oldScore = getScore(agent.player, agent.game.board);
		Board simBoard = agent.game.board.clone();
		if (simBoard.addBuilding(b)){
			simBoard.findClaimedAreas(agent.player);
			switch (agent.player) {
			case Definitions.LIGHT:
				points += getScore(Definitions.LIGHT, simBoard) - oldScore;
				if (b.getArea().touches(simBoard.occupiedAreaDark)  && (b.touchesBorder() ||  b.getArea().touches(simBoard.occupiedAreaCathedral)))
					points += 2;
				if (b.getArea().touches(simBoard.occupiedAreaLight))
					points += 1;
				if (!b.getArea().intersects(agent.game.board.claimedAreaLight))
					points += 3;
				break;
	
			case Definitions.DARK:
				points += getScore(Definitions.DARK, simBoard) - oldScore;
				if (b.getArea().touches(simBoard.occupiedAreaLight)  && (b.touchesBorder() ||  b.getArea().touches(simBoard.occupiedAreaCathedral)))
					points += 2;
				if (b.getArea().touches(simBoard.occupiedAreaDark))
					points += 1;
				if (!b.getArea().intersects(agent.game.board.claimedAreaDark))
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

	public int getScore(int player, Board board) {
		if (player == Definitions.LIGHT) {
			return board.occupiedAreaLight.cardinality()
					+ board.claimedAreaLight.cardinality();

		} else {
			return board.occupiedAreaDark.cardinality()
					+ board.claimedAreaDark.cardinality();
		}
	}
}
