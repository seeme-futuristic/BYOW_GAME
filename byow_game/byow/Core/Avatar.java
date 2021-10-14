package byow.Core;



import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;

public class Avatar implements Serializable {
    private Position pos;


    public void spawnAvatar(World w) {
        TETile avatar = Tileset.AVATAR;
        ArrayList<Room> roomList = w.getRooms();
        Room r = roomList.get(RandomUtils.uniform(w.getRandom(), 0, roomList.size()));
        Position startRoom = r.randomPositionInRoom(w);
        Position initPos = startRoom;
        w.getWorld()[initPos.x][initPos.y] = avatar;
        pos = initPos;
        //w.updateWorld();
    }

    public Position getPos() {
        return pos;
    }


    public void moveUp(TETile[][] tileArray) {
        if (tileArray[pos.x][pos.y + 1] == Tileset.FLOOR) {
            tileArray[pos.x][pos.y + 1] = Tileset.AVATAR;
            tileArray[pos.x][pos.y] = Tileset.FLOOR;
            pos.y += 1;
        } else {
            return;
        }
    }

    public void moveDown(TETile[][] tileArray) {
        if (tileArray[pos.x][pos.y - 1] == Tileset.FLOOR) {
            tileArray[pos.x][pos.y - 1] = Tileset.AVATAR;
            tileArray[pos.x][pos.y] = Tileset.FLOOR;
            pos.y -= 1;
        } else {
            return;
        }
    }

    public void moveLeft(TETile[][] tileArray) {
        if (tileArray[pos.x - 1][pos.y] == Tileset.FLOOR) {
            tileArray[pos.x - 1][pos.y] = Tileset.AVATAR;
            tileArray[pos.x][pos.y] = Tileset.FLOOR;
            pos.x -= 1;
        } else {
            return;
        }
    }

    public void moveRight(TETile[][] tileArray) {
        if (tileArray[pos.x + 1][pos.y] == Tileset.FLOOR) {
            tileArray[pos.x + 1][pos.y] = Tileset.AVATAR;
            tileArray[pos.x][pos.y] = Tileset.FLOOR;
            pos.x += 1;
        } else {
            return;
        }
    }

    public static void main(String[] args) {
        //TERenderer ter = new TERenderer();
        World w = new World(1432L);

        //ter.initialize(50, 50);

        w.renderWorld();
        w.generateWorld();
//        Avatar.spawnAvatar(w);
        //w.updateWorld();


    }

}
