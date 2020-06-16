/*
 * Copyright (c) 2020 SvenAugustus
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.flysium.io.tank.service;

import com.github.flysium.io.remoting.ProtocolMessage;
import com.github.flysium.io.remoting.ServerChildHandler;
import com.github.flysium.io.remoting.SimpleNettyServer;
import com.github.flysium.io.serialization.SerializerUtils;
import com.github.flysium.io.tank.config.GameConfig;
import com.github.flysium.io.tank.config.ResourceManager;
import com.github.flysium.io.tank.config.ServerConfig;
import com.github.flysium.io.tank.model.Bullet;
import com.github.flysium.io.tank.model.BulletAttributes;
import com.github.flysium.io.tank.model.Direction;
import com.github.flysium.io.tank.model.Explode;
import com.github.flysium.io.tank.model.FinalRectangle;
import com.github.flysium.io.tank.model.Flyable;
import com.github.flysium.io.tank.model.GameObject;
import com.github.flysium.io.tank.model.Group;
import com.github.flysium.io.tank.model.Movable;
import com.github.flysium.io.tank.model.StaticRectangle;
import com.github.flysium.io.tank.model.Tank;
import com.github.flysium.io.tank.model.TankAttributes;
import com.github.flysium.io.tank.model.Wall;
import com.github.flysium.io.tank.remoting.TankMessageType;
import com.github.flysium.io.tank.remoting.client.ClientLoginMessage;
import com.github.flysium.io.tank.remoting.client.TankFireMessage;
import com.github.flysium.io.tank.remoting.client.TankMoveMessage;
import com.github.flysium.io.tank.remoting.server.GameInitializationMessage;
import com.github.flysium.io.tank.remoting.server.NewObjectMessage;
import com.github.flysium.io.tank.remoting.server.ObjectDieMessage;
import com.github.flysium.io.tank.remoting.server.ObjectStatus;
import com.github.flysium.io.tank.remoting.server.StatusUpdateMessage;
import com.github.flysium.io.tank.service.collision.PhysicsCollisionDetectorChain;
import com.github.flysium.io.tank.service.objectfactory.DefaultGameObjectFactory;
import com.github.flysium.io.tank.service.objectfactory.GameObjectFactory;
import com.github.flysium.io.tank.view.shaper.GameObjectShaper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Game Model with manage all <code>GameObject</code>
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class TankServerService {

  private static final Logger logger = LoggerFactory.getLogger(TankServerService.class);

  private final GameConfig gameConfig = GameConfig.getSingleton();
  private final ServerConfig serverConfig = ServerConfig.getSingleton();
  private final FinalRectangle bounds = gameConfig.getFinalRectangle();
  private final GameObjectShaper shaper = GameObjectShaper
      .newGameObjectShaper(gameConfig.getShaper());
  private final GameObjectFactory gameObjectFactory = new DefaultGameObjectFactory();
  private final PhysicsCollisionDetectorChain physicsCollisionDetectorChain = new PhysicsCollisionDetectorChain();

  private final SimpleNettyServer server = new SimpleNettyServer(serverConfig.getPort(),
      new TankServerChildHandler());

  private final Map<String, GameObject> gameObjects = new ConcurrentHashMap<>();
  private final Map<String, ObjectStatus> lastPositions = new ConcurrentHashMap<>();

  public TankServerService() {
    // init walls
    createWall(new StaticRectangle(150, 150, 200, 50));
    createWall(new StaticRectangle(550, 150, 200, 50));
    createWall(new StaticRectangle(300, 300, 50, 200));
    createWall(new StaticRectangle(550, 300, 50, 200));
  }

  /**
   * Start
   */
  public void start() throws InterruptedException {
    new Thread(() -> {
      while (true) {
        try {
          TimeUnit.MILLISECONDS.sleep(ServerConfig.getSingleton().getRefreshMillis());
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        // refresh
        refresh();
      }
    }, "refresh").start();

    this.server.start();
  }

  /**
   * refresh
   */
  public void refresh() {
    // detect and handle any physics collisions
    physicsCollisionDetect(gameObjects.values(), gameObjects.values());

    List<ObjectStatus> update = new ArrayList<>();
    // paint
    this.gameObjects.forEach((id, o) -> {
      if (!o.isAlive()) {
        if (o instanceof Tank) {
          Explode explode = createExplode((Tank) o);
          publishNewObjectMessage(null, explode);
        }
        gameObjects.remove(o.getId());
        lastPositions.remove(o.getId());
        publishGameObjectDieMessage(o);
      }
      if (o instanceof Explode) {
        Explode explode = (Explode) o;
        if (explode.next() >= ResourceManager.getSingleton().getExplodeImage().size()) {
          explode.die();
        }
      }
      if (o instanceof Flyable) {
        ((Flyable) o).fly();
      }
      ObjectStatus readyToUpdate = getReadyToUpdate(o);
      if (readyToUpdate != null) {
        update.add(readyToUpdate);
      }
    });
    publishStatusUpdateMessages(update);
  }


  /**
   * detect and handle any physics collisions
   */
  private void physicsCollisionDetect(Collection<? extends GameObject> as,
      Collection<? extends GameObject> bs) {
    for (GameObject a : as) {
      for (GameObject b : bs) {
        if (a != b) {
          if (physicsCollisionDetectorChain.detect(a, b)) {
            break;
          }
        }
      }
    }
  }

  /**
   * return ${@link ObjectStatus} if it's possible to update
   */
  private ObjectStatus getReadyToUpdate(GameObject o) {
    ObjectStatus p = lastPositions.get(o.getId());
    ObjectStatus np = ObjectStatus.builder()
        .id(o.getId())
        .direction(o instanceof Movable ? ((Movable) o).getDirection() : null)
        .x(o.getLocation().x)
        .y(o.getLocation().y)
        .health(o.getHealthValue())
        .build();
    if (p == null || !p.equals(np)) {
      lastPositions.put(o.getId(), np);
      return np;
    }
    return null;
  }

  /**
   * create a tank
   */
  private Tank createTank(final String username, final String userId) {
    int x = 50;
    int y = 50;
    Tank tank = gameObjectFactory.createTank(x, y,
        TankAttributes.builder()
            .ownerUsername(username)
            .ownerUserId(userId)
            .initialHealth(serverConfig.getTankInitialHealth())
            .initialDirection(Direction.RIGHT)
            .shape(shaper.getTankShape())
            .bounds(this.bounds)
            .movingSpeed(serverConfig.getTankMovingSpeed())
            .build());
    while (true) {
      boolean physicsCollision = false;
      for (GameObject a : gameObjects.values()) {
        if (a != tank && physicsCollisionDetectorChain.detect(a, tank, false)) {
          physicsCollision = true;
          break;
        }
      }
      if (!physicsCollision) {
        break;
      }
      Random random = new Random();
      x = this.bounds.x + random.nextInt(this.bounds.width - this.bounds.x);
      y = this.bounds.y + random.nextInt(this.bounds.height - this.bounds.y);
      tank.setLocation(tank.getDirection(), x, y);
    }
    tank.setGroup(new Group(tank.getOwnerUsername()));
    gameObjects.putIfAbsent(tank.getId(), tank);
    return tank;
  }

  /**
   * create a explode
   */
  private Explode createExplode(Tank tank) {
    Explode explode = gameObjectFactory.createExplode(tank);
    gameObjects.putIfAbsent(explode.getId(), explode);
    return explode;
  }

  /**
   * create a bullet and make it fly
   */
  private Bullet createBullet(Tank tank) {
    Bullet bullet = gameObjectFactory.createBullet(tank,
        BulletAttributes.builder()
            .shape(shaper.getBulletShape())
            .bulletFlyingSpeed(serverConfig.getTankBulletFlyingSpeed())
            .damageValue(serverConfig.getTankBulletDamage())
            .build());
    gameObjects.putIfAbsent(bullet.getId(), bullet);
    return bullet;
  }

  /**
   * create a wall
   */
  private Wall createWall(StaticRectangle rectangle) {
    Wall wall = new Wall(rectangle);
    gameObjects.putIfAbsent(wall.getId(), wall);
    return wall;
  }

  private void publishNewObjectMessage(String userId, GameObject o) {
    try {
      NewObjectMessage msg = NewObjectMessage.builder()
          .ownerUserId(userId).object(o)
          .build();
      logger.debug("--------->New Object: {}", msg);
      server.sendMessageToAllChannels(msg.convertToProtocolMessage());
    } catch (Exception e) {
      logger.error("new object error", e);
    }
  }

  private void publishNewObjectMessageExcept(String channelId, String userId, GameObject o) {
    try {
      NewObjectMessage msg = NewObjectMessage.builder()
          .ownerUserId(userId).object(o)
          .build();
      logger.debug("--------->New Object: {}", msg);
      server.sendMessageToChannelsExcept(channelId, msg.convertToProtocolMessage());
    } catch (Exception e) {
      logger.error("new object error", e);
    }
  }

  private void publishGameObjectDieMessage(GameObject o) {
    try {
      ObjectDieMessage msg = ObjectDieMessage.builder()
          .id(o.getId())
          .type(o.getClass().getSimpleName())
          .build();
      logger.debug("--------->Game Object Die: {}", msg);
      server.sendMessageToAllChannels(msg.convertToProtocolMessage());
    } catch (Exception e) {
      logger.error("object die error", e);
    }
  }

  private void publishStatusUpdateMessages(List<ObjectStatus> update) {
    if (!update.isEmpty()) {
      try {
        StatusUpdateMessage msg = new StatusUpdateMessage(update);
        logger.debug("--------->Status Update: {}", msg);
        server.sendMessageToAllChannels(msg.convertToProtocolMessage());
      } catch (Exception e) {
        logger.error("status update error", e);
      }
    }
  }

  class TankServerChildHandler implements ServerChildHandler {

    private final Map<String, String> username2UserId = new ConcurrentHashMap<>();
    private final Map<String, String> channel2UserId = new ConcurrentHashMap<>();
    private final Map<String, String> user2TankId = new ConcurrentHashMap<>();

    @Override
    public void channelRead(String channelId, ProtocolMessage message) {
      TankMessageType type = TankMessageType.values()[message.getType()];
      switch (type) {
        case CLIENT_LOGIN:
          login(channelId, message);
          break;
        case CLIENT_MOVE:
          move(channelId, message);
          break;
        case CLIENT_FIRE:
          fire(channelId, message);
          break;
        default:
          break;
      }
    }

    @Override
    public void channelInactive(String channelId) {
      String userId = channel2UserId.get(channelId);
      if (userId != null) {
        channel2UserId.remove(channelId);
      }
    }

    private void login(String channelId, ProtocolMessage message) {
      GameInitializationMessage response;
      // login
      try {
        ClientLoginMessage request = SerializerUtils.fromBytes(message.getBody(),
            ClientLoginMessage.class);
        logger.debug("<---------Login request: {}", request);
        String username = request.getUsername();
        String password = request.getPassword();
        // TODO authority
        if (!"123456".equals(password)) {
          logger.error("login: {} failed, password invalid.", username);
          return;
        }
        String userId = getUserId(username);
        channel2UserId.putIfAbsent(channelId, userId);
        Tank tank = createTank(username, userId);
        user2TankId.putIfAbsent(userId, tank.getId());
        response = GameInitializationMessage.builder()
            .success(true)
            .userId(userId)
            .tank(tank)
            .objects(new ArrayList<>(gameObjects.values()))
            .build();
      } catch (Exception e) {
        logger.error("login in error", e);
        response = GameInitializationMessage.builder()
            .success(false)
            .error("Fail to login in.")
            .build();
      }
      try {
        logger.debug("--------->Game Initialization: {}", response);
        // response
        server.sendMessageToChannel(channelId, response.convertToProtocolMessage());
        // broadcast
        publishNewObjectMessageExcept(channelId, response.getUserId(), response.getTank());
      } catch (Exception e) {
        logger.error("login response error", e);
      }
    }

    private void move(String channelId, ProtocolMessage message) {
      try {
        TankMoveMessage msg = SerializerUtils.fromBytes(message.getBody(),
            TankMoveMessage.class);
        logger.debug("<---------Move: {}", msg);
        if (notLogin(channelId, msg.getUserId())) {
          logger.warn("validate fail.");
          return;
        }
        Tank tank = getTank(msg.getUserId());
        if (tank == null) {
          return;
        }
        tank.changeDirection(msg.getDirection());
        tank.moveOn();
      } catch (Exception e) {
        logger.error("move error", e);
      }
    }

    private void fire(String channelId, ProtocolMessage message) {
      try {
        TankFireMessage msg = SerializerUtils.fromBytes(message.getBody(),
            TankFireMessage.class);
        logger.debug("<---------Fire: {}", msg);
        if (notLogin(channelId, msg.getUserId())) {
          logger.warn("validate fail.");
          return;
        }
        Tank tank = getTank(msg.getUserId());
        if (tank == null) {
          return;
        }
        Bullet bullet = TankServerService.this.createBullet(tank);
        // broadcast
        publishNewObjectMessage(msg.getUserId(), bullet);
      } catch (Exception e) {
        logger.error("fire error", e);
      }
    }

    private Tank getTank(String userId) {
      String tankId = user2TankId.get(userId);
      GameObject object = gameObjects.get(tankId);
      if (object == null) {
        logger.warn("Tank: {} is not found.", tankId);
        return null;
      }
      if (!(object instanceof Tank)) {
        logger.warn("Object: {} is not Tank.", tankId);
        return null;
      }
      return (Tank) object;
    }

    protected boolean notLogin(String channelId, String userId) {
      return userId == null || !userId.equals(channel2UserId.get(channelId));
    }

    protected String getUserId(String username) {
      String userId = username2UserId.get(username);
      if (userId == null) {
        synchronized (this) {
          userId = username2UserId.get(username);
          if (userId == null) {
            userId = UUID.randomUUID().toString();
            username2UserId.putIfAbsent(username, userId);
          }
        }
      }
      return userId;
    }
  }

}
