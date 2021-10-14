package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class used to generate the world
 */
public class World implements Serializable {

    private long seed;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private TETile[][] world;
    private Random random;
    private Boolean[][] taken;
    private Boolean[][] floorspace;
    private ArrayList<Room> rooms;


    public World(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        this.world = new TETile[WIDTH][HEIGHT];
        this.taken = new Boolean[WIDTH][HEIGHT];

        this.floorspace = new Boolean[WIDTH][HEIGHT];
        this.rooms = new ArrayList<Room>();

    }


    public void renderWorld() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        generateWorld();
        ter.renderFrame(world);
    }


    public TETile[][] generateWorld() {
        /** Creating world. */
        initializeTaken();
        initializeFloorSpace();
        fillWithNothingTiles(this.world);
        createRandomAmountOfRooms(this.world);
        connectRooms(this.world);
        prepareWorld(this.world);
        placeLightSwitch(this.world);
        return this.world;
    }




    public void initializeTaken() {
        int height = taken[0].length;
        int width = taken.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                taken[x][y] = false;
            }
        }
    }

    public void initializeFloorSpace() {
        int height = floorspace[0].length;
        int width = floorspace.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                floorspace[x][y] = false;
            }
        }
    }

    public void fillWithNothingTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }


    public Random getRandom() {
        return random;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }


    /**
     * BUILDING BLOCKS FOR STRUCTURES.
     */

    private void verticalRow(Integer xPos, Integer yPos, Integer length,
                             TETile[][] tileArray, TETile tileType) {
        for (int y = yPos; y < yPos + length; y++) {
            if (tileType.equals(Tileset.FLOOR)) {
                floorspace[xPos][y] = true;
            }
            tileArray[xPos][y] = tileType;
            taken[xPos][y] = true;
        }
    }

    private void verticalRowForWalls(Integer xPos, Integer yPos, Integer length,
                                     TETile[][] tileArray, TETile tileType) {
        for (int y = yPos; y < yPos + length; y++) {
            if (!floorspace[xPos][y]) {
                tileArray[xPos][y] = tileType;
                taken[xPos][y] = true;
            }
        }
    }

    private void horizontalRow(Integer xPos, Integer yPos, Integer length,
                               TETile[][] tileArray, TETile tileType) {
        for (int x = xPos; x < xPos + length; x++) {
            if (tileType.equals(Tileset.FLOOR)) {
                floorspace[x][yPos] = true;
            }
            tileArray[x][yPos] = tileType;
            taken[x][yPos] = true;
        }
    }

    private void horizontalRowForWalls(Integer xPos, Integer yPos, Integer length,
                                       TETile[][] tileArray, TETile tileType) {
        for (int x = xPos; x < xPos + length; x++) {
            if (!floorspace[x][yPos]) {
                tileArray[x][yPos] = tileType;
                taken[x][yPos] = true;
            }
        }
    }

    /**
     * STRUCTURES OF THE WORLD.
     */

    // given position will be BOTTOM of hallway (floor)
    public void createVerticalHallway(Position p, int length, TETile[][] tileArray) {
        verticalRow(p.x - 1, p.y, length, tileArray, Tileset.WALL);
        verticalRow(p.x, p.y, length, tileArray, Tileset.FLOOR);
        verticalRow(p.x + 1, p.y, length, tileArray, Tileset.WALL);
    }

    public void createVerticalHallwayWalls(Position p, int length, TETile[][] tileArray) {
        verticalRowForWalls(p.x - 1, p.y, length, tileArray, Tileset.WALL);
        verticalRowForWalls(p.x + 1, p.y, length, tileArray, Tileset.WALL);
    }

    public void createVerticalHallwayFloor(Position p, int length, TETile[][] tileArray) {
        verticalRow(p.x, p.y, length, tileArray, Tileset.FLOOR);
    }


    // given position will be LEFT of hallway (floor)
    public void createHorizontalHallway(Position p, int length, TETile[][] tileArray) {
        horizontalRow(p.x, p.y - 1, length, tileArray, Tileset.WALL);
        horizontalRow(p.x, p.y, length, tileArray, Tileset.FLOOR);
        horizontalRow(p.x, p.y + 1, length, tileArray, Tileset.WALL);
    }


    public void createHorizontalHallwayWalls(Position p, int length, TETile[][] tileArray) {
        horizontalRowForWalls(p.x, p.y - 1, length, tileArray, Tileset.WALL);
        horizontalRowForWalls(p.x, p.y + 1, length, tileArray, Tileset.WALL);
    }

    public void createHorizontalHallwayFloor(Position p, int length, TETile[][] tileArray) {
        horizontalRow(p.x, p.y, length, tileArray, Tileset.FLOOR);
    }


    /** Creates a room of random size at the given position **/
    public void createRoom(Position p, TETile[][] tileArray, int iD) {
        int width = RandomUtils.uniform(random,  5, 12);
        int height = RandomUtils.uniform(random, 5, 12);
        if (!doesNotOverlap(p, width, height, tileArray)) {
            return;
        }
        horizontalRow(p.x, p.y, width, tileArray, Tileset.WALL);
        horizontalRow(p.x, p.y + height - 1, width, tileArray, Tileset.WALL);
        verticalRow(p.x, p.y, height, tileArray, Tileset.WALL);
        verticalRow(p.x + width - 1, p.y, height, tileArray, Tileset.WALL);
        Position innerStart = new Position(p.x + 1, p.y + 1);
        for (int i = 0; i < height - 2; i++) {
            horizontalRow(innerStart.x, innerStart.y + i, width - 2, tileArray, Tileset.FLOOR);
        }
        Room thisRoom = new Room(width, height, p, iD);
        rooms.add(thisRoom);

    }


    /** Check if the room would overlap with an already created room **/
    public Boolean doesNotOverlap(Position p, int width, int height, TETile[][] tileArray) {
        Boolean overlapOccurs = true;
        if ((checkRowOverlap(p.x, p.y, width, tileArray))
                || (checkRowOverlap(p.x, p.y + height - 1, width, tileArray))) {
            overlapOccurs = false;
        } else if ((checkColumnOverlap(p.x, p.y, height, tileArray))
                || (checkColumnOverlap(p.x + width - 1, p.y, height, tileArray))) {
            overlapOccurs = false;
        }
        Position innerStart = new Position(p.x + 1, p.y + 1);
        for (int i = 0; i < height - 2; i++) {
            if (checkRowOverlap(innerStart.x, innerStart.y + i, width - 2, tileArray)) {
                overlapOccurs = false;
            }
        }
        return overlapOccurs;
    }

    public Boolean checkRowOverlap(int xPos, int yPos, int length, TETile[][] tileArray) {
        for (int i = xPos; i < xPos + length; i++) {
            if (taken[i][yPos]) {
                return true;
            }
        }
        return false;
    }

    public Boolean checkColumnOverlap(int xPos, int yPos, int length, TETile[][] tileArray) {
        for (int i = yPos; i < yPos + length; i++) {
            if (taken[xPos][i]) {
                return true;
            }
        }
        return false;
    }

    public Position createRandomPosition() {
        int xCord = RandomUtils.uniform(random, 0, WIDTH - 11);
        int yCord = RandomUtils.uniform(random, 0, HEIGHT - 11);
        return new Position(xCord, yCord);
    }


    public void createRandomAmountOfRooms(TETile[][] tileArray) {
        int numOfRooms = RandomUtils.uniform(random, 10, 25);
        for (int i = 0; i < numOfRooms; i++) {
            Position p = createRandomPosition();
            createRoom(p, tileArray, i);
        }
    }

    /** Connects r1 to r2 with an appropriate hallway **/
    public void createHallway(Room r1, Room r2, TETile[][] tileArray) {
        int similarityOnX = Math.abs(r1.getStartPos().x - r2.getStartPos().x);
        int similarityOnY = Math.abs(r1.getStartPos().y - r2.getStartPos().y);
        r1.addRoomsConnectedToOtherRoom(r2);
        r2.addRoomsConnectedToOtherRoom(r1);
        if (similarityOnX <= similarityOnY) {
            makeVerticalHallway(r1, r2, tileArray);
        } else {
            makeHorizontalHallway(r1, r2, tileArray);
        }
    }


    public void makeVerticalHallway(Room r1, Room r2, TETile[][] tileArray) {
        Room lowerRoom = r1.lowerRoom(r2);
        Position lowerWallPos = lowerRoom.randomPositionOnTopWall(this);
        Room rightmostRoom = r1.rightmostRoom(r2);
        Position destinationPos = null;
        Room higherRoom = r1.higherRoom(r2);
        if ((lowerWallPos.x > higherRoom.getStartPos().x)
                && (lowerWallPos.x < higherRoom.getStartPos().x + higherRoom.getWidth() - 1)) {
            createVerticalHallwayWalls(lowerWallPos,
                    higherRoom.getStartPos().y - lowerWallPos.y + 1, tileArray);
            createVerticalHallwayFloor(lowerWallPos,
                    higherRoom.getStartPos().y - lowerWallPos.y + 1, tileArray);
            return;
        } else if ((lowerWallPos.x == higherRoom.getStartPos().x)
                || (lowerWallPos.x == higherRoom.getStartPos().x + higherRoom.getWidth() - 1)) {
            if (lowerWallPos.x + 1 < higherRoom.getStartPos().x + higherRoom.getWidth() - 1) {
                Position newWallPosition = new Position(lowerWallPos.x + 1, lowerWallPos.y);
                createVerticalHallwayWalls(newWallPosition,
                        higherRoom.getStartPos().y - lowerWallPos.y + 1, tileArray);
                createVerticalHallwayFloor(newWallPosition,
                        higherRoom.getStartPos().y - lowerWallPos.y + 1, tileArray);
                return;
            } else {
                Position newWallPosition = new Position(lowerWallPos.x - 1, lowerWallPos.y);
                createVerticalHallwayWalls(newWallPosition,
                        higherRoom.getStartPos().y - lowerWallPos.y + 1, tileArray);
                createVerticalHallwayFloor(newWallPosition,
                        higherRoom.getStartPos().y - lowerWallPos.y + 1, tileArray);
                return;
            }
        } else if (lowerRoom == rightmostRoom) {
            Room leftmostRoom = r1.leftmostRoom(r2);
            destinationPos = leftmostRoom.randomPositionOnRightWall(this);
        } else {
            destinationPos = rightmostRoom.randomPositionOnLeftWall(this);
        }
        createVerticalHallwayWalls(lowerWallPos,
                Math.abs(destinationPos.y - lowerWallPos.y) + 2, tileArray);
        Position corner = new Position(lowerWallPos.x,
                lowerWallPos.y + Math.abs(destinationPos.y - lowerWallPos.y));
        Position smallerX = Position.getSmallerX(corner, destinationPos);
        createHorizontalHallwayWalls(smallerX,
                Math.abs(corner.x - destinationPos.x) + 1, tileArray);
        createVerticalHallwayFloor(lowerWallPos,
                Math.abs(destinationPos.y - lowerWallPos.y), tileArray);
        createHorizontalHallwayFloor(smallerX,
                Math.abs(corner.x - destinationPos.x) + 1, tileArray);
    }

    public void makeHorizontalHallway(Room r1, Room r2, TETile[][] tileArray) {
        Room leftmostRoom = r1.leftmostRoom(r2);
        Position rightWallPos = leftmostRoom.randomPositionOnRightWall(this);
        Room upperRoom = r1.higherRoom(r2);
        Position destinationPos = null;
        Room rightmostRoom = r1.rightmostRoom(r2);
        if ((rightWallPos.y > rightmostRoom.getStartPos().y)
                && (rightWallPos.y < (rightmostRoom.getStartPos().y
                + rightmostRoom.getHeight() - 1))) {
            createHorizontalHallwayWalls(rightWallPos,
                    rightmostRoom.getStartPos().x - rightWallPos.x + 1, tileArray);
            createHorizontalHallwayFloor(rightWallPos,
                    rightmostRoom.getStartPos().x - rightWallPos.x + 1, tileArray);
            return;
        } else if ((rightWallPos.y == rightmostRoom.getStartPos().y)
                || (rightWallPos.y == rightmostRoom.getStartPos().y
                + rightmostRoom.getHeight() - 1)) {
            if (rightWallPos.y + 1 < rightmostRoom.getStartPos().y
                    + rightmostRoom.getHeight() - 1) {
                Position newWallPosition = new Position(rightWallPos.x, rightWallPos.y + 1);
                createHorizontalHallwayWalls(newWallPosition,
                        rightmostRoom.getStartPos().x - rightWallPos.x + 1, tileArray);
                createHorizontalHallwayFloor(newWallPosition,
                        rightmostRoom.getStartPos().x - rightWallPos.x + 1, tileArray);
                return;
            } else {
                Position newWallPosition = new Position(rightWallPos.x, rightWallPos.y - 1);
                createHorizontalHallwayWalls(newWallPosition,
                        rightmostRoom.getStartPos().x - rightWallPos.x + 1, tileArray);
                createHorizontalHallwayFloor(newWallPosition,
                        rightmostRoom.getStartPos().x - rightWallPos.x + 1, tileArray);
                return;
            }
        } else if (leftmostRoom == upperRoom) {
            Room lowerRoom = r1.lowerRoom(r2);
            destinationPos = lowerRoom.randomPositionOnTopWall(this);
        } else {
            destinationPos = upperRoom.randomPositionOnBottomWall(this);
        }
        createHorizontalHallwayWalls(rightWallPos,
                Math.abs(destinationPos.x - rightWallPos.x) + 2, tileArray);
        Position corner = new Position(rightWallPos.x
                +  Math.abs(destinationPos.x - rightWallPos.x), rightWallPos.y);
        Position smallerY = Position.getSmallerY(corner, destinationPos);
        createVerticalHallwayWalls(smallerY,
                Math.abs(corner.y - destinationPos.y) + 1, tileArray);
        createHorizontalHallwayFloor(rightWallPos,
                Math.abs(destinationPos.x - rightWallPos.x), tileArray);
        createVerticalHallwayFloor(smallerY,
                Math.abs(corner.y - destinationPos.y) + 1, tileArray);
    }

    /** Connects every room to its two closest neighbors in distance **/
    public void connectRooms(TETile[][] tileArray) {
        for (int i = 0; i < rooms.size(); i++) {
            Room closestNeighbor = rooms.get(i).findClosestNeighbor(tileArray, this);
            Room secondClosestNeighbor = rooms.get(i).findSecondClosestNeighbor(tileArray, this);
            createHallway(rooms.get(i), closestNeighbor, tileArray);
            createHallway(rooms.get(i), secondClosestNeighbor, tileArray);
        }
    }

    public void placeLightSwitch(TETile[][] tileArray) {
        int roomNum = random.nextInt(rooms.size());
        Position posInRoom = rooms.get(roomNum).randomPositionInRoom(this);
        tileArray[posInRoom.x][posInRoom.y] = Tileset.SWITCH;
    }



    /** Makes sure that there is no FLOOR tiles touching a NOTHING tile **/
    public void prepareWorld(TETile[][] tileArray) {
        for (int i = 1; i < floorspace.length - 10; i++) {
            for (int j = 1; j < floorspace[0].length - 10; j++) {
                Position p = new Position(i, j);
                if ((floorspace[i][j]) && (touchesNothingTile(p))) {
                    tileArray[i][j] = Tileset.WALL;
                }
            }
        }

        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomsConnectedTo() != rooms) {
                for (int j = 0; j < rooms.size(); j++) {
                    if (!rooms.get(i).getRoomsConnectedTo().contains(rooms.get(j))) {
                        createHallway(rooms.get(i), rooms.get(j), tileArray);
                    }
                }
            }
        }
    }

    /**
     * Checks if a tile at a given position touches a NOTHING tile
     **/
    public Boolean touchesNothingTile(Position p) {
        if (!taken[p.x - 1][p.y + 1]) {
            return true;
        }
        if (!taken[p.x][p.y + 1]) {
            return true;
        }
        if (!taken[p.x + 1][p.y + 1]) {
            return true;
        }
        if (!taken[p.x + 1][p.y]) {
            return true;
        }
        if (!taken[p.x + 1][p.y - 1]) {
            return true;
        }
        if (!taken[p.x][p.y - 1]) {
            return true;
        }
        if (!taken[p.x - 1][p.y - 1]) {
            return true;
        }
        if (!taken[p.x - 1][p.y]) {
            return true;
        }
        return false;
    }

    public TETile[][] getWorld() {
        return world;
    }

}
