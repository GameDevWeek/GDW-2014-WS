package de.hochschuletrier.gdw.ws1415.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector3;
import de.hochschuletrier.gdw.commons.devcon.DevConsole;
import de.hochschuletrier.gdw.commons.devcon.cvar.CVarEnum;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundDistanceModel;
import de.hochschuletrier.gdw.commons.gdx.audio.SoundEmitter;
import de.hochschuletrier.gdw.ws1415.Main;
import de.hochschuletrier.gdw.ws1415.game.ComponentMappers;
import de.hochschuletrier.gdw.ws1415.game.components.PlayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SoundEmitterComponent;

public class UpdateSoundEmitterSystem extends IteratingSystem {

    private static final CVarEnum<SoundDistanceModel> distanceModel = new CVarEnum("snd_distanceModel", SoundDistanceModel.INVERSE, SoundDistanceModel.class, 0, "sound distance model");
    private static final CVarEnum<SoundEmitter.Mode> emitterMode = new CVarEnum("snd_mode", SoundEmitter.Mode.STEREO, SoundEmitter.Mode.class, 0, "sound mode");
    private ImmutableArray<Entity> players;

    public UpdateSoundEmitterSystem() {
        super(Family.all(PositionComponent.class, SoundEmitterComponent.class).get(), 0);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    public static void initCVars() {
        DevConsole console = Main.getInstance().console;
        console.register(distanceModel);
        distanceModel.addListener((CVar) -> distanceModel.get().activate());

        console.register(emitterMode);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if(players.size() > 0) {
            Entity player = players.get(0);
            PositionComponent pos = ComponentMappers.position.get(player);
            SoundEmitter.setListenerPosition(pos.x, pos.y, 10, emitterMode.get());
        }
        SoundEmitter.updateGlobal();
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        SoundEmitterComponent sound = ComponentMappers.soundEmitter.get(entity);
        PositionComponent position = ComponentMappers.position.get(entity);
        sound.emitter.setPosition(position.x, position.y, 0);
        sound.emitter.update();
    }
}
