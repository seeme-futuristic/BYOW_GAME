package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;


import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Keyboard {

    private String input;
    private Boolean trackingInput;
    private Boolean seedRecieved = false;
    private Boolean smallerFOV = false;
    private Boolean inMiniGame = false;
    private World w;
    private int fusesPickedUp = 0;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    /**
     * The current working directory.
     */
    public static final File CWD = new File(System.getProperty("user.dir"));


    public Keyboard() {
        this.input = "";
        this.trackingInput = true;
    }

    public Keyboard(String inputFromFile) {
        this.input = inputFromFile;
        this.trackingInput = true;
        this.seedRecieved = true;

    }

    public void setWorld(World world) {
        this.w = world;
    }

    public void keyboard(TERenderer ter, TETile[][] tileArray, Avatar avatar) {
        trackingInput = true;

        while (trackingInput) {
            if (StdDraw.hasNextKeyTyped()) {
                char inputChar = Character.toUpperCase(StdDraw.nextKeyTyped());
                gameReaction(inputChar, avatar, tileArray);
            }
            StdDraw.enableDoubleBuffering();
            drawText("Type :q or :Q to save and quit game.", 14, 25, 49);
            drawText("Type V to change Field of View", 14, 25, 48);
            if (avatarEncountersTile(avatar, tileArray, Tileset.SWITCH) && (!inMiniGame)) {
                drawText("Type I to interact with mysterious switch", 14, 25, 47);
                displayMousePosition(tileArray);
                displayDateAndTime();
                ter.renderFrame(tileArray);
                StdDraw.pause(10);
            } else if (inMiniGame) {
                drawText("Oh No! The lights went out!", 14, 25, 46);
                drawText("Collect the fuses to turn them back on", 14, 25, 45);
                TETile[][] smallerTileArray = smallerTileArray(avatar, tileArray);
                displayMousePosition(smallerTileArray);
                displayDateAndTime();
                if (avatarEncountersTile(avatar, tileArray, Tileset.FUSE)) {
                    drawText("Type I to pick up fuse", 14, 25, 47);
                }
                if (fusesPickedUp == 3) {
                    inMiniGame = false;
                    drawText("Congrats! The lights are back on!", 14, 25, 44);
                }
                ter.renderFrame(smallerTileArray);
                StdDraw.pause(10);
            } else if (smallerFOV) {
                TETile[][] smallerTileArray = smallerTileArray(avatar, tileArray);
                displayMousePosition(smallerTileArray);
                displayDateAndTime();
                ter.renderFrame(smallerTileArray);
                StdDraw.pause(10);
            } else {
                displayMousePosition(tileArray);
                displayDateAndTime();
                ter.renderFrame(tileArray);
                StdDraw.pause(10);
            }
        }
    }

    public static void displayMousePosition(TETile[][] tileArray) {
        Position mousePosition = new Position((int) StdDraw.mouseX(), (int) StdDraw.mouseY());
        TETile currentMouseTile = tileArray[mousePosition.x][mousePosition.y];
        drawText(currentMouseTile.description(), 14, 3, 49);
    }


    public static void displayDateAndTime() {
        LocalDateTime uglyTime = LocalDateTime.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("MM-dd-yyy HH:mm:ss");
        String prettyTime = f.format(uglyTime);
        Font font = new Font("Arial", Font.PLAIN, 12);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(45, 49, prettyTime);
        StdDraw.pause(1);
        StdDraw.show();


//        drawText(prettyTime, 12, 45, 49);
    }

    public Boolean avatarEncountersTile(Avatar avatar, TETile[][] tileArray, TETile tile) {
        Position p = avatar.getPos();
        if (tileArray[p.x - 1][p.y + 1] == tile) {
            return true;
        }
        if (tileArray[p.x][p.y + 1] == tile) {
            return true;
        }
        if (tileArray[p.x + 1][p.y + 1] == tile) {
            return true;
        }
        if (tileArray[p.x + 1][p.y] == tile) {
            return true;
        }
        if (tileArray[p.x + 1][p.y - 1] == tile) {
            return true;
        }
        if (tileArray[p.x][p.y - 1] == tile) {
            return true;
        }
        if (tileArray[p.x - 1][p.y - 1] == tile) {
            return true;
        }
        if (tileArray[p.x - 1][p.y] == tile) {
            return true;
        }
        return false;
    }


    public void pickUpTile(Avatar avatar, TETile[][] tileArray, TETile tile) {
        Position p = avatar.getPos();
        if (tileArray[p.x - 1][p.y + 1] == tile) {
            tileArray[p.x - 1][p.y + 1] = Tileset.FLOOR;
            fusesPickedUp += 1;
        }
        if (tileArray[p.x][p.y + 1] == tile) {
            tileArray[p.x][p.y + 1] = Tileset.FLOOR;
            fusesPickedUp += 1;
        }
        if (tileArray[p.x + 1][p.y + 1] == tile) {
            tileArray[p.x + 1][p.y + 1] = Tileset.FLOOR;
            fusesPickedUp += 1;
        }
        if (tileArray[p.x + 1][p.y] == tile) {
            tileArray[p.x + 1][p.y] = Tileset.FLOOR;
            fusesPickedUp += 1;
        }
        if (tileArray[p.x + 1][p.y - 1] == tile) {
            tileArray[p.x + 1][p.y - 1] = Tileset.FLOOR;
            fusesPickedUp += 1;
        }
        if (tileArray[p.x][p.y - 1] == tile) {
            tileArray[p.x][p.y - 1] = Tileset.FLOOR;
            fusesPickedUp += 1;
        }
        if (tileArray[p.x - 1][p.y - 1] == tile) {
            tileArray[p.x - 1][p.y - 1] = Tileset.FLOOR;
            fusesPickedUp += 1;
        }
        if (tileArray[p.x - 1][p.y] == tile) {
            tileArray[p.x - 1][p.y] = Tileset.FLOOR;
            fusesPickedUp += 1;
        }
    }

    public void gameReaction(char userInput, Avatar avatar, TETile[][] tileArray) {
        if (userInput == 'W') {
            input += userInput;
            avatar.moveUp(tileArray);
        } else if (userInput == 'S') {
            input += userInput;
            avatar.moveDown(tileArray);
        } else if (userInput == 'A') {
            input += userInput;
            avatar.moveLeft(tileArray);
        } else if (userInput == 'D') {
            input += userInput;
            avatar.moveRight(tileArray);
        } else if (userInput == ':') {
            quitGame(avatar);
        } else if (userInput == 'V') {
            changeFieldOfView();
        } else if ((userInput == 'I') && (avatarEncountersTile(avatar, tileArray, Tileset.SWITCH))
                && (!inMiniGame)) {
            startMiniGame(tileArray);
        } else if ((userInput == 'I') && (avatarEncountersTile(avatar, tileArray, Tileset.FUSE))) {
            pickUpTile(avatar, tileArray, Tileset.FUSE);
        }
    }

    public void changeFieldOfView() {
        smallerFOV = !smallerFOV;
    }

    public void startMiniGame(TETile[][] tileArray) {
        fusesPickedUp = 0;
        inMiniGame = true;
        spawnFuses(tileArray);


    }

    public void spawnFuses(TETile[][] tileArray) {
        for (int i = 0; i < 3; i++) {
            Position p = w.getRooms().get(i).randomPositionInRoom(w);
            tileArray[p.x][p.y] = Tileset.FUSE;
        }
    }

    public TETile[][] smallerTileArray(Avatar avatar, TETile[][] tileArray) {
        Position avatarPos = avatar.getPos();
        TETile[][] smallerTileArray = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < tileArray.length; i++) {
            for (int j = 0; j < tileArray[0].length; j++) {
                smallerTileArray[i][j] = Tileset.NOTHING;
            }
        }
        for (int i = 0; i < tileArray.length; i++) {
            for (int j = 0; j < tileArray[0].length; j++) {
                if (((i + 4 >= avatarPos.x) && (i <= avatarPos.x))
                        || ((i >= avatarPos.x) && (i - 4 <= avatarPos.x))) {
                    if (((j <= avatarPos.y) && (j + 4 >= avatarPos.y))
                            || ((j >= avatarPos.y) && (j - 4 <= avatarPos.y))) {
                        smallerTileArray[i][j] = tileArray[i][j];
                    }
                }
            }
        }
        return smallerTileArray;
    }



    private void quitGame(Avatar avatar) {
        while (trackingInput) {
            if (StdDraw.hasNextKeyTyped()) {
                char letter = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (letter == 'Q') {
                    trackingInput = false;
                    saveFile(avatar, input);
                    System.exit(0);
                } else {
                    return;
                }
            } else {
                continue;
            }
        }
    }


    public void loadFile(Engine e) {
//        File save = Utils.join(CWD, "save");
        File inputFile = Utils.join(CWD, "input.txt");
        File avatarFile = Utils.join(CWD, "avatar.txt");
        String inputFromFile = Utils.readContentsAsString(inputFile);
        Avatar avatar = Utils.readObject(avatarFile, Avatar.class);
        TETile[][] tileArray = e.interactWithInputString(inputFromFile);
        e.interactWithKeyboard(tileArray, avatar, inputFromFile);
    }

    public String loadForInputString(Engine e) {
//        File save = Utils.join(CWD, "save");
        File inputFile = Utils.join(CWD, "input.txt");
        File avatarFile = Utils.join(CWD, "avatar.txt");
        String inputFromFile = Utils.readContentsAsString(inputFile);
        return inputFromFile;
    }

    public void saveFile(Avatar avatar, String inputParam) {
//        File save = Utils.join(CWD, "save");
        File inputFile = Utils.join(CWD, "input.txt");
        File avatarFile = Utils.join(CWD, "avatar.txt");
//        if (!save.exists()) {
//            save.mkdir();
//        }
        if (!inputFile.exists()) {
            try {
                inputFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!avatarFile.exists()) {
            try {
                avatarFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Utils.writeContents(inputFile, inputParam);
        Utils.writeObject(avatarFile, avatar);

    }

    public long initialUserInput(Engine e) {
        long seed = 0;
        while (trackingInput) {
            while (StdDraw.hasNextKeyTyped()) {
                char userInput = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (userInput == 'N') {
                    input += userInput;
                    seed = gatheringSeed();
                } else if (userInput == 'L') {
                    loadFile(e);
                } else if (userInput == 'Q') {
                    System.exit(0);
                }
            }
        }
        return seed;
    }

    public long gatheringSeed() {
        long seed = 0;
        int i = 0;
        while (trackingInput) {
            drawText("Enter Your Seed: ", 20, Engine.WIDTH / 2, 10);
            while (StdDraw.hasNextKeyTyped()) {
                char userInput = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (userInput != 'S') {

                    input += userInput;
                    long number = Character.getNumericValue(userInput);
                    seed = (seed * 10) + number;
                    i += 1;
//                    StdDraw.clear(StdDraw.BLACK);
//                    Engine.generateMenu();
                    drawText(String.valueOf(number), 20, ((Engine.WIDTH) / 2) - 10 + i, 8);
                } else {
                    input += userInput;
                    trackingInput = false;
                    return seed;
                }
            }
        }
        return seed;
    }

    private static void drawText(String txt, int fontSize, int xPos, int yPos) {
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(xPos, yPos, txt);
        StdDraw.show();
    }


    public static void main(String[] args) {
        World w = new World(123);
        Avatar a = new Avatar();
        Keyboard k = new Keyboard();
        //k.saveFile(a);
    }

}
