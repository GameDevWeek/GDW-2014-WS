package de.hochschuletrier.gdw.ws1415.sandbox.menu;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.hochschuletrier.gdw.commons.gdx.assets.AssetManagerX;
import de.hochschuletrier.gdw.commons.gdx.menu.widgets.DecoImage;
import de.hochschuletrier.gdw.ws1415.Main;

public class MenuPage extends Group {

    protected Main main = Main.getInstance();
    protected AssetManagerX assetManager = main.getAssetManager();
    protected final Skin skin;

    public MenuPage(Skin skin, String background){
        super();
        this.skin = skin;
        
        addActor(new DecoImage(assetManager.getTexture(background)));
        setVisible(false);
    }

    @Override
    public void act(float delta) {
        if(isVisible())
        {
        	super.act(delta);
        }
    }

    @Override
    protected void drawChildren(Batch batch, float parentAlpha) {
        if(clipBegin(0,0 ,getWidth(), getHeight())){
        super.drawChildren(batch, parentAlpha);
        clipEnd();
        }
    }
    
    
    protected final void addCenteredButton(int x, int y, int width, int height, String text, Runnable runnable) {
        TextButton button = addButton(x - width / 2, y - height / 2, width, height, text, runnable, "mainMenu");
        button.getLabel().setAlignment(Align.center);
    }
    
    protected final void addCenteredImage(int x, int y, int width, int height, String bild, Runnable runnable){
       DecoImage image=addImage(x,y,width,height,bild,runnable);
        
         
       
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
    
    protected final DecoImage addImage(int x, int y, int width, int height,String bild,  Runnable runnable){
    DecoImage image=new DecoImage(assetManager.getTexture(bild));
    image.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event,float x, float y) {
            System.out.println("TEST");
            System.exit(-1);
            //runnable.run();
        }
    });
    addActor(image);
    return image;
    
    }
    
    protected final void addLeftAlignedButton(int x, int y, int width, int height, DecoImage button, Runnable runnable) {
        //TextButton button = addButton(x, y, width, height, text, runnable, "mainMenu");
        //button.getLabel().setAlignment(Align.left);
        button.setPosition(x,y);
        addActor(button);
    }
}
