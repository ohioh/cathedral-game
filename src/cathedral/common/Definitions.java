package cathedral.common;

public interface Definitions {

	public static final int LIGHT = 1;
	public static final int DARK = 2;

	public static final int HUMAN = 0;
	public static final int ARTIFICIAL = 1;

	public static final int LIGHT_AGENT = ARTIFICIAL;
	public static final int DARK_AGENT = HUMAN;
//	public static final int LIGHT_AGENT = HUMAN;
//	public static final int DARK_AGENT = ARTIFICIAL;
	public static final int FIRST_TURN = LIGHT;
	
	public static final int SIZE_X = 10;
	public static final int SIZE_Y = 10;
	
	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;

	public static final int CATHEDRAL = 0;
	public static final int TAVERN = 1;
	public static final int STABLE = 2;
	public static final int INN = 3;
	public static final int BRIDGE = 4;
	public static final int SQUARE = 5;
	public static final int ABBEYL = 6;
	public static final int ABBEYD = 7;
	public static final int MANOR = 8;
	public static final int TOWER = 9;
	public static final int INFIRMARY = 10;
	public static final int CASTLE = 11;
	public static final int ACADEMYL = 12;
	public static final int ACADEMYD = 13; //academy, dark added
	
	public static final String[] BUILDING_NAME = {
		"catheral",
		"tavern",
		"stable",
		"inn",
	 	"bridge",
	 	"square",
	 	"abbey",
	 	"abbey",
	 	"manor",
	 	"tower",
	 	"infirmary",
	 	"castle",
	 	"academy",
	 	"academy"
	};

	//shapes of all buildings represented as boolean arrays
	//@ Jan: please note that I added the academydark! 
	public static final boolean[][][] BUILDING_SHAPE = {
		{{false,true,false,false}, {true,true,true,true}, {false,true,false,false}}, 	//cathedral
		{{true}},																		//tavern
		{{true,true}},																	//stable
		{{true,true}, {false,true}},													//inn
		{{true,true,true}},																//bridge
		{{true,true}, {true,true}},														//square
		{{true,true,false}, {false,true,true}},											//abbey, light
		{{false,true,true}, {true,true,false}},											//abbey, dark
		{{false,true}, {true,true}, {false,true}},										//manor
		{{false,false,true}, {false,true,true}, {true,true,false}},						//tower
		{{false,true,false}, {true,true,true}, {false,true,false}},						//infirmary
		{{true,true,true}, {true,false,true}},											//castle
		{{false,true,false}, {true,true,true}, {false,false,true}},						//academy, light
		{{false,false,true}, {true,true,true}, {false,true,false}}						//academy, dark
	};
	
	
	//@Jan: You used the prime squares as displayed on the website?
	//I made changes according to the above arrays 
	public static final int[][] BUILDING_PRIME_SQUARE = {
		{1,0},													//cathedral
		{0,0},													//tavern
		{0,0},													//stable
		{0,0},													//inn
		{0,0},													//bridge
		{0,0},													//square
		{0,0},													//abbey, light
		{1,0},													//abbey, dark
		{1,0},													//manor
		{2,0},													//tower
		{1,1},													//infirmary
		{1,0},													//castle
		{1,0},													//academy, light
		{1,0}													//academy, dark
	};

	//changed academy to academyl
	public static final int[] BUILDINGS_LIGHT = {
		Definitions.TAVERN,
		Definitions.TAVERN,
		Definitions.STABLE,
		Definitions.STABLE,
		Definitions.INN,
		Definitions.INN,
		Definitions.BRIDGE,
		Definitions.SQUARE,
		Definitions.ABBEYL,
		Definitions.MANOR,
		Definitions.TOWER,
		Definitions.INFIRMARY,
		Definitions.CASTLE,
		Definitions.ACADEMYL
	};

	//changed academy to academyd
	public static final int[] BUILDINGS_DARK = {
		Definitions.TAVERN,
		Definitions.TAVERN,
		Definitions.STABLE,
		Definitions.STABLE,
		Definitions.INN,
		Definitions.INN,
		Definitions.BRIDGE,
		Definitions.SQUARE,
		Definitions.ABBEYD,
		Definitions.MANOR,
		Definitions.TOWER,
		Definitions.INFIRMARY,
		Definitions.CASTLE,
		Definitions.ACADEMYD
	};
}
