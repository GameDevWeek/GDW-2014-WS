package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.commons.gdx.audio.SoundInstance;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.LavaBallComponent;
import de.hochschuletrier.gdw.ws1415.game.components.LavaFountainComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SoundEmitterComponent;

public class LavaFountainSystem extends IteratingSystem {

    public LavaFountainSystem(int priority) {
        super(Family.one(LavaFountainComponent.class, LavaBallComponent.class)
                .get(), priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        LavaFountainComponent lavaFountain = ComponentMappers.lavaFountain
                .get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        LavaBallComponent lavaBall = ComponentMappers.lavaBall.get(entity);
        PhysixBodyComponent physixBody = ComponentMappers.physixBody
                .get(entity);
        // lavaBall spawning
        if (lavaFountain != null) {
            if (lavaFountain.intervallOffset <= 0) {
                // if offset is over
                if (lavaFountain.timeToNextFountain <= 0) {
                    // if cooldown (aka intervall) of fountain has run down
                    if (lavaFountain.timeTillFountainStops >= 0) {
                        // if the time a lava fountain runs has run
                        lavaFountain.timeTillFountainStops -= deltaTime;
                        if (lavaFountain.timeToNextBall <= 0) {

                            Entity ball = EntityCreator.createLavaBall(position.x,
                                    position.y, lavaFountain.lavaBallSpeed,
                                    lavaFountain.height);
                            SoundEmitterComponent soundEmitter = ComponentMappers.soundEmitter.get(ball);
                            SoundInstance si = soundEmitter.emitter.play(EntityCreator.assetManager.getSound("burst1"), false);
                            if(si != null) {
                                si.setReferenceDistance(30f);
                                si.setVolume(2.5f);
                            }
                            
                            lavaFountain.timeToNextBall = lavaFountain.lavaBallSpawnIntervall; // TODO
                                                                                               // dynamisch
                            // berechnen
                        } else {
                            lavaFountain.timeToNextBall -= deltaTime;
                        }

                    } else {
                        lavaFountain.timeTillFountainStops = lavaFountain.length;
                        lavaFountain.timeToNextFountain = lavaFountain.intervall;

                    }
                } else {
                    lavaFountain.timeToNextFountain -= deltaTime;
                }
            } else {
                lavaFountain.intervallOffset -= deltaTime;
            }
        }

        // lavaBall management
        if (lavaBall != null) {
            if (lavaBall.travelLength <= (float) (Math.abs(position.y
                    - lavaBall.startPositionY))
                    / (float) GameConstants.getTileSizeY()) {
                
                
                EntityCreator.engine.removeEntity(entity);
            } 
        }

    }

}
