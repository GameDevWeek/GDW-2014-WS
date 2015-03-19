package de.hochschuletrier.gdw.ws1415.game.utils;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by oliver on 16.03.15.
 */

public enum Direction{
    RIGHT(0, new Vector2(1,0)), UP(1, new Vector2(0,-1)),
    LEFT(2, new Vector2(-1,0)), DOWN(3, new Vector2(0,1)); //, EAST(0), NORD(1), WEST(2), SOUTH(3);

    Direction(int v, Vector2 vector2){
        this.value = v;
        this.vector2 = vector2;
    }

    int value;
    Vector2 vector2;

    /**
     * @return direcction for 90 degrees counter clock wise
     */
    public Direction rotate90CCL(){
        return fromInt((this.value + 1) % 4);
    }

    public Direction rotate90CL(){
        return fromInt((4 + this.value - 1) % 4);
    }

    public Direction rotate180(){
        return fromInt((this.value + 2) % 4);
    }

    public int toInt(){
        return value;
    }

    public Vector2 toVector2(){
        return new Vector2(this.vector2);
    }

    public static Direction fromInt(int i){
        switch (i){
            case(0): return RIGHT;
            case(1): return UP;
            case(2): return LEFT;
            case(3): return DOWN;
        }
        return null;
    };

    /**
     *  USE WITH CAUTION
     */
    public static Direction fromVector2(Vector2 v){
        float i = v.dot(Vector2.X);
        float j = v.dot(Vector2.Y);
        if(i == 0 && j == 0) return null;
        if(Math.abs(i) > Math.abs(j)) {
            if (i > 0) {
                return RIGHT;
            }
            else {
                return LEFT;
            }
        }else {
            if (j > 0) {
                return DOWN;
            }
            else {
                return UP;
            }
        }
    };

}