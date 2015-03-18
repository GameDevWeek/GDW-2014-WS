package de.hochschuletrier.gdw.ws1415.game.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;

import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.resourcelocator.CurrentResourceLocator;
import de.hochschuletrier.gdw.commons.tiled.LayerObject;
import de.hochschuletrier.gdw.commons.tiled.Layer;
import de.hochschuletrier.gdw.commons.tiled.TileInfo;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.TiledMap;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.commons.tiled.TileSet;
import de.hochschuletrier.gdw.commons.tiled.tmx.TmxImage;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.components.DamageComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.KillsPlayerOnContactComponent;


import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SpawnComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TriggerComponent;



// 
public class MapLoader {
    
    private final static int TILE_NO_ENTITY = 0;
    // Test Entity
    private final static int TILE_ENTITY_X = 1;
  

    HashMap<TileSet, Texture> tilesetImages;

    private PooledEngine engine;
    private PhysixSystem phySys;
    private TiledMap tiledMap;
    private ArrayList<Entity> triggered;
    // Objekte die andere Objecte ausloesen
    private ArrayList<Entity> triggering;
    private Entity spawn = null;
  
    private class RockSafe
    {
        public int x;
        public int y;
        public int ID;
        public RockSafe( int x,int y,int ID )
        {
            this.x = x;
            this.y = y;
            this.ID = ID;
        }
    }
    private ArrayList<RockSafe> rockTriggerList;
    
