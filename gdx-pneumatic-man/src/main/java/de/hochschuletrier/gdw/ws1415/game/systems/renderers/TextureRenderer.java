package de.hochschuletrier.gdw.ws1415.game.systems.renderers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TextureComponent;
import de.hochschuletrier.gdw.ws1415.game.systems.SortedFamilyRenderSystem;

public class TextureRenderer extends SortedFamilyRenderSystem.Renderer {

    @SuppressWarnings("unchecked")
	public TextureRenderer() {
        super(Family.all(TextureComponent.class).get());
    }

    @Override
    public void render(Entity entity, float deltaTime) {
        TextureComponent textureComponent = ComponentMappers.texture.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        
        render(textureComponent.texture, position.x, position.y, position.rotation);
    }
    
    private void render(Texture tex, float x, float y, float rotation) {
        int w = tex.getWidth();
        int h = tex.getHeight();
        
    	DrawUtil.batch.draw(tex, x, y, w*0.5f, h*0.5f, w, h, 1f, 1f, rotation, (int)0, (int)0, (int)w, (int)h, false, true);
    }
}