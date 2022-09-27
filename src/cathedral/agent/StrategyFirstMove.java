package cathedral.agent;

import java.util.ArrayList;

import cathedral.common.*;

public class StrategyFirstMove  implements Strategy {

	private Agent agent;
	
	public StrategyFirstMove(Agent agent) {
		this.agent = agent;
	}
	
	public Building move() {
		ArrayList<Building> buildings = agent.player == Definitions.LIGHT ?
				agent.game.buildingsLight : agent.game.buildingsDark;

		
		for(Building b : buildings) {
			if(b.getType() == Definitions.CATHEDRAL) return randomMove(b);
		}
		
		Strategy gainGround = new StrategyGainGround(agent);
		return gainGround.move();
	}

	public Building randomMove(Building b) {
		int x, y, direction;
		x = (int) (Math.random() * (Definitions.SIZE_X - 6)) + 3;
		y = (int) (Math.random() * (Definitions.SIZE_Y - 6)) + 3;
		direction = (int) (Math.random() * 4);

/*		switch(direction){
		case Definitions.NORTH:
			x = (int) (Math.random() * Definitions.SIZE_X - b.getSizeX()) + b.getPrimeSquareX() )
			valid = (
				(posX - primeSquareX >= 0) &&
				(posY - primeSquareY >= 0) &&
				(posX + sizeX - primeSquareX <= Definitions.SIZE_X) &&
				(posY + sizeY - primeSquareY <= Definitions.SIZE_Y)
			);
			break;
		case Definitions.EAST:
			valid = (
				(posX + primeSquareY >= sizeY -1) &&
				(posY - primeSquareX >= 0) &&
				(posX + primeSquareY < Definitions.SIZE_X) &&
				(posY - primeSquareX + sizeX <= Definitions.SIZE_Y)
			);
			break;
		case Definitions.SOUTH:
			valid = (
				(posX + primeSquareX < Definitions.SIZE_X) &&
				(posY + primeSquareY < Definitions.SIZE_Y) &&
				(posX + primeSquareX - sizeX + 1 >= 0) &&
				(posY - primeSquareY >= sizeY -1 )
			);
			break;
		case Definitions.WEST:
			valid = (
				(posX - primeSquareY >= 0) &&
				(posY + primeSquareX >= sizeX - 1) &&
				(posX + sizeY - primeSquareY <= Definitions.SIZE_X) &&
				(posY + primeSquareX < Definitions.SIZE_Y)
			);
		}
*/
		b.setPosition(x, y, direction);

		return b;
	}

}
