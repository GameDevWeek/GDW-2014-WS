package de.hochschuletrier.gdw.ws1415.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

import de.hochschuletrier.gdw.commons.gdx.physix.PhysixBodyDef;
import de.hochschuletrier.gdw.commons.gdx.physix.PhysixFixtureDef;
import de.hochschuletrier.gdw.commons.gdx.physix.components.PhysixBodyComponent;
import de.hochschuletrier.gdw.commons.gdx.physix.systems.PhysixSystem;
import de.hochschuletrier.gdw.commons.utils.Rectangle;
import de.hochschuletrier.gdw.ws1415.game.components.AIComponent;
import de.hochschuletrier.gdw.ws1415.game.components.AnimationComponent;
import de.hochschuletrier.gdw.ws1415.game.components.BlockComponent;
import de.hochschuletrier.gdw.ws1415.game.components.DamageComponent;
import de.hochschuletrier.gdw.ws1415.game.components.HealthComponent;
import de.hochschuletrier.gdw.ws1415.game.components.InputComponent;
import de.hochschuletrier.gdw.ws1415.game.components.JumpComponent;
import de.hochschuletrier.gdw.ws1415.game.components.LayerComponent;
import de.hochschuletrier.gdw.ws1415.game.components.MovementComponent;
import de.hochschuletrier.gdw.ws1415.game.components.PositionComponent;
import de.hochschuletrier.gdw.ws1415.game.components.SpawnComponent;
import de.hochschuletrier.gdw.ws1415.game.components.TriggerComponent;
import de.hochschuletrier.gdw.ws1415.game.utils.EventBoxType;

public class EntityCreator {

    public static Entity createAndAddPlayer(float x, float y, PooledEngine engine,
            PhysixSystem physixSystem, float width, float height) {
        Entity entity = engine.createEntity();

        entity.add(engine.createComponent(AnimationComponent.class));
        entity.add(engine.createComponent(PositionComponent.class));
        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(SpawnComponent.class));
        entity.add(engine.createComponent(InputComponent.class));

        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.DynamicBody,
                physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        bodyComponent.getBody().setUserData(bodyComponent);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(0).restitution(0.1f).shapeBox(width, height);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(bodyComponent);
        entity.add(bodyComponent);

        JumpComponent jumpComponent = engine.createComponent(JumpComponent.class);
        jumpComponent.jumpImpulse = 20000.0f;
        jumpComponent.restingTime = 0.02f;
        entity.add(jumpComponent);

        MovementComponent moveComponent = engine.createComponent(MovementComponent.class);
        moveComponent.speed = 10000.0f;
        entity.add(moveComponent);

        BlockComponent blockComp = engine.createComponent(BlockComponent.class);
        entity.add(blockComp);

        engine.addEntity(entity);
        return entity;
    }

    public static Entity createAndAddEnemy(PhysixSystem physixSystem, float x, float y, float rotation, PooledEngine engine) {
        Entity entity = engine.createEntity();

        entity.add(engine.createComponent(DamageComponent.class));
        entity.add(engine.createComponent(AIComponent.class));
        entity.add(engine.createComponent(AnimationComponent.class));
        entity.add(engine.createComponent(PositionComponent.class));
        entity.add(engine.createComponent(SpawnComponent.class));

        PhysixBodyComponent pbc = new PhysixBodyComponent();
        PhysixBodyDef pbdy = new PhysixBodyDef(BodyDef.BodyType.DynamicBody, physixSystem).position(x, y).fixedRotation(true);
        PhysixFixtureDef pfx = new PhysixFixtureDef(physixSystem).density(1).friction(1f).shapeBox(10, 10).restitution(0.1f);
        Fixture fixture = pbc.createFixture(pfx);
        fixture.setUserData(pbdy);
        pbc.init(pbdy, physixSystem, entity);

        entity.add(pbc);

        engine.addEntity(entity);
        return entity;
    }

    public static Entity createAndAddEventBox(EventBoxType type, float x, float y, PooledEngine engine) {
        Entity box = engine.createEntity();

        box.add(engine.createComponent(TriggerComponent.class));
        box.add(engine.createComponent(PositionComponent.class));

        engine.addEntity(box);
        return box;
    }

    public static Entity createAndAddInvulnerableFloor(PooledEngine engine, PhysixSystem physixSystem, Rectangle rect, int tileWidth, int tileHeight) {
        float width = rect.width * tileWidth;
        float height = rect.height * tileHeight;
        float x = rect.x * tileWidth + width / 2;
        float y = rect.y * tileHeight + height / 2;

        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody,
                physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f).shapeBox(width, height)
                .restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);

        entity.add(bodyComponent);

        BlockComponent blockComp = engine.createComponent(BlockComponent.class);
        entity.add(blockComp);

        engine.addEntity(entity);
        return entity;
    }

    public static Entity createAndAddVulnerableFloor(PooledEngine engine, PhysixSystem physixSystem, float x, float y, float width, float height) {
        Entity entity = engine.createEntity();

        PhysixBodyComponent bodyComponent = engine
                .createComponent(PhysixBodyComponent.class);
        PhysixBodyDef bodyDef = new PhysixBodyDef(BodyDef.BodyType.StaticBody,
                physixSystem).position(x, y).fixedRotation(true);
        bodyComponent.init(bodyDef, physixSystem, entity);
        PhysixFixtureDef fixtureDef = new PhysixFixtureDef(physixSystem)
                .density(1).friction(1f).shapeBox(width, height)
                .restitution(0.1f);
        Fixture fixture = bodyComponent.createFixture(fixtureDef);
        fixture.setUserData(entity);
        entity.add(bodyComponent);

        BlockComponent blockComp = engine.createComponent(BlockComponent.class);
        entity.add(blockComp);

        HealthComponent Health = engine.createComponent(HealthComponent.class);
        Health.Value = 0;
        entity.add(Health);

        engine.addEntity(entity);
        return entity;

    }
}
