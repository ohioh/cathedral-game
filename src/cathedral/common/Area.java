package cathedral.common;

import java.util.*;

public class Area extends BitSet {

	static final long serialVersionUID = 0xca75ed2a;
	private ArrayList<Area> subareas;

	public Area() {
		super(Definitions.SIZE_X * Definitions.SIZE_Y);
		subareas = new ArrayList<Area>();
	}

	public Area(BitSet b) {
		this();
		this.or(b);
	}

	public Area(Building b) {
		this((BitSet) b);
	}

	public Area(Area a) {
		this((BitSet) a);
	}

	public boolean isSet(int x, int y) {
		if (x >= 0 && x < Definitions.SIZE_X && y >= 0
				&& y < Definitions.SIZE_Y) {
			return get(x + y * Definitions.SIZE_X);
		}
		return false;
	}

	/**
	 * adds an area to this area by performing a logical OR on the bit sets of
	 * the areas
	 * 
	 * @param a
	 *            - the area to be added
	 * 
	 */
	public void add(Area a) {
		subareas.add(a);
		this.or(a);
	}

	/**
	 * removes an area from this area by performing a logical and not on the bit
	 * sets of the areas
	 * 
	 * @param a
	 *            - the area to be removed
	 * @return true, if the area was contained in the ArrayList of subareas
	 *         false, if it was not contained in the ArrayList of subareas
	 */
	public boolean remove(Area a) {
		if (subareas.remove(a)) {
			this.andNot(a);
			return true;
		}
		return false;
	}

	/**
	 * @param a
	 * @return
	 */
	public boolean fits(Area a) {
		BitSet temp = (BitSet) this.clone();
		temp.flip(0, Definitions.SIZE_X * Definitions.SIZE_Y);

		return !temp.intersects(a);
	}

	/**
	 * @param a
	 * @param invert
	 * @return
	 */
	public boolean fits(Area a, boolean invert) {
		if (invert) {
			return !this.intersects(a);
		}

		return this.fits(a);
	}

	public boolean touches(Area a) {
		boolean touches = false;

		for (int i = a.nextSetBit(0); i >= 0 && !touches; i = a
				.nextSetBit(i + 1)) {
			if (i >= Definitions.SIZE_X)
				touches |= this.get(i - Definitions.SIZE_X); // oben
			if ((i + 1) % Definitions.SIZE_X != 0)
				touches |= this.get(i + 1); // rechts
			if (i < Definitions.SIZE_X * (Definitions.SIZE_Y - 1))
				touches |= this.get(i + Definitions.SIZE_X); // unten
			if (i % Definitions.SIZE_X != 0)
				touches |= this.get(i - 1); // links
		}

		return touches;
	}

	/**
	 * @return
	 */
	public boolean touchesBorder() {
		return this.touchesBorder(Definitions.NORTH)
				|| this.touchesBorder(Definitions.EAST)
				|| this.touchesBorder(Definitions.SOUTH)
				|| this.touchesBorder(Definitions.WEST);
	}

	/**
	 * @param direction
	 * @return
	 */
	public boolean touchesBorder(int direction) {
		boolean touches = false;

		switch (direction) {
		case Definitions.NORTH:
			touches = !(this.get(0, Definitions.SIZE_X).cardinality() == 0);
			break;
		case Definitions.EAST:
			for (int i = Definitions.SIZE_X - 1; i < Definitions.SIZE_X
					* Definitions.SIZE_Y
					&& !touches; i += Definitions.SIZE_X) {
				touches = this.get(i);
			}
			break;
		case Definitions.SOUTH:
			touches = !(this.get(Definitions.SIZE_X * (Definitions.SIZE_Y - 1),
					Definitions.SIZE_X * Definitions.SIZE_Y).cardinality() == 0);
			break;
		case Definitions.WEST:
			for (int i = 0; i < Definitions.SIZE_X * Definitions.SIZE_Y
					&& !touches; i += Definitions.SIZE_X) {
				touches = this.get(i);
			}
			break;
		}

		return touches;
	}

	public void floodArea(int x, int y, Area other) {
		if (x >= 0 && x < Definitions.SIZE_X && y >= 0
				&& y < Definitions.SIZE_Y
				&& !this.get(x + y * Definitions.SIZE_Y)
				&& !other.get(x + y * Definitions.SIZE_Y)) {

			this.set(x + y * Definitions.SIZE_Y);
			floodArea(x, y - 1, other);
			floodArea(x, y + 1, other);
			floodArea(x - 1, y, other);
			floodArea(x + 1, y, other);
			floodArea(x - 1, y - 1, other);
			floodArea(x - 1, y + 1, other);
			floodArea(x + 1, y - 1, other);
			floodArea(x + 1, y + 1, other);
		}
	}
	
	public void floodArea(int x, int y) {
		if (x >= 0 && x < Definitions.SIZE_X &&
				y >= 0 && y < Definitions.SIZE_Y
				&& !this.get(x + y * Definitions.SIZE_Y)) {

			this.set(x + y * Definitions.SIZE_Y);
			floodArea(x, y - 1);
			floodArea(x, y + 1);
			floodArea(x - 1, y);
			floodArea(x + 1, y);
			floodArea(x - 1, y - 1);
			floodArea(x - 1, y + 1);
			floodArea(x + 1, y - 1);
			floodArea(x + 1, y + 1);
		}
	}
}
