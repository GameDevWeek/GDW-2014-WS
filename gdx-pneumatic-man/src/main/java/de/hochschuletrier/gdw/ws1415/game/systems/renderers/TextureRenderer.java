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

        if(textureComponent.region == null)
        	render(textureComponent.texture, position.x, position.y, position.rotation, position.scaleX, position.scaleY);
        else
        	render(textureComponent.region, position.x, position.y, position.rotation, position.scaleX, position.scaleY);
    }
    
    private void render(Texture tex, float x, float y, float rotation, float scaleX, float scaleY) {
        int w = tex.getWidth();
        int h = tex.getHeight();
        
    	DrawUtil.batch.draw(tex, x, y, w*0.5f, h*0.5f, w, h, scaleX, scaleY, rotation, 
    			(int)0, (int)0, (int)w, (int)h, false, true);
    }
    
    private void render(TextureRegion region, float x, float y, float rotation, float scaleX, float scaleY) {
    	Texture tex = region.getTexture();
    	
        int w = region.getRegionWidth();
        int h = region.getRegionHeight();
        
    	DrawUtil.batch.draw(tex, x - w * 0.5f, y - h * 0.5f, w*0.5f, h*0.5f, w, h, scaleX, scaleY, rotation, 
    			region.getRegionX(), region.getRegionY(), w, h, false, true);
    }
}