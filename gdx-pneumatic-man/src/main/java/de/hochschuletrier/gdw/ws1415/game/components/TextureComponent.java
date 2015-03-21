package de.hochschuletrier.gdw.ws1415.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * 
 * Use for anything that is just a sprite without animations. <br>
 * The texture can be easily set with the global available AssetManagerX class. <br>
 * Usage example: assetManager.getTexture("logo") <br>
 * Make sure the texture is <b>not null</b>. Otherwise you will get an assertion error or a NullPointerException.
 * Specify a region if just a certain portion of the texture should be rendered.
 * <br>
 * The textures can be found in the resources/images folder. They should be set/changed in the available json file and loaded
 * at the start of the game.
 */
public class TextureComponent extends Component implements Pool.Poolable {

	public Texture texture;
	public TextureRegion region;
	
	@Override
	public void reset() {
		texture = null;
		region = null;
	}

}
