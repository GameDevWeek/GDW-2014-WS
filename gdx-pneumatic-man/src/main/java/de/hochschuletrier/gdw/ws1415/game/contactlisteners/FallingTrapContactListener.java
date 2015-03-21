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

        PhysixBodyComponent mybody = ComponentMappers.physixBody.get(myEntity);

        // kommt rock von oben?
        float dot = d.dot(Direction.DOWN.toVector2());
        if(dot > 0) return; // nope

        DamageComponent dmg = myEntity.getComponent(DamageComponent.class);

        // ja stein kommt von oben:
        if(ComponentMappers.health.has(otherEntity)) {
            HealthComponent h = ComponentMappers.health.get(otherEntity);
            if(ComponentMappers.block.has(otherEntity)){
                h.DecrementByValueNextFrame += dmg.damageToTile ? dmg.damage : 0;
            }else if(ComponentMappers.player.has(otherEntity)){
                h.DecrementByValueNextFrame += dmg.damageToPlayer ? dmg.damage : 0;
            }

            EntityCreator.engine.removeEntity(myEntity);
        }
    }

    private void handleSpikes (PhysixContact contact, Entity myEntity, Entity otherEntity){
        //if(! ComponentMappers.block.has(otherEntity)) return; //just handle if the spike hits a block
        if(myEntity.getComponent(DirectionComponent.class).facingDirection != Direction.TOP) return;
        PhysixBodyComponent mybody = ComponentMappers.physixBody.get(myEntity);

        Vector2 a = mybody.getBody().getPosition().cpy().sub(contact.getOtherFixture().getBody().getPosition());
        float dot = a.dot(Direction.DOWN.toVector2());
        if(dot > 0) return;

        if(ComponentMappers.player.has(otherEntity)) return; // player jumped from ground to the spike
        if(ComponentMappers.killsPlayerOnContact.has(otherEntity)) return; // enemy jumped from ground to the spike

        mybody.setGravityScale(0.0f);
        mybody.setLinearVelocity(0, 0);
        mybody.getFixtureList().forEach(f -> f.setSensor(true));


        AnimationComponent animationComponent = ComponentMappers.animation.get(myEntity);
        animationComponent.stateTime = animationComponent.animation.animationDuration ;
        animationComponent.permanent_stateTime = animationComponent.animation.animationDuration ;

    }

}
