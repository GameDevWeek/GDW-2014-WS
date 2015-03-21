package de.hochschuletrier.gdw.ws1415.game.systems.renderers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.EntityCreator;
import de.hochschuletrier.gdw.ws1415.game.Game;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DestructableBlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedFamilyRenderSystem;

public class AnimationRenderer extends SortedFamilyRenderSystem.Renderer {

    @SuppressWarnings("unchecked")
	public AnimationRenderer() {
        super(Family.all(AnimationComponent.class).exclude(DestructableBlockComponent.class).get());
    }

    @Override
    public void render(Entity entity, float deltaTime) {
    	AnimationComponent animation = ComponentMappers.animation.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);

        if (animation.IsActive)
        {
            animation.stateTime += deltaTime;
            animation.permanent_stateTime += deltaTime;
            if (animation.stateTime >= animation.animation.animationDuration)
            {
                animation.stateTime %= animation.animation.animationDuration;
                animation.animationFinished = true;
                if(animation.isDyingPlayer)
                {
                    Game.loadLevel();
                } else if(animation.isSpawningPlayer) {
                    EntityCreator.modifyPlayerToLiving(entity);
                }
            }
        }
        
        TextureRegion keyFrame = animation.animation.getKeyFrame(animation.permanent_stateTime);
        int w = keyFrame.getRegionWidth();
        int h = keyFrame.getRegionHeight();
        
        if(ComponentMappers.input.has(entity))
        {
            float direction = ComponentMappers.input.get(entity).direction;
        	if(direction > 0f)
                animation.flipX = false;
            else if(direction < 0f) // nötig da 0 ignoriert werden soll ohne guckrichtung zu ändern
                animation.flipX = true;

        }
        //keyFrame.isFlipXY() = ist (nicht gedreht), animation.flipXY = Frame soll gedreht sein; flipY wegen libGdx invertiert. Finde es so besser weil flip nur Werte vertauscht und scale multipliziert.
        keyFrame.flip(keyFrame.isFlipX() != animation.flipX, keyFrame.isFlipY() != !animation.flipY); 
        
        //float CalculatedPositionScaleX = (animation.flipX?-1:1) * position.scaleX;
        DrawUtil.batch.draw(keyFrame, position.x - w * 0.5f, position.y - h * 0.5f, w * 0.5f, h * 0.5f, w, h,  position.scaleX, position.scaleY, position.rotation);
    }
}
