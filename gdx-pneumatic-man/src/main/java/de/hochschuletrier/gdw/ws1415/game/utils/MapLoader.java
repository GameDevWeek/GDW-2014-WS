package de.hochschuletrier.gdw.ws1415.game.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import de.hochschuletrier.gdw.commons.gdx.assets.AnimationExtended.PlayMode;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.tiled.utils.RectangleGenerator;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ws1415.Settings;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.systems.CameraSystem;

// 
public class MapLoader
{
    /**
     * 
     * @return List of all avaiable Maps
     * @author Tobias Gepp
     */
    public static ArrayList<String>loadMapList() throws IOException 
    {
        ArrayList<String> list = new ArrayList<String>();        
        
        InputStream fis = new FileInputStream("src/main/resources/data/maps/Maps.txt");
        StringBuffer line = new StringBuffer(80);
        boolean lastWasReturn = false;
        int c = 0;
        while( (c = fis.read()) > 0  )
        {
            if ( c != 13 && c != 10 )
            {
                lastWasReturn = false;
                line.append( (char)c );
            } else
            {
                if ( lastWasReturn == false )
                {
                    lastWasReturn = true;
                    list.add( line.toString()  );
                    line = new StringBuffer(80);
                }
            }
        }    
        if ( list.equals("") == false ) list.add( line.toString() );
        
        return list;
    }
    
