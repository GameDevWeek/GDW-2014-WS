package de.hochschuletrier.gdw.ws1415.game.contactlisteners;

import java.util.Random;

import javax.print.attribute.standard.MediaSize.Other;

import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.Settings;
import de.hochschuletrier.gdw.ws1415.game.utils.Direction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector2;

import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundInstance;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContact;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixContactAdapter;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixModifierComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.Game;
import de.hochschuletrier.gdw.ws1415.game.GameConstants;
import de.hochschuletrier.gdw.ws1415.game.Score;
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.systems.ScoreSystem;
import de.hochschuletrier.gdw.ws1415.states.WinState;

/**
 * Handles contacts between player and other entities
 */
public class PlayerContactListener extends PhysixContactAdapter {
    private static final Logger logger = LoggerFactory.getLogger(PlayerContactListener.class);
    PooledEngine engine;
    
    public PlayerContactListener(PooledEngine engine){
        super();
        this.engine = engine;
    }

    public void beginContact(PhysixContact contact) {
        
        if (contact.getOtherComponent() == null)
            return;

        Entity player = contact.getMyComponent().getEntity();
        Entity otherEntity = contact.getOtherComponent().getEntity();
        
        PhysixBodyComponent body = ComponentMappers.physixBody.get(player);
        if("jump".equals(contact.getMyFixture().getUserData())){
            JumpComponent jump = ComponentMappers.jump.get(player);
            if(jump.groundContacts==0)
            {
                jump.justLanded = true;
            }
            if(!contact.getOtherFixture().isSensor()) {
                jump.groundContacts++;
            }
            if(otherEntity.getComponent(PlatformComponent.class) != null) {
                player.getComponent(PlayerComponent.class).platformContactEntities.add(otherEntity);
            }
            if(ComponentMappers.killsPlayerOnContact.has(otherEntity) && ComponentMappers.health.has(otherEntity)) {
                if (isPlayerAboveContact(body, contact)) {
                    HealthComponent Health = otherEntity.getComponent(HealthComponent.class);
                    otherEntity.getComponent(DamageComponent.class).damageToPlayer = false;
                    Health.DecrementByValueNextFrame += 1;
                    player.getComponent(HealthComponent.class).DecrementByValueNextFrame = 0;
                }
            }
            return;
        }
        
        AnimationComponent anim = player.getComponent(AnimationComponent.class);
        
        if(otherEntity.getComponent(MinerComponent.class) != null){
            otherEntity.getComponent(HealthComponent.class).Value = 0;
        }
        
        if(otherEntity.getComponent(GoalComponent.class) != null){
            if(otherEntity.getComponent(GoalComponent.class).end_of_level){
                logger.info("Congratulations you saved all miners!");
                ScoreSystem scoreSys = engine.getSystem(ScoreSystem.class);
                scoreSys.goal.getComponent(GoalComponent.class).end_of_level = true;
                int current_game_time = scoreSys.player.getComponent(PlayerComponent.class).game_time;
                int saved_miners = scoreSys.player.getComponent(PlayerComponent.class).saved_miners;
                int destroyed_blocks = scoreSys.player.getComponent(PlayerComponent.class).destroyed_blocks;
                int miners_threshold = scoreSys.goal.getComponent(GoalComponent.class).miners_threshold;
                //Score.calculate_score(current_game_time, saved_miners, destroyed_blocks, miners_threshold);
                logger.info("Your score is: " + Score.score);
                
                // ***** Sound *****
                Random rm=new Random();
                int i=rm.nextInt(3)+1;//1-3
                logger.info("GlorySound "+i);
                ComponentMappers.soundEmitter.get(player).emitter.play(EntityCreator.assetManager.getSound("glory"+i), false);
                
                Settings.HIGHSCORE.set(""+Score.score);
                //Game.loadLevel();
                if(scoreSys.scoreCanBeRegistered){
                    Score.calculate_score(current_game_time, saved_miners, destroyed_blocks, miners_threshold);
                    logger.info("Your score is: " + Score.score);
                    
                    scoreSys.timeSinceLastCalculation = 0;
                    scoreSys.scoreCanBeRegistered = false;
                    
                    Main main = Main.getInstance();
                    WinState Win = (WinState) main.getPersistentState(WinState.class);
                    // Transfer data here
                    main.changeState(Win);
                    
                    //Game.win();
                    //Game.loadLevel();
                }
            }
        }

        if (otherEntity.getComponent(FallingRockTriggerComponent.class) != null){
            FallingRockTriggerComponent rockTriggerComponent = otherEntity.getComponent(FallingRockTriggerComponent.class);
            //FallingRockComponent rockComponent = ComponentMappers.rockTraps.get(rockTriggerComponent.rockEntity);
            //rockComponent.falling = true;
            if(ComponentMappers.physixBody.has(rockTriggerComponent.rockEntity)) {
                PhysixBodyComponent bodyComponent = ComponentMappers.physixBody.get(rockTriggerComponent.rockEntity);

                PhysixModifierComponent modifierComponent = EntityCreator.engine.createComponent(PhysixModifierComponent.class);
                modifierComponent.schedule(() -> {
                    bodyComponent.setGravityScale(1);
                    bodyComponent.setAwake(true);
                });
                
                ComponentMappers.damage.get(rockTriggerComponent.rockEntity).damageToTile = true;
                ComponentMappers.animation.get(rockTriggerComponent.rockEntity).IsActive = true;
                rockTriggerComponent.rockEntity.add(modifierComponent);
                EntityCreator.engine.removeEntity(otherEntity);
            }
        }
        

        

        if ((ComponentMappers.killsPlayerOnContact.has(otherEntity) ||  // enemys + lava
                ComponentMappers.spikes.has(otherEntity) ||
                ComponentMappers.lavaBall.has(otherEntity)
                )) {
            // player touched an enemy
            boolean IsAlive = true;
            if(ComponentMappers.health.has(otherEntity))
            {
                HealthComponent Health = ComponentMappers.health.get(otherEntity);
                IsAlive = (Health.Value - Health.DecrementByValueNextFrame) > 0;
            }
            
            if (IsAlive && otherEntity.getComponent(DamageComponent.class).damageToPlayer) {
                logger.info(contact.getWorldManifold().getNormal().toString());
                if(player.getComponent(HealthComponent.class) != null){
                    player.getComponent(HealthComponent.class).DecrementByValueNextFrame = otherEntity.getComponent(DamageComponent.class).damage;
                }
                
            }
        }
        

        
        //WiP
        /*if(otherEntity.getComponent(PlatformComponent.class)!= null) {
            PhysixBodyComponent body = ComponentMappers.physixBody.get(player);
            PhysixBodyComponent otherbody = ComponentMappers.physixBody.get(otherEntity);
            PlatformComponent platform = ComponentMappers.platform.get(otherEntity);
            
            PhysixModifierComponent modifierComp = EntityCreator.engine.createComponent(PhysixModifierComponent.class);
            modifierComp.schedule(() -> {
                float debug1 = otherbody.getLinearVelocity().x;
                float debug2 = body.getLinearVelocity().x;
                float debug3 = (otherbody.getLinearVelocity().cpy().add(body.getLinearVelocity())).x;
                body.setLinearVelocity(otherbody.getLinearVelocity().cpy().add(body.getLinearVelocity()));

            });
            player.add(modifierComp);
            
            }*/
    }
    public void endContact(PhysixContact contact) {
        Entity player = contact.getMyComponent().getEntity();
        Entity otherEntity = contact.getOtherComponent().getEntity();
        PlayerComponent playerComp = ComponentMappers.player.get(player);
        PlatformComponent otherPlatformComp = ComponentMappers.platform.get(otherEntity);
        
        PhysixBodyComponent body = ComponentMappers.physixBody.get(player);
        if(body!= null && "jump".equals(contact.getMyFixture().getUserData())){
           
            JumpComponent jump = ComponentMappers.jump.get(player);
            
            if(jump!= null && !contact.getOtherFixture().isSensor()){
                jump.groundContacts--;
            }
            if(otherPlatformComp != null && playerComp != null) {
                playerComp.platformContactEntities.remove(otherEntity);
            }
            
        }
    } 

        // If the contact was with a tile then nothing happens to the player but
        // the tile's health
        // is reduced by 1.

    private boolean isPlayerAboveContact(PhysixBodyComponent mybody, PhysixContact contact) {
        Vector2 playerPos = mybody.getBody().getPosition().cpy();
        playerPos.y = playerPos.y + 1.5f; // magic number because player is bigger than one tile
        Vector2 a = playerPos.sub(contact.getOtherFixture().getBody().getPosition());
        float dot = a.dot(Direction.DOWN.toVector2());
        if(dot > 0) return false;
        return true;

    }
    
}
