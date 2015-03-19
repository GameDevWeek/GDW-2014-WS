package de.hochschuletrier.gdw.ws1415.game.contactlisteners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.components.*;

/**
 * Handles contacts between player and other entities
 */
public class PlayerContactListener extends PhysixContactAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PlayerContactListener.class);

    public void beginContact(PhysixContact contact) {

        Entity player = contact.getMyComponent().getEntity();
        AnimationComponent anim = player.getComponent(AnimationComponent.class);

        if (contact.getOtherComponent() == null)
            return;

        // Entity myEntity = contact.getMyComponent().getEntity(); //
        Entity otherEntity = contact.getOtherComponent().getEntity();
        
        if(otherEntity.getComponent(MinerComponent.class) != null){
            otherEntity.getComponent(HealthComponent.class).Value = 0;
        }

        // Player collides with lava.
        if (otherEntity.getComponent(KillsPlayerOnContactComponent.class) != null) {
            // Player dies and level resets.
            HealthComponent Health = player.getComponent(HealthComponent.class);
            Health.DecrementByValueNextFrame = Health.Value;
        }

        if (otherEntity.getComponent(FallingRockTriggerComponent.class) != null){
            FallingRockTriggerComponent rockTriggerComponent = otherEntity.getComponent(FallingRockTriggerComponent.class);
            //FallingRockComponent rockComponent = ComponentMappers.rockTraps.get(rockTriggerComponent.rockEntity);
            //rockComponent.falling = true;
            PhysixBodyComponent bodyComponent = ComponentMappers.physixBody.get(rockTriggerComponent.rockEntity);
            PhysixModifierComponent modifierComponent = EntityCreator.engine.createComponent(PhysixModifierComponent.class);
            modifierComponent.schedule(() -> {
                bodyComponent.setActive(true);
            });
            rockTriggerComponent.rockEntity.add(modifierComponent);
            EntityCreator.engine.removeEntity(otherEntity);
        }

        if (otherEntity.getComponent(DamageComponent.class) != null) {
            // player touched an enemy
            if (otherEntity.getComponent(DamageComponent.class).damageToPlayer) {
                player.getComponent(HealthComponent.class).DecrementByValueNextFrame = otherEntity.getComponent(DamageComponent.class).damage;
            }
        }

        Vector2 d = contact.getMyFixture().getBody().getPosition()
                .sub(contact.getOtherFixture().getBody().getPosition()); // vector von object zu player
        // kommt player von oben?

        if(d.dot(Vector2.Y) < 0) { //TODO: && anim.animationFinished
            if (ComponentMappers.block.has(otherEntity)) {
//                if (ComponentMappers.health.has(otherEntity)) {
//                    if (contact.getWorldManifold().getNormal().y > 0) {
//                        HealthComponent OtherHealth = otherEntity.getComponent(HealthComponent.class);
//                        OtherHealth.Value -= 1;
//                    }
//                }
            }
        //}
    }

        // If the contact was with a tile then nothing happens to the player but
        // the tile's health
        // is reduced by 1.

}