    /**
     * @Deprecated Alte Version des Maploaders
     * 
     * @param engine    
     *      Die Pooled Engine
     * @param physixSystem
     * @param map
     * @param cameraSystem
     * 
     * @author Renderer
     */
//    public static void generateWorldFromTileMap(PooledEngine engine, PhysixSystem physixSystem, TiledMap map, CameraSystem cameraSystem) 
//    {
//        
//        try {
//            GameConstants.setTileSizeX(map.getTileWidth());
//            GameConstants.setTileSizeY(map.getTileHeight());
//        }catch (AccessDeniedException e){
//            e.printStackTrace();
//        }
//        RectangleGenerator generator = new RectangleGenerator();
//        generator.generate(map,
//                (Layer layer, TileInfo info) -> {
//                    return info.getBooleanProperty("Invulnerable", false)
//                            && info.getProperty("Type", "").equals("Floor");
//                },
//                EntityCreator::createAndAddInvulnerableFloor);
//        
//        generator.generate(map,
//                (Layer layer, TileInfo info) -> {
//                    return info.getBooleanProperty("Invulnerable", false)
//                            && info.getProperty("Type", "").equals("Lava");
//                },
//                EntityCreator::createAndAddLava);
//                
//        
//        
//        
//        HashMap<Integer, Entity> rocks = new HashMap<>();
//        for (Layer layer : map.getLayers()) {
//            if(layer.isObjectLayer()){
//                /// pre filtering important objects
//                for(LayerObject obj : layer.getObjects()){
//                    if(obj.getName().equalsIgnoreCase("Rock")){
//                        int RockId = obj.getIntProperty("TriggerId", 0);
//                        rocks.put(RockId, EntityCreator.createTrapBlock(obj.getX(), obj.getY(), RockId));
//                    }
//                }
//                for(LayerObject obj : layer.getObjects()){
//                    if(obj.getProperty("Name", "").equalsIgnoreCase("Platform")){
//                        PlatformMode mode = PlatformMode.valueOf(obj.getProperty("Mode", PlatformMode.ALWAYS.name()).toUpperCase());
//                        Direction dir = Direction.valueOf(obj.getProperty("Direction", Direction.UP.name()).toUpperCase()); // "Direction"
//                        int distance = (int)obj.getFloatProperty("Distance", 0);
//                        int hitpoints = obj.getIntProperty("Hitpoints", 0);
//                        float speed = obj.getFloatProperty("Speed", 0);
//                        if(hitpoints == 0)
//                            EntityCreator.IndestructablePlattformBlock(obj.getX(), obj.getY(), distance, dir, speed, mode);
//                        else
//                            EntityCreator.DestructablePlattformBlock(obj.getX(), obj.getY(), distance, dir, speed, mode, hitpoints);
//                    }else if(obj.getProperty("Name", "").equalsIgnoreCase("Rock")){
//                        // DO NOTHING - rocks are handled prior
//                    }else if(obj.getProperty("Name", "").equalsIgnoreCase("RockTrigger")){
//                            int RockId = obj.getIntProperty("Id", 0);
//                            Entity e = rocks.get(RockId);
//                        EntityCreator.createTrapSensor(
//                                obj.getX() - obj.getWidth()/2, obj.getY() + obj.getHeight()/2,
//                                obj.getWidth(), obj.getHeight(), e);
//                    }
//                    else if(obj.getProperty("Name", "").equalsIgnoreCase("Player")) {
//                            
//                    }
//                    else if(obj.getProperty("Name", "").equalsIgnoreCase("PlayerSpawn")){
//                        cameraSystem.follow(EntityCreator.createAndAddPlayer(obj.getX(), obj.getY(), 0));
//                        //TESTS FOR LIGHT
//                        EntityCreator.createConeLight(obj.getX(), obj.getY()-500f, new Color(1f, 1f, 1f, 1f), 50f, 90f, 45f);
//                        //EntityCreator.createChainLight(obj.getX(), obj.getY(), new Color(1f, 1f, 1f, 1f), 100f, true, new float[]{50f, -300f, 500f, -300f}/*new float[]{obj.getX()+20f, obj.getY()-20f,obj.getX()+40f, obj.getY()-20f}*/);
//                        //EntityCreator.createDirectionalLight(obj.getX(), obj.getY(), new Color(1f, 1f, 1f, 1f), 45f);
//                    }
//                    else if(obj.getProperty("Name", "").equalsIgnoreCase("LevelEnd")){
//                        EntityCreator.createAndAddGoal(obj.getX(), obj.getY(), Integer.parseInt(obj.getProperty("RequiredMiners", "0")));
//                        //EntityCreator.createAndAddEventBox(obj.getX(), obj.getY());
//                    }
//                    else if(obj.getProperty("Name", "").equalsIgnoreCase("Enemy")){
//                        Direction dir = Direction.valueOf(obj.getProperty("Direction", Direction.LEFT.name()).toUpperCase());
//                        AIType type = AIType.valueOf(obj.getProperty("Type", AIType.CHAMELEON.name()).toUpperCase());
//                            EntityCreator.createAndAddEnemy(obj.getX(), obj.getY(), dir, type);
//                    }
//                    else if(obj.getProperty("Name", "").equalsIgnoreCase("Miner")){
//                        EntityCreator.createAndAddMiner(obj.getX(), obj.getY());
//                    
//                    }
//                    else{
//                        Gdx.app.log("WARNING", "object " + obj.getName() + "[" + obj.getProperty("Name", "") + "]" + "does not match any name. No Entity created");
//                    }
//
//                }
//                // Spawning LavaFountains here
//                for(LayerObject obj : layer.getObjects()){
//                    if(obj.getName().equalsIgnoreCase("LavaFountain")){
//                        float positionX = obj.getX();
//                        float positionY = obj.getY();
//                        float height = obj.getFloatProperty("Height", 1.0f);
//                        float intervall = obj.getFloatProperty("Intervall", 1.0f);
//                        float intervallOffset = obj.getFloatProperty("IntervallOffset", 0.0f);
//                        float length = obj.getFloatProperty("Length", 1.0f);
//                        EntityCreator.createLavaFountain(positionX, positionY, height, intervall, intervallOffset, length);
//                    }
//                }
//                continue; // because it was a object layer
//            }
//            
//            // TODO: Move this code to another class: EntityMapCreator maybe? 
//            // is tile layer:
//            TileInfo[][] tiles = layer.getTiles();
//            for (int i = 0; i < map.getWidth(); i++) {
//                for (int j = 0; j < map.getHeight(); j++) {
//                    if (tiles != null && tiles[i] != null && tiles[i][j] != null) {
//                        if (tiles[i][j].getIntProperty("Hitpoint", 0) != 0
//                                && tiles[i][j].getProperty("Type", "").equals("Floor")) {
//                            TileInfo info = tiles[i][j];
//                            EntityCreator.createAndAddVulnerableFloor(
//                                    i * map.getTileWidth() + 0.5f * map.getTileWidth(),
//                                    j * map.getTileHeight() + 0.5f * map.getTileHeight(),
//                                    map, info, Integer.parseInt(info.getProperty("Hitpoint", "")), i, j);
//                        }
//                       
//                        
//                        else if (tiles[i][j].getBooleanProperty("Invulnerable", false)
//                                && tiles[i][j].getProperty("Type", "").equals("Floor")) {
//                            TileInfo info = tiles[i][j];
//                            EntityCreator.createAndAddVisualEntity(map, info, i, j);
//                        }
//                        
//                        if (tiles[i][j].getBooleanProperty("Invulnerable", false)
//                                && tiles[i][j].getProperty("Type", "").equals("Lava")) {
//                            TileInfo info = tiles[i][j];
//                            EntityCreator.createAndAddVisualEntity(map, info, i, j, PlayMode.LOOP, true);
//                        }
//                    }
//                }
//            }
//        }
//    }
    
    
    public static boolean constantsLoaded = false;
    /**
     * 
     * @param engine
     *      PooledEngine
     * @param physixSystem
     *      PhysikSystem
     * @param map
     *      TiledMap for this level
     * @param cameraSystem
     *      CameraSystem
     * @author Assets
     */
    
