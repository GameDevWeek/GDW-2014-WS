package de.hochschuletrier.gdw.ws1415.game.systems;

import java.time.Clock;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Timer;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.JumpableAnimationComponent;

public class JumpAnimationSystem extends IteratingSystem {
    
    long startTimeForJump;
   
    public JumpAnimationSystem(AssetManagerX assetManager, int priority) {
        super(Family.all(JumpableAnimationComponent.class).get(), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PhysixBodyComponent physix = ComponentMappers.physixBody.get(entity);
        JumpableAnimationComponent jumpable = entity.getComponent(JumpableAnimationComponent.class);

        // Charakter has been in midair, check if on ground again
        if(jumpable.midair)
        {
            if(Math.abs(physix.getLinearVelocity().y - jumpable.pastFrameYVelocity) <= 1.5f)
            {
                // Charakter now on the ground, switch to idle-animation
                jumpable.midair = false;
                AnimationComponent animation = ComponentMappers.animation.get(entity);
                entity.getComponent(AnimationComponent.class).reset();
                entity.getComponent(AnimationComponent.class).animation = jumpable.idle;
                
//                System.out.println("Time for Jump: " + (System.currentTimeMillis() - startTimeForJump));
            }
        }
        // Charakter has been on the ground - check if falling or acending
        else if (Math.abs(physix.getLinearVelocity().y - jumpable.pastFrameYVelocity) > 40)
        {
            // Charakter in midair -> switch to jump-animation
            jumpable.midair = true;
            AnimationComponent animation = ComponentMappers.animation.get(entity);
            entity.getComponent(AnimationComponent.class).reset();
            entity.getComponent(AnimationComponent.class).animation = jumpable.jump;

            startTimeForJump = System.currentTimeMillis();
        }
       
    }
}