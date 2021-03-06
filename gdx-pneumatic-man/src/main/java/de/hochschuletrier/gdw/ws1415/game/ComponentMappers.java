package de.hochschuletrier.gdw.ws1415.game;

import com.badlogic.ashley.core.ComponentMapper;

import de.hochschuletrier.gdw.commons.gdx.physix.components.*;
import de.hochschuletrier.gdw.ws1415.game.components.*;
import de.hochschuletrier.gdw.ws1415.game.components.lights.ChainLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.ConeLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.DirectionalLightComponent;
import de.hochschuletrier.gdw.ws1415.game.components.lights.PointLightComponent;

public class ComponentMappers {
    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<TriggerComponent> trigger = ComponentMapper.getFor(TriggerComponent.class);
    public static final ComponentMapper<PhysixBodyComponent> physixBody = ComponentMapper.getFor(PhysixBodyComponent.class);
    public static final ComponentMapper<PhysixModifierComponent> physixModifier = ComponentMapper.getFor(PhysixModifierComponent.class);
    public static final ComponentMapper<ImpactSoundComponent> impactSound = ComponentMapper.getFor(ImpactSoundComponent.class);
    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<MovementComponent> movement = ComponentMapper.getFor(MovementComponent.class);
    public static final ComponentMapper<BouncingComponent> bouncing = ComponentMapper.getFor(BouncingComponent.class);
    public static final ComponentMapper<KillsPlayerOnContactComponent> killsPlayerOnContact = ComponentMapper.getFor(KillsPlayerOnContactComponent.class);
    public static final ComponentMapper<AIComponent> AI = ComponentMapper.getFor(AIComponent.class);
    public static final ComponentMapper<HealthComponent> health = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<DestructableBlockComponent> block = ComponentMapper.getFor(DestructableBlockComponent.class);
    public static final ComponentMapper<IndestructableBlockComponent> iblock = ComponentMapper.getFor(IndestructableBlockComponent.class);
    public static final ComponentMapper<LayerComponent> layer = ComponentMapper.getFor(LayerComponent.class);
    public static final ComponentMapper<TextureComponent> texture = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<JumpComponent> jump = ComponentMapper.getFor(JumpComponent.class);
    public static final ComponentMapper<InputComponent> input = ComponentMapper.getFor(InputComponent.class);
    public static final ComponentMapper<ParticleComponent> particle = ComponentMapper.getFor(ParticleComponent.class);
    public static final ComponentMapper<FallingRockComponent> rockTraps = ComponentMapper.getFor(FallingRockComponent.class);
    public static final ComponentMapper<PointLightComponent>  pointLight = ComponentMapper.getFor(PointLightComponent.class);
    public static final ComponentMapper<DirectionalLightComponent>  directionalLight = ComponentMapper.getFor(DirectionalLightComponent.class);
    public static final ComponentMapper<ChainLightComponent>  chainLight = ComponentMapper.getFor(ChainLightComponent.class);
    public static final ComponentMapper<ConeLightComponent>  coneLight = ComponentMapper.getFor(ConeLightComponent.class);
    public static final ComponentMapper<LavaFountainComponent> lavaFountain = ComponentMapper.getFor(LavaFountainComponent.class);
    public static final ComponentMapper<LavaBallComponent> lavaBall = ComponentMapper.getFor(LavaBallComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<PlatformComponent> platform = ComponentMapper.getFor(PlatformComponent.class);
    public static final ComponentMapper<SpikeComponent> spikes = ComponentMapper.getFor(SpikeComponent.class);
    
    public static final ComponentMapper<BombComponent> bomb = ComponentMapper.getFor(BombComponent.class);
    public static final ComponentMapper<SoundEmitterComponent> soundEmitter = ComponentMapper.getFor(SoundEmitterComponent.class);
    public static final ComponentMapper<DeathTimerComponent> deathTimer = ComponentMapper.getFor(DeathTimerComponent.class);
    public static final ComponentMapper<DirectionComponent> direction = ComponentMapper.getFor(DirectionComponent.class);
    
    public static final ComponentMapper<DamageComponent> damage = ComponentMapper.getFor(DamageComponent.class);

}