    public static String convertLight(String lightName)
    { 
        switch(lightName.toLowerCase())
        {
        case "white" : 
            return "FFFFFF";
        case "yellow" :
            return "FFFB00";
        case "blue" :
            return "0000FF";
        case "purple" :
            return "B22C8E";
        default : //TODO Exeption hinzufügen
            return "FFFFFF";
        }
    }
    public static void generateWorldFromTileMapX(PooledEngine engine, PhysixSystem physixSystem, TiledMap map, CameraSystem cameraSystem) 
    {
        // if constants weren't loaded yet, do so
        if ( !constantsLoaded )
        {
            constantsLoaded = true;
            try 
            {
                GameConstants.setTileSizeX(map.getTileWidth());
                GameConstants.setTileSizeY(map.getTileHeight());
            }catch (AccessDeniedException e)
            {
                e.printStackTrace();
            }
        }
        
        RectangleGenerator generator = new RectangleGenerator();
        generator.generate(map,
                (Layer layer, TileInfo info) -> {
                    return info.getBooleanProperty("Invulnerable", false)
                            && info.getProperty("Type", "").equals("Floor");
                },
                EntityCreator::createAndAddInvulnerableFloor);
        
        generator.generate(map,
                (Layer layer, TileInfo info) -> {
                     return info.getBooleanProperty("Invulnerable", false)
                            && info.getProperty("Type", "").equals("Lava");
                },
                EntityCreator::createAndAddLava);
        
        // Liste um fallende Rocks abzuspeichern
        HashMap<Integer, Entity> rocks = new HashMap<>();
        for (Layer layer : map.getLayers()) 
        {
            if(layer.isObjectLayer())
            {
                /// pre filtering important objects,
                /// wich needs to be already existing when loading other Objects
                for(LayerObject obj : layer.getObjects())
                {
                    // get name of Object and make it Case Insensitive
                    String name = obj.getName().toLowerCase();
                    switch( name )
                    {
                    case "rock":
                        int RockId = obj.getIntProperty("TriggerId", 0);
                        rocks.put(RockId, EntityCreator.createTrapBlock(obj.getX() + obj.getWidth()/2 , obj.getY() + obj.getHeight()/2 , RockId));
                        break;
                    }
                }
                // loading the restObjects
                for(LayerObject obj : layer.getObjects())
                {
                    String name = obj.getName().toLowerCase();
                    switch( name )
                    {
                        // all preloaded Object are not needed to be handled
                        case "rock":
                            break;
                        case "platform":
                        {
                            PlatformMode mode = PlatformMode.valueOf(obj.getProperty("Mode", PlatformMode.ALWAYS.name()).toUpperCase());
                            Direction dir = Direction.valueOf(obj.getProperty("Direction", Direction.UP.name()).toUpperCase()); // "Direction"
                            int distance = (int)obj.getDoubleProperty("Distance", 0.0);
                            int hitpoints = obj.getIntProperty("Hitpoints", 0);
                            float speed = obj.getFloatProperty("Speed", 0);
                            if(hitpoints == 0){
                                EntityCreator.IndestructablePlattformBlock(obj.getX(), obj.getY(), distance, dir, speed, mode);
                                if(obj.getProperty("Name", "").equalsIgnoreCase("SpawnAndLevelEnd")){
                                    float x = obj.getX() * map.getTileWidth() + 0.5f * map.getTileWidth();
                                    float y = obj.getY() * map.getTileHeight() + 0.5f * map.getTileHeight();
                                    EntityCreator.createPointLight(x, y, 0f, -10f, new Color(1f, 1f, 1f, 1f), 2f, true);
                                }
                            }
                            else
                                EntityCreator.DestructablePlattformBlock(obj.getX(), obj.getY(), distance, dir, speed, mode, hitpoints);
                        }
                            break;
                        case "rocktrigger":
                        {
                            int RockId = obj.getIntProperty("Id", 0);
                            Entity e = rocks.get(RockId);
                            EntityCreator.createTrapSensor(
                                obj.getX() + obj.getWidth()/2, obj.getY() + obj.getHeight()/2,
                                obj.getWidth(), obj.getHeight(), e);
                        }
                            break;
                        case "player": // ???
                            break;
                        case "playerspawn":
                        {
                            float width = GameConstants.getTileSizeX() * 0.9f;
                            float height = GameConstants.getTileSizeY() * 1.5f;
                            cameraSystem.follow( EntityCreator.createAndAddPlayer(obj.getX() + width, obj.getY(), 0) );
                        }

                            break;
                        case "levelend":
                        {
                            EntityCreator.createAndAddGoal(obj.getX(), obj.getY(), Integer.parseInt(obj.getProperty("MinMiner", "0")));                            
                        }
                            break;
                        case "enemy":
                        {
                            Direction dir = Direction.valueOf(obj.getProperty("Direction", Direction.LEFT.name()).toUpperCase());
                            AIType type = AIType.valueOf(obj.getProperty("Type", AIType.CHAMELEON.name()).toUpperCase());
                                EntityCreator.createAndAddEnemy(obj.getX(), obj.getY(), dir, type);
                        }
                            break;
                        case "lavafountain":
                        {
                            float positionX = obj.getX();
                            float positionY = obj.getY();
                            float height = obj.getFloatProperty("Height", 1.0f);
                            float intervall = obj.getFloatProperty("Intervall", 1.0f);
                            float intervallOffset = obj.getFloatProperty("IntervallOffset", 0.0f);
                            float length = obj.getFloatProperty("Length", 1.0f);
                            EntityCreator.createLavaFountain(positionX, positionY, height, intervall, intervallOffset, length);
                        }
                            break;
                        case "miner":
                        {    
                            float positionX = obj.getX() ;
                            float positionY = obj.getY();
                            EntityCreator.createAndAddMiner(positionX, positionY);
                            break;
                        }
                        default: 
                        {
                            Gdx.app.log("WARNING", "object " + name + " does not match any name. No Entity created");
                        }
                    }
                }
            } else if ( layer.isTileLayer() ) // is tile layer:
            {
                TileInfo[][] tiles = layer.getTiles();
                for (int i = 0; i < map.getWidth(); i++) 
                {
                    for (int j = 0; j < map.getHeight(); j++) 
                    {
                        // if tile exist
                        if (tiles != null && tiles[i] != null && tiles[i][j] != null) 
                        {
                            // make CaseInsesitve
                            String type = tiles[i][j].getProperty("Type", "").toLowerCase();
                            TileInfo tinfo = tiles[i][j];
                            
                            switch( type )
                            {
                                case "floor":
                                {
                                    
                                    if ( tinfo.getIntProperty("Hitpoints", 0) != 0 )
                                    {  
                                        
                                        EntityCreator.createAndAddVulnerableFloor(
                                                i * map.getTileWidth() + 0.5f * map.getTileWidth(),
                                                j * map.getTileHeight() + 0.5f * map.getTileHeight(),
                                                map, tinfo, tinfo.getIntProperty("Hitpoints", 0), i, j);
                                                
                                    } 
                                    if ( tinfo.getBooleanProperty("Invulnerable", false) )
                                    {
                                        EntityCreator.createAndAddVisualEntity(map, tinfo, i, j);
                                    }
                                    if ( tinfo.getProperty("Name","").equalsIgnoreCase("SpawnAndLevelEnd") )
                                    {
                                        
                                        EntityCreator.createConeLight( 
                                                i * map.getTileWidth(), 
                                                j * map.getTileHeight(),
                                                0.5f * map.getTileWidth(),
                                                0.25f * map.getTileHeight(),
                                                Color.valueOf("FF0088" ), 4.0f, 270.0f, 40.0f, false);
                                                
                                    }
                                }
                                    break;
                                case "spikeleft": case "spiketop": case "spikeright": case "spikedown":
                                {
                                    Direction dir = Direction.valueOf(type.substring(5).toUpperCase());
                                    EntityCreator.createSpike(i, j, dir, tinfo, map);
                                   
                                }
                                    break;
                                case "lava":
                                {
                                    if (tiles[i][j].getBooleanProperty("Invulnerable", false)
                                            && tiles[i][j].getProperty("Type", "").equals("Lava")) 
                                    {
                                        TileInfo info = tiles[i][j];
                                        EntityCreator.createAndAddVisualEntity(map, info, i, j, PlayMode.LOOP, true);
                                        Color color = Color.valueOf("FFF600");
                                        float x = i * map.getTileWidth()+0.5f * map.getTileWidth();
                                        float y = j * map.getTileHeight()+0.5f * map.getTileHeight();
                                        EntityCreator.createChainLight(x, y,0,0, color, Float.parseFloat(info.getProperty("Distance", ""))+1f, false, new float[]{-32.5f,30f,32.5f,30f},true);
                                    }
                                }
                                    break;
                                    
                                case "bomb":
                                {
                                    EntityCreator.createAndAddBomb(i * map.getTileWidth() + 0.5f * map.getTileWidth(),
                                                j * map.getTileHeight() + 0.5f * map.getTileHeight(), map, tinfo, i, j);
                                }
                                    break;
                                case "deko":                                
                                {
                                    Color color = Color.valueOf(convertLight(tinfo.getProperty("Colour", "")));
                                    color.a = Float.parseFloat(tinfo.getProperty("Brightness", "0.7"));
                                    float xOffset = Float.parseFloat(tinfo.getProperty("XOffset", "0.0"));
                                    float yOffset = Float.parseFloat(tinfo.getProperty("YOffset", "0.0"));
                                    float x = i * map.getTileWidth();
                                    float y = j * map.getTileHeight();
                                    // different sizes needed to have some offsets
                                    switch( map.getTileSets().get( tinfo.tileSetId ).getName().toLowerCase() )
                                    {
                                        case "deco_gross":
                                            EntityCreator.createAndAddVisualEntity(map, tinfo, i+0.5f, j+0.5f);
                                            break;
                                        case "tutorialschild":
                                        {
                                            int add=0;
                                            if ( Settings.GAMEPAD_ENABLED.get() ) add = 1;
                                            TileInfo ti = new TileInfo( tinfo.tileSetId,tinfo.localId+add,tinfo.globalId+add,tinfo.getProperties() );
                                            EntityCreator.createAndAddVisualEntity(map, ti, i+1, j+1);
                                        }
                                            break;
                                        default :
                                            EntityCreator.createAndAddVisualEntity(map, tinfo, i, j);
                                            break;
                                    }
                                    
                                    
                                    
                                    if ( tinfo.getBooleanProperty("Light",false) == true ) 
                                    {
                                        float dis = tinfo.getFloatProperty("Distance", 0.0f); 
                                        float dir = (float) (tinfo.getFloatProperty("Direction", 90.0f) );
                                        float conedir = tinfo.getFloatProperty("Radius", 30.0f);
                                        
                                        
//                                        // TODO tests for the ChainLight
//                                        if ( tinfo.getProperty("Name","").toLowerCase().equals( "lamp1" ) ||
//                                                tinfo.getProperty("Name","").toLowerCase().equals( "lamp2" ) ||
//                                                tinfo.getProperty("Name","").toLowerCase().equals( "lamp3" ) ) 
//                                        {
//                                            dis = 5f; // TODO 5f for test
//                                        }
                                        
                                        
                                        EntityCreator.createAndAddDeko(x, y, xOffset, yOffset, dir, dis, conedir, color, tinfo.getProperty("LightType","point") );
                                    }
                                    
                                }
                                    break;
                                default :
                                    Gdx.app.log("WARNING", "layer-type " + type + " does not match any name. No Entity created");
                                    break;
                            }
                        }
                    }
                }
            } 
        }
    }
}
