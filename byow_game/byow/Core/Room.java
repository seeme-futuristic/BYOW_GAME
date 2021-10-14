package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Room implements Serializable {

    private int width;
    private int height;
    private Position startPos;
    private int ID;
    private Boolean connected;
    private ArrayList<Room> roomsConnectedTo;


    public Room(int givenWidth, int givenHeight, Position givenStartPos, int givenID) {
        this.width = givenWidth;
        this.height = givenHeight;
        this.startPos = givenStartPos;
        this.ID = givenID;
        this.connected = false;
        this.roomsConnectedTo = new ArrayList<Room>();
        roomsConnectedTo.add(this);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Position getStartPos() {
        return this.startPos;
    }

    public Boolean isConnected() {
        return this.connected;
    }

    public void setConnected(Boolean b) {
        this.connected = b;
    }

    public ArrayList<Room> getRoomsConnectedTo() {
        return this.roomsConnectedTo;
    }

    public Position randomPositionInRoom(World w) {
        Random rand = w.getRandom();
        int xPos = rand.nextInt(this.width - 2) + this.startPos.x + 1;
        int yPos = rand.nextInt(this.height - 2) + this.startPos.y + 1;
        return new Position(xPos, yPos);
    }

    public Position randomPositionOnTopWall(World w) {
        Random rand = w.getRandom();
        int xPos = rand.nextInt(this.width - 2) + this.startPos.x + 1;
        int yPos = this.startPos.y + height - 1;
        return new Position(xPos, yPos);
    }

    public Position randomPositionOnBottomWall(World w) {
        Random rand = w.getRandom();
        int xPos = rand.nextInt(this.width - 2) + this.startPos.x + 1;
        int yPos = this.startPos.y;
        return new Position(xPos, yPos);
    }

    public Position randomPositionOnLeftWall(World w) {
        Random rand = w.getRandom();
        int xPos = this.startPos.x;
        int yPos = rand.nextInt(this.height - 2) + startPos.y + 1;
        return new Position(xPos, yPos);
    }

    public Position randomPositionOnRightWall(World w) {
        Random rand = w.getRandom();
        int xPos = this.startPos.x + width - 1;
        int yPos = rand.nextInt(this.height - 2) + startPos.y + 1;
        return new Position(xPos, yPos);
    }

    public Room lowerRoom(Room otherRoom) {
        if (this.startPos.y < otherRoom.getStartPos().y) {
            return this;
        } else {
            return otherRoom;
        }
    }

    public Room higherRoom(Room otherRoom) {
        if (this.startPos.y > otherRoom.getStartPos().y) {
            return this;
        } else {
            return otherRoom;
        }
    }

    public Room leftmostRoom(Room otherRoom) {
        if (this.startPos.x < otherRoom.getStartPos().x) {
            return this;
        } else {
            return otherRoom;
        }
    }

    public Room rightmostRoom(Room otherRoom) {
        if (this.startPos.x > otherRoom.getStartPos().x) {
            return this;
        } else {
            return otherRoom;
        }
    }

    public Room findClosestNeighbor(TETile[][] world, World w) {
        ArrayList<Room> allRooms = w.getRooms();
        ArrayList<Room> roomsExceptThis = (ArrayList<Room>) allRooms.clone();
        roomsExceptThis.remove(this);
        Room closest = roomsExceptThis.get(0);
        for (int i = 1; i < roomsExceptThis.size(); i++) {
            Room temp = roomsExceptThis.get(i);
            if (this.distanceFormula(temp) < this.distanceFormula(closest)) {
                closest = temp;
            }
        }
        return closest;
    }

    public Room findSecondClosestNeighbor(TETile[][] world, World w) {
        ArrayList<Room> allRooms = w.getRooms();
        ArrayList<Room> roomsExceptThis = (ArrayList<Room>) allRooms.clone();
        roomsExceptThis.remove(this);
        roomsExceptThis.remove(this.findClosestNeighbor(world, w));
        Room closest = roomsExceptThis.get(0);
        for (int i = 1; i < roomsExceptThis.size(); i++) {
            Room temp = roomsExceptThis.get(i);
            if (this.distanceFormula(temp) < this.distanceFormula(closest)) {
                closest = temp;
            }
        }
        return closest;
    }

    private int distanceFormula(Room other) {
        Position thisPosition = this.getStartPos();
        Position otherPosition = other.getStartPos();

        int xPart = (otherPosition.x - thisPosition.x) * (otherPosition.x - thisPosition.x);
        int yPart = (otherPosition.y - thisPosition.y) * (otherPosition.y - thisPosition.y);
        return (int) Math.sqrt(xPart + yPart);
    }

    public void addRoomsConnectedToOtherRoom(Room other) {
        for (int i = 0; i < other.roomsConnectedTo.size(); i++) {
            if (!roomsConnectedTo.contains(other.roomsConnectedTo.get(i))) {
                for (int j = 0; j < roomsConnectedTo.size(); j++) {
                    if (!roomsConnectedTo.get(j).roomsConnectedTo.
                            contains(other.roomsConnectedTo.get(i))) {
                        roomsConnectedTo.get(j).roomsConnectedTo.add(other.roomsConnectedTo.get(i));
                    }
                }
                roomsConnectedTo.add(other.roomsConnectedTo.get(i));

            }
        }
    }


    public String toString() {
        return "\nID: " + ID + "\nWidth: " + width + "\nHeight: "
                + height + "\nStart Position: " + startPos + "\n";
    }
}
