package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    /**
     * The current working directory.
     */
    // put if needed.

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        Engine.generateMenu();
        Keyboard userInput = new Keyboard();
        long seed = userInput.initialUserInput(this);
        World w = new World(seed);
        userInput.setWorld(w);
        Avatar avatar = new Avatar();
        TETile[][] tileArray = w.generateWorld();
        avatar.spawnAvatar(w);
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(tileArray);
        userInput.keyboard(ter, tileArray, avatar);


    }

    public void interactWithKeyboard(TETile[][] tileArray, Avatar avatar, String inputFromFile) {
        Keyboard userInput = new Keyboard(inputFromFile);
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(tileArray);
        userInput.keyboard(ter, tileArray, avatar);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        //
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        long seed = 0;
        Keyboard key = new Keyboard();
        Avatar av = new Avatar();

        String movements = "";
        for (int i = 0; i < input.length(); i++) {
            char chr = Character.toUpperCase(input.charAt(i));
            if (chr == 'N') {
                while (input.charAt(i + 1) != 'S' && input.charAt(i + 1) != 's') {
                    String stringNum = String.valueOf(input.charAt(i + 1));
                    int number = Integer.parseInt(stringNum);
                    seed = seed * 10 + number;
                    i += 1;
                }
                i += 1;
            } else if (chr == 'W' || chr == 'A' || chr == 'S' || chr == 'D') {
                movements += chr;
            } else if (chr == ':') {
                if (Character.toUpperCase(input.charAt(i + 1)) == 'Q') {
                    key.saveFile(av, input);
                    break;
                }
            } else if (chr == 'L') {

                String newInput = key.loadForInputString(this);
                newInput = newInput.substring(0, newInput.length() - 2);
                newInput += input.substring(1);

                return interactWithInputString(newInput);

                //movements = key.loadForInputString(this);
            }
        }
        World w = new World(seed);
        TETile[][] finalWorldFrame = w.generateWorld();

        av.spawnAvatar(w);
        avatarMovements(movements, av, finalWorldFrame);
        return finalWorldFrame;
    }

    /**
     * Helper method used to move avatar with input string.
     */
    private void avatarMovements(String inputs, Avatar av, TETile[][] tileArray) {
        for (int i = 0; i < inputs.length(); i++) {
            char direction = Character.toUpperCase(inputs.charAt(i));
            if (direction == 'W') {
                av.moveUp(tileArray);
            } else if (direction == 'A') {
                av.moveLeft(tileArray);
            } else if (direction == 'S') {
                av.moveDown(tileArray);
            } else if (direction == 'D') {
                av.moveRight(tileArray);
            }
        }
    }


    /**
     * Creates the menu screen with options.
     */
    public static void generateMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(StdDraw.BLACK);
        Font font = new Font("Arial", Font.BOLD, 50);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 15, "GAME");
        Font font2 = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font2);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "New World (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load Previous World (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Quit Game (Q)");
        StdDraw.show();
        return;
    }


    public static void main(String[] args) {
        Engine e = new Engine();
        TETile[][] worldAfterLoad = e.interactWithInputString("lwsd");

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(worldAfterLoad);

    }


}

