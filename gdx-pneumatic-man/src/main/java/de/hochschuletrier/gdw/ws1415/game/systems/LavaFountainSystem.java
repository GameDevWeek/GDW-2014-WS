package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.LavaFountainComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;

public class LavaFountainSystem extends IteratingSystem{

    public LavaFountainSystem(int priority) {
        super(Family.all(LavaFountainComponent.class, PositionComponent.class).get(),
                priority);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        
        LavaFountainComponent lavaFountain = ComponentMappers.lavaFountain.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        if(lavaFountain.intervallOffset <= 0){
            if(lavaFountain.timeToNextFountain <= 0){
                if(lavaFountain.timeTillFountainStops>0){
                    lavaFountain.timeTillFountainStops -= deltaTime;
                    
                    //TODO Shoot LavaBalls
                }else{
                    lavaFountain.timeTillFountainStops = lavaFountain.length;
                    lavaFountain.timeToNextFountain = lavaFountain.intervall;
                }
            }else {
                lavaFountain.timeToNextFountain -= deltaTime;
            }
        }else {
            lavaFountain.intervallOffset -= deltaTime;
        }
    }

}