    public MapLoader( PooledEngine eng,PhysixSystem phySys, String filename )
    {
        engine = eng;
        rockTriggerList = new ArrayList<RockSafe>();
        
        // versuche map zu laden,
        // wenn nicht geladen werden kann wird
        // throw IllegalArgument
        try
        {
            // erhalte komplette Mapinformationen aus der Datei
            tiledMap = new TiledMap( filename, LayerObject.PolyMode.ABSOLUTE );


            
            // laufe ueber alle Layer und suche nach Entities
            for( Layer l : tiledMap.getLayers() )
            {
                if ( l == null ) continue;

              
              
                // Wenn der Layer aus Objecten besteht
                if ( l.getType() == Layer.Type.OBJECT )
                {
                    ArrayList<LayerObject> olist = l.getObjects();
                    
                    // wenn keine ObjektList zu finden ist NullPointerException
                    if ( olist == null )
                    {
                        throw new NullPointerException("Unable to find ObjectList");
                    } else
                    {
                        // gehe durch alle LayerObjects durch 
                        for( LayerObject o : olist )
                        {
                            // Einbinden der Objecte in die PoolEngine findet hier statt
                            // TODO Restlichen Objekte einbinden
                            
                            Entity ent; //= null;
                            switch( o.getName() )
                            {
                            case "Rock":
                            {
                                // fügt der rockTirggerList einen  neuen spawnable Rock an
                                rockTriggerList.add( new RockSafe( o.getX(),o.getY(),Integer.parseInt(o.getProperty("TriggerId","0") ) ) );
                            } 
                                break;
                            case "Player":
                            {
                                if ( spawn != null )
                                {
                                    EntityCreator.createAndAddPlayer( spawn.getComponent( PositionComponent.class ).x ,spawn.getComponent( PositionComponent.class ).y,0,engine );
                                } else
                                {
                                    spawn = EntityCreator.createAndAddPlayer( o.getX() , o.getY() ,0,engine );
                                }
                                
                            }
                                break;
                            case "Spawn":
                            {
                                if ( spawn != null )
                                {
                                    Entity e = engine.createEntity();
                                    SpawnComponent sc = engine.createComponent(SpawnComponent.class);
                                    e.add( sc );
                                    PositionComponent pc = engine.createComponent(PositionComponent.class);
                                    pc.x = o.getX();
                                    pc.y = o.getY();
                                    e.add(pc);
                                
                                    spawn.getComponent( PositionComponent.class ).x = o.getX();
                                    spawn.getComponent( PositionComponent.class ).y = o.getY();
                                } else
                                {
                                    Entity e = engine.createEntity();
                                    SpawnComponent sc = engine.createComponent(SpawnComponent.class);
                                    e.add( sc );
                                    PositionComponent pc = engine.createComponent(PositionComponent.class);
                                    pc.x = o.getX();
                                    pc.y = o.getY();
                                    e.add(pc);
                                    
                                    spawn = e;
                                }
                                
                            }
                                break;
                            case "RockTriggerX":
                            {
                                /*
                                // Triggerd den Rock
                                
                                // kreeire EventBox
                                ent =  EntityCreator.createAndAddEventBox( EventBoxType.EVENT , o.getX(), o.getY(), engine );
                                ent.getComponent( PositionComponent.class ).x = o.getX();
                                ent.getComponent( PositionComponent.class ).y = o.getY();
                                
                                
                                // TODO trigger erhalten
                                int triggID = Integer.parseInt( o.getProperty( "Id","0" ) );
                                
                                // erhalte Rocksafeattribute
                                RockSafe rockSafe;
                                for ( RockSafe rs : rockTriggerList )
                                {
                                    if ( rs.ID == triggID )
                                    {
                                        rockSafe = rs;
                                        break;
                                    }
                                }
                                
                                
                                EntityCreator.createTrigger(engine, phySys, (float)o.getX(),(float)o.getY(),(float)o.getWidth(),(float)o.getHeight(), (Entity ente) -> {
                                                
                                    EntityCreator.createAndAddEventBox( EventBoxType.EVENT , rockSafe.x, rockSafe.y, engine );
                                    
                                } );
                                */
                                
                            } 
                                break;
                            default:
                                // TODO Exception    wenn ein unbekanntes Objekt auftritt
                                break;    
                            }
                            // entities in die Liste aufnehmen
                            //entities.add( ent );
                        }                    
                    }
                } else
                
                {
                    // wenn der Layer keine Objekte enthält,
                    // besteht er aus Tiles 
                    
                    // alle Tiles des Layers abfangen
                    TileInfo[][] tiles =  l.getTiles();
                  
                    // Wenn Tiles Existieren
                    if ( tiles != null )
                    {
                        for(int i=0;i<tiles.length;i++)
                        {
                            if ( tiles[i] != null )
                            for(int j=0;j<tiles[i].length;j++)
                            {
                                // wenn tiles exisitiert wird dementsprechend eine 
                                
                                // Entity kreiert
                                if ( tiles[i][j] != null )
                                {
                                    // TileSet erhalten
                                    TileSet ts = tiledMap.getTileSets().get( tiles[i][j].tileSetId );
                                    
                                    Entity ent;
                                    
                                    // frage ob der Block Unverwundbar ist
                                    if ( tiles[i][j].getProperty("Invulnerable","None").equals("true") )
                                    {
                                        // Unverwundbare Einheit erstellen
                                        ent = EntityCreator.createAndAddInvulnerableFloor( engine, phySys,new Rectangle( i,j,1,1 ) ,ts.getTileWidth(),ts.getTileHeight() );
                                    } else
                                    {
                                        // Verwundbare einheit erstellen
                                        ent = EntityCreator.createAndAddVulnerableFloor( engine, phySys, i*ts.getTileWidth(),j*ts.getTileHeight(),ts.getTileWidth(),ts.getTileHeight() );
                                        // Lebenspunkte-Komponente anfügen
                                        HealthComponent hp = engine.createComponent(HealthComponent.class);
                                        hp.reset();
                                        hp.Value = Integer.parseInt(tiles[i][j].getProperty("HitPoints","1"));
                                        ent.add(hp);
                                    }
                                    
                                    // Position für den Block hinzufügen
                                    PositionComponent pc = engine.createComponent(PositionComponent.class);
                                    pc.reset();
                                    pc.x = i*ts.getTileWidth();
                                    pc.y = i*ts.getTileHeight();
                                    ent.add(pc);
                                    
                                    // je nach Blocktyp bestimmte Komponenten hinzufügen
                                    switch( tiles[i][j].getProperty("Type","None") )
                                    {
                                    case "None":
                                        // None sollte nicht vorkommen (Exception werfen?)
                                      break;
                                    case "Floor":
                                        //  keine extra Komponente
                                      break;
                                    case "Lava":
                                    {
                                        // Lava toetet bei Beruehrung
                                        KillsPlayerOnContactComponent c = engine.createComponent(KillsPlayerOnContactComponent.class);
                                        ent.add(c);
                                        // TODO welche Komponents beinhaltet es
                                    }
                                      break;
                                    case "SpikeDown":
                                    {
                                        // 1 Schaden nur an Spieler
                                        DamageComponent c = engine.createComponent(DamageComponent.class);
                                        c.reset();
                                        c.damageToPlayer = true;
                                        c.damage = 1;
                                        ent.add(c);
                                    }
                                      break;
                                    case "SpikeLeft":
                                    {
                                        // 1 Schaden nur an Spieler
                                        DamageComponent c = engine.createComponent(DamageComponent.class);
                                        c.reset();
                                        c.damageToPlayer = true;
                                        c.damage = 1;
                                        ent.add(c);
                                    }
                                      break;
                                    case "SpikeTop":
                                    {
                                        // 1 Schaden nur an Spieler
                                        DamageComponent c = engine.createComponent(DamageComponent.class);
                                        c.reset();
                                        c.damageToPlayer = true;
                                        c.damage = 1;
                                        ent.add(c);
                                    }
                                      break;
                                    case "SpikeRight":
                                    {
                                        // 1 Schaden nur an Spieler
                                        DamageComponent c = engine.createComponent(DamageComponent.class);
                                        c.reset();
                                        c.damageToPlayer = true;
                                        c.damage = 1;
                                        ent.add(c);
                                    }
                                      break;
                                    default:
                                        // TODO Exception ausgeben 
                                        // der ObjektTyp ist unbekannt
                                    break;
                                    }
                                }
                            }
                        }
                    } else
                    {
                        // sollte als Layertype : TILES angeben sein aber keine Tiles vorhanden
                        throw new NullPointerException("Unable to find TilesList");
                    }
                }
            }
        } 
        catch(Exception e)
        {
          throw new IllegalArgumentException( "Map konnte nicht geladen werden: " + filename );
        }
    }
    
