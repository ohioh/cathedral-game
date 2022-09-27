package cathedral.common;

import java.util.BitSet;


public class Building extends BitSet {

	static final long serialVersionUID = 0xca75ed1a;
	private String name;
	private int type;
	private int player;
	private int sizeX;
	private int sizeY;
	private int primeSquareX;
	private int primeSquareY;
	private int direction;
	private boolean[][] shape;
	private int posX;
	private int posY;

	//constructor method
	public Building(String n, int t, int p, boolean[][] s, int[] ps) {
		super(Definitions.SIZE_X * Definitions.SIZE_Y);
		name = n;
		type = t;
		player = p;
		shape = s;
		primeSquareX = ps[0];
		primeSquareY = ps[1];
		sizeX = shape.length;
		sizeY = shape[0].length;
		transferShape();
	}

	//getter and setter...
	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public int getPlayer() {
		return player;
	}
	
	public void setPlayer(int player) {
		this.player = player;
	}

	public int getSizeX() {
		return sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public int getPrimeSquareX() {
		return primeSquareX;
	}

	public int getPrimeSquareY() {
		return primeSquareY;
	}

	public int getDirection() {
		return direction;
	}

	public boolean[][] getShape() {
		return shape;
	}

	public Area getArea() {
		return new Area(this);
	}
	
	public int getPosX(){
		return posX;
	}
	
	public int getPosY(){
		return posY;
	}
	
	public int getWeigth(){
		int weight = 0;
		
		for(int x=0; x<sizeX; x++) {
			for(int y=0; y<sizeY; y++) {
				if(shape[x][y]) weight++;
			}
		}

		return weight;
	}

	public void setPosition(int x, int y, int d) {
		boolean  change = !(posX == x & posY == y & direction == d);
		posX = x;
		posY = y;
		direction = d;
		if(change) 
			transferShape();
	}
	
	public void setPosX(int x) {
		boolean change = posX != x;
		posX = x;
		if(change) transferShape();
	}

	public void setPosY(int y) {
		boolean change = posY != y;
		posY = y;
		if(change) transferShape();
	}

	public void setDirection(int d) {
		boolean change = direction != d;
		if(d<4 && d>=0)	direction = d;
		if(change && d<4 && d>=0) transferShape();
	}

	public boolean touches(Building b){
		return this.getArea().touches(b.getArea());
	}
	
	public boolean touchesBorder() {
		return this.getArea().touchesBorder();
	}
	
	public boolean touchesBorder(int direction){
		return this.getArea().touchesBorder(direction);
	}
	
	public boolean findPossiblePosition(Area a) {
		for (int x = 0; x < Definitions.SIZE_X; x++){
			for (int y = 0; y < Definitions.SIZE_Y; y++){
				for (int d = 0; d < 4; d++){
					setPosition(x, y, d);
					if (isValid() && a.fits(getArea())) return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * function isValid
	 * checks if a buildings fits on the board, or if it is partially off the board
	 * @return 
	 * true: the buildings fits at the specified position
	 * false: the buildings does not fit at the specified position
	 */
	public boolean isValid() {
		boolean valid = false;

		switch(direction){
		case Definitions.NORTH:
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
/*		case Definitions.WEST:
			valid = (
				(posX - primeSquareY >= 0) &&
				(posY - primeSquareX >= 0) &&
				(posX + sizeY - primeSquareY <= Definitions.SIZE_X) &&
				(posY + sizeX - primeSquareX <= Definitions.SIZE_Y)
			);
*/		case Definitions.WEST:
			valid = (
				(posX - primeSquareY >= 0) &&
				(posY + primeSquareX >= sizeX - 1) &&
				(posX + sizeY - primeSquareY <= Definitions.SIZE_X) &&
				(posY + primeSquareX < Definitions.SIZE_Y)
			);
		}
		return valid;
	}

	/**
	 * moves and rotates the building
	 * called by the methods setDirection and setPosX, setPosY 
	 */
	private void transferShape() {
		this.clear();
		switch(direction) {
		case Definitions.NORTH:
			for(int x=posX-primeSquareX; x<posX-primeSquareX+sizeX; x++) {
				for(int y=posY-primeSquareY; y<posY-primeSquareY+sizeY; y++) {
					if(0<=x && x<Definitions.SIZE_X &&
							0<=y && y<Definitions.SIZE_Y &&
							shape[x-posX+primeSquareX][y-posY+primeSquareY]
					) {
						this.set(x+y*Definitions.SIZE_X);
					}
				}
			}
		break;
		
		case Definitions.EAST:
			for(int x=posX+primeSquareY-sizeY+1; x<=posX+primeSquareY; x++) {
				for(int y=posY-primeSquareX; y<posY-primeSquareX+sizeX; y++) {
					if(0<=x && x<Definitions.SIZE_X &&
							0<=y && y<Definitions.SIZE_Y &&
							shape[y-posY+primeSquareX][-x+posX+primeSquareY]
					) {
						this.set(x+y*Definitions.SIZE_X);
					}
				}
			}
		break;
		
		case Definitions.SOUTH:
			int xhelp = sizeX-1;
			for(int x=posX+primeSquareX-sizeX+1; x<=posX+primeSquareX; x++) {
				int yhelp = sizeY-1;
				for(int y= posY+primeSquareY-sizeY +1; y<=posY+primeSquareY; y++) {
					if(0<=x && x<Definitions.SIZE_X &&
							0<=y && y<Definitions.SIZE_Y &&
							shape[xhelp][yhelp]
					) {
						this.set(x+y*Definitions.SIZE_X);
					}
					yhelp--;
				}
				xhelp--;
			}
		break;

		/*		case Definitions.WEST:
		for(int x=posX-primeSquareY; x<posX-primeSquareY + sizeY; x++) {
			for(int y=posY-primeSquareX; y<posY-primeSquareX + sizeX; y++) {
				if(0<=x && x<Definitions.SIZE_X &&
						0<=y && y<Definitions.SIZE_Y &&
						shape[y-posY+primeSquareX][x-posX+primeSquareY]
				) {
					this.set(x+y*Definitions.SIZE_X);
				}
			}
		}
	break;
*/		
		case Definitions.WEST:
			for(int x=posX-primeSquareY; x<posX-primeSquareY + sizeY; x++) {
				for(int y=posY+primeSquareX-sizeX+1; y<=posY+primeSquareX; y++) {
					if(0<=x && x<Definitions.SIZE_X && 0<=y && y<Definitions.SIZE_Y) {
						this.set(x+y*Definitions.SIZE_X, shape[-y+posY+primeSquareX][x-posX+primeSquareY]);
					}
				}
			}
		break;
		
		}
	}

	
}
