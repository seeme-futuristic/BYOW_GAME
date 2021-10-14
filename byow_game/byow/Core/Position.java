package byow.Core;

import java.io.Serializable;

public class Position implements Serializable {
    int x, y;

    Position(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public static Position getLargerX(Position p1, Position p2) {
        if (p1.x > p2.x) {
            return p1;
        } else {
            return p2;
        }
    }

    public static Position getLargerY(Position p1, Position p2) {
        if (p1.y > p2.y) {
            return p1;
        } else {
            return p2;
        }
    }

    public static Position getSmallerX(Position p1, Position p2) {
        if (p1.x < p2.x) {
            return p1;
        } else {
            return p2;
        }
    }

    public static Position getSmallerY(Position p1, Position p2) {
        if (p1.y < p2.y) {
            return p1;
        } else {
            return p2;
        }
    }


    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