    /**
     * gibt Informationen zu einem Block in der Map zurück
     * @param layer Layer in dem sich der Blcok befindet
     * @param x X-Position des Blocks
     * @param y Y-Position des Blocks
     * @return Infos Über den Block
     */
    public TileInfo getTileInfo(int layer,int x,int y)
    {
        return tiledMap.getLayers().get( layer ).getTiles()[x][y];
    }
    
    /**
     * gibt die LayerObject-Liste zurueck
     * @param layer  layer für die die Objekte gezogen werden sollen
     * @return Liste aller Objekte in diesem Layer
     */
    public ArrayList<LayerObject> getLayerObjects( int layer )
    {
        return tiledMap.getLayers().get( layer ).getObjects();
    }
        
    
    /**
     * 
     * @return die komplette TiledMap
     */
    public TiledMap getTiledMap()
    {
      return tiledMap;
    }
    
    public ArrayList<Layer> getLayers()
    {
      return tiledMap.getLayers();
    }
    
    public Layer getLayer( int index )
    {
      return tiledMap.getLayers().get(index);
    }
    
    public ArrayList<Entity> getEntities()
    {
      return entities;
    }
      
    /**
     * gibt das Entity mit dem Index <index> zurueck
     * !!! Momentan stehen hier nur unvollständige Entities drinne !!!
     * @param index Index der Entity
     * @return Entity
     */
    /*
    public Entity getEntity( int index)
    {
      return entities.get( index );
    }
          
    /**
     * Bisher sind noch keine Angaben hierzu in der Mapdata
     * @return
     */
    public int getRescuingMinersLeft()
    {
        // TODO
        return 0;
    }


  
    /**
     * keine Funktion
     */


    public TiledMap getMap()
    {
        return this.tiledMap;
    }

    
    /*
    public static void main( String args[])
    {
        MapLoader ml = new MapLoader("data/maps/Test_Physics.tmx");
    
    }*/
    public void mapobject()
    {
    	
    }
}
