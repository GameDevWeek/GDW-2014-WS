package de.hochschuletrier.gdw.ws1415.game.contactlisteners;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;

/**
 * Handles contacts between falling Rocks and Blocks / Enemies / maybe even player
 * Handles contact between falling spikes hit blocks
 */
public class FallingTrapContactListener extends PhysixContactAdapter {

    public void beginContact(PhysixContact contact) {
        Entity myEntity = contact.getMyComponent().getEntity();
        Entity otherEntity = contact.getOtherComponent().getEntity();
        if(ComponentMappers.rockTraps.has(myEntity)){
            handleRocks(
                    contact, ComponentMappers.rockTraps.get(myEntity),
                    myEntity, otherEntity
            );
        }else if(ComponentMappers.spikes.has(myEntity)){
            handleSpikes(
                    contact, myEntity, otherEntity
            );
        }
    }

    private void handleRocks(PhysixContact contact, FallingRockComponent c, Entity myEntity, Entity otherEntity){
        Vector2 d = contact.getMyFixture().getBody().getPosition()
                .sub(contact.getOtherFixture().getBody().getPosition()); // vector von other nach rock

        // kommt rock von oben?
        if(d.dot(Vector2.Y) >= 0) return; // nein tut er nicht


        // ja stein kommt von oben:
        if(ComponentMappers.health.has(otherEntity)) {
            HealthComponent h = ComponentMappers.health.get(contact.getOtherComponent().getEntity());
            EntityCreator.engine.removeEntity(myEntity);
        }else if(ComponentMappers.block.has(otherEntity)){
            EntityCreator.engine.removeEntity(myEntity);
        }
    }

    private void handleSpikes (PhysixContact contact, Entity myEntity, Entity otherEntity){
        if(! ComponentMappers.block.has(otherEntity)) return; //just handle if the spike hits a block

        PhysixBodyComponent otherbody = ComponentMappers.physixBody.get(otherEntity);
        otherbody.getBody().setTransform(otherbody.getPosition(), 180);
        otherbody.setGravityScale(0.0f);
    }

}
