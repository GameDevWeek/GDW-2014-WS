package de.hochschuletrier.gdw.ws1415.game.menu;

import java.awt.MouseInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.commons.gdx.utils.DrawUtil;
import de.hochschuletrier.gdw.ws1415.Main;

public class MenuPage extends Group {

    protected Main main = Main.getInstance();
    protected AssetManagerX assetManager = main.getAssetManager();
    protected final Skin skin;
    public static boolean abspielen=true;
//    protected final Texture overlay = new Texture(Gdx.files.internal("data/images/background_overlay.png"));
//    private float x = (Gdx.graphics.getWidth() - overlay.getWidth())/2;
//	private float y = (Gdx.graphics.getHeight() - overlay.getHeight())/2;
    // final DecoImage overlay = new DecoImage(assetManager.getTexture("background_overlay"));

    public MenuPage(Skin skin, String background) {
        super();
        this.skin = skin;
        //DrawUtil.draw(overlay, x, y);
        addActor(new DecoImage(assetManager.getTexture(background)));
        

        setVisible(false);
    }

    @Override
    public void act(float delta) {
        if (isVisible()) {
            super.act(delta);
        }
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        if (clipBegin(0, 0, getWidth(), getHeight())) {
            super.drawChildren(batch, parentAlpha);
            clipEnd();
        }
    }

    protected final void addLeftAlignedButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x, y, width, height, text, runnable, "mainMenu");
        button.getLabel().setAlignment(Align.left);
    }

    protected final void addCenteredButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x - width / 2, y - height / 2, width, height, text, runnable, "mainMenu");
        button.getLabel().setAlignment(Align.center);
    }

    protected final void addCenteredImage(int x, int y, int width, int height, DecoImage image, Runnable runnable)
    {
        image.setPosition(x,y);
        image.setTouchable(Touchable.enabled);
        abspielen=false;

        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float width, float height) {
                //System.out.println("kjfghkd");
                abspielen=false;
                if(image.getRegion().getTexture()!=assetManager.getTexture("back_button")){
                    SoundEmitter.updateGlobal();
                    SoundEmitter.playGlobal(assetManager.getSound("pmAccept"),false);
                }else{
                    SoundEmitter.updateGlobal();
                    SoundEmitter.playGlobal(assetManager.getSound("pmCancel"),false); 
                }
               
                runnable.run();
            }
            
        });
        
        image.addListener(new InputListener(){

            @Override
            public void enter(InputEvent event, float x, float y, int pointer,
                    Actor fromActor) {
                    if(abspielen==true){
                        //System.out.println("abspielen");
                        SoundEmitter.updateGlobal();
                    SoundEmitter.playGlobal(assetManager.getSound("pmHover"),false);}
                    changeTextureActive(image);
                //}
            }
            
        });
         image.addListener(new InputListener(){

            @Override
            public void exit(InputEvent event, float x, float y, int pointer,
                    Actor toActor) {
                abspielen=true;

                changeTextureNotActive( image);
            }
             
         });

        addActor(image);
    }
    protected final TextButton addButton(int x, int y, int width, int height, String text, Runnable runnable, String style) {
        TextButton button = new TextButton(text, skin, style);
        button.setBounds(x, y, width, height);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                runnable.run();
            }
        });
        addActor(button);
        return button;
    }
    
    protected final ImageButton addImageButton(int x, int y, int width, int height, Runnable runnable)
    {
    	ImageButton button = new ImageButton(skin);
    	button.setBounds(x, y, width, height);
    	button.addListener(new ClickListener()
    	{
    		@Override
            public void clicked(InputEvent event, float x, float y) {
                runnable.run();
            }
        });
        addActor(button);
        return button;
    }
    
    protected final  void changeTextureActive(DecoImage image)
    {
        if(image.getRegion().getTexture()==assetManager.getTexture("optionen_button"))
            image.setTexture(assetManager.getTexture("optionen_button_active"));
        if(image.getRegion().getTexture()==assetManager.getTexture("start_button"))
            image.setTexture(assetManager.getTexture("start_button_active"));
        if(image.getRegion().getTexture()==assetManager.getTexture("levels_button"))
            image.setTexture(assetManager.getTexture("levels_button_active"));
        if(image.getRegion().getTexture()==assetManager.getTexture("score_button"))
            image.setTexture(assetManager.getTexture("score_button_active"));
        if(image.getRegion().getTexture()==assetManager.getTexture("credits_button"))
            image.setTexture(assetManager.getTexture("credits_button_active"));
    	
    }
    
    protected final void addImage(int x, int y, int width, int height, DecoImage dI){
        DecoImage image = dI;
    	image.setBounds(x, y, width, height);
    	addActor(image);
    }

    protected final  void changeTextureNotActive(DecoImage image)
    {
        if(image.getRegion().getTexture()==assetManager.getTexture("optionen_button_active"))
            image.setTexture(assetManager.getTexture("optionen_button"));
        if(image.getRegion().getTexture()==assetManager.getTexture("start_button_active"))
            image.setTexture(assetManager.getTexture("start_button"));
        if(image.getRegion().getTexture()==assetManager.getTexture("levels_button_active"))
            image.setTexture(assetManager.getTexture("levels_button"));
        if(image.getRegion().getTexture()==assetManager.getTexture("score_button_active"))
            image.setTexture(assetManager.getTexture("score_button"));
        if(image.getRegion().getTexture()==assetManager.getTexture("credits_button_active"))
            image.setTexture(assetManager.getTexture("credits_button"));
    }
}
