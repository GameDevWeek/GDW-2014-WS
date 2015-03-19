package de.hochschuletrier.gdw.ws1415.game;

import java.nio.file.AccessDeniedException;

public class GameConstants {

    // Priorities for entity systems
    public static final int PRIORITY_PHYSIX = 0;
    public static final int PRIORITY_ENTITIES = 10;
    public static final int PRIORITY_ANIMATIONS = 20;
    public static final int PRIORITY_DEBUG_WORLD = 30;
    public static final int PRIORITY_HUD = 40;
    public static final int PRIORITY_REMOVE_ENTITIES = 1000;

    // PooledEngine parameters
    public static final int ENTITY_POOL_INITIAL_SIZE = 32;
    public static final int ENTITY_POOL_MAX_SIZE = 256;
    public static final int COMPONENT_POOL_INITIAL_SIZE = 32;
    public static final int COMPONENT_POOL_MAX_SIZE = 256;

    // Physix parameters
    public static final int POSITION_ITERATIONS = 3;
    public static final int GRAVITY_CONSTANT = 30;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int BOX2D_SCALE = 40;
    
    // Light parameters
    public static final int LIGHT_RAYS = 125;
    
    private static int TileSizeX = 0;
    private static int TileSizeY = 0;

    public static int getTileSizeX(){ return TileSizeX; }
    public static int getTileSizeY(){ return TileSizeY; }

    public static void setTileSizeX(int x) throws AccessDeniedException {
        if(TileSizeX != 0) throw new AccessDeniedException("Do not change Tilesize after initializing");
        else TileSizeX = x;
    }

    public static void setTileSizeY(int y) throws AccessDeniedException {
        if(TileSizeY != 0) throw new AccessDeniedException("Do not change Tilesize after initializing");
        else TileSizeY = y;
    }

}
