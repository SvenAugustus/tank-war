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

import com.github.flysium.io.remoting.ClientHandler;
import com.github.flysium.io.remoting.ProtocolMessage;
import com.github.flysium.io.remoting.SimpleNettyClient;
import com.github.flysium.io.serialization.SerializerUtils;
import com.github.flysium.io.tank.config.AudioManager;
import com.github.flysium.io.tank.config.ClientConfig;
import com.github.flysium.io.tank.config.GameConfig;
import com.github.flysium.io.tank.model.Bullet;
import com.github.flysium.io.tank.model.DefaultExplode;
import com.github.flysium.io.tank.model.Direction;
import com.github.flysium.io.tank.model.Explode;
import com.github.flysium.io.tank.model.FinalRectangle;
import com.github.flysium.io.tank.model.GameObject;
import com.github.flysium.io.tank.model.Group;
import com.github.flysium.io.tank.model.Movable;
import com.github.flysium.io.tank.model.Tank;
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
import com.github.flysium.io.tank.service.painter.GameObjectPainter;
import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Game Model with manage all <code>GameObject</code>
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class TankClientService {

  private static final Logger logger = LoggerFactory.getLogger(TankClientService.class);

  private final GameConfig gameConfig = GameConfig.getSingleton();
  private final ClientConfig clientConfig = ClientConfig.getSingleton();
  private final FinalRectangle bounds = gameConfig.getFinalRectangle();
  private final GameObjectPainter painter = GameObjectPainter
      .newGameObjectPainter(clientConfig.getPainter());

  private final SimpleNettyClient client = new SimpleNettyClient(clientConfig.getHost(),
      clientConfig.getPort(), new TankClientHandler());
  private final String username;
  private volatile String userId;

  private volatile Tank myTank;
  private final Map<String, GameObject> gameObjects = new ConcurrentHashMap<>();
  private transient volatile boolean gameOver = false;

  public TankClientService(String username) {
    this.username = username;
  }

  /**
   * Start
   */
  public void start() throws InterruptedException {
    this.client.start();
  }

  /**
   * initialize
   */
  private void initialize(Tank tank, List<GameObject> objects) {
    if (tank == null) {
      return;
    }
    this.myTank = tank;
    this.myTank.setGroup(Group.MY_GROUP);
    if (objects != null) {
      this.gameObjects.clear();
      objects.stream().filter(object -> !object.getId().equals(myTank.getId()))
          .forEach(object -> this.gameObjects.putIfAbsent(object.getId(), object));
    }
    this.gameObjects.putIfAbsent(myTank.getId(), myTank);
  }

  /**
   * paint <code>Graphics</code>
   *
   * @param g Graphics
   */
  public void paint(Graphics g) {
    // paint
    this.gameObjects.forEach((id, gameObject) -> {
      if (!gameObject.isAlive()) {
        if (gameObject instanceof Tank) {
          Explode explode = new DefaultExplode((Tank) gameObject);
          gameObjects.putIfAbsent(explode.getId(), explode);
          AudioManager.getSingleton().asyncPlayExplodeAudio();
        }
        gameObjects.remove(gameObject.getId());
        return;
      }
      painter.paint(g, gameObject);
    });

    // messages.
    if (myTank != null && !myTank.isAlive()) {
      painter.paint(g, Color.RED, "You Lose the War !");
      gameOver = true;
    } else {
      long wallsCount = gameObjects.values().stream().filter(o -> o instanceof Wall).count();
      long tanksCount = gameObjects.values().stream().filter(o -> o instanceof Tank).count();
      long bulletsCount = gameObjects.values().stream().filter(o -> o instanceof Bullet).count();
      painter.paint(g, Color.BLACK, "Walls: " + wallsCount + ", Tanks: " + tanksCount
          + ", Bullets: " + bulletsCount);
    }
  }

  /**
   * Moves the main <code>Tank</code> by the specified <code>Direction</code>.
   *
   * @param direction <code>Direction</code>
   */
  public void moveMainTankByDirection(Direction direction) {
    if (gameOver) {
      return;
    }
    try {
      TankMoveMessage msg = TankMoveMessage.builder()
          .userId(userId)
          .direction(direction)
          .build();
      logger.debug("--------->Move: {}", msg);
      this.client.sendMessage(msg.convertToProtocolMessage());
    } catch (Exception e) {
      logger.error("send to server error", e);
    }
    AudioManager.getSingleton().asyncPlayMoveAudio();
  }

  /**
   * Fire the main <code>Tank</code>
   */
  public void fireMainTank() {
    if (gameOver) {
      return;
    }
    try {
      TankFireMessage msg = TankFireMessage.builder()
          .userId(userId)
          .build();
      logger.debug("--------->Fire: {}", msg);
      this.client.sendMessage(msg.convertToProtocolMessage());
    } catch (Exception e) {
      logger.error("send to server error", e);
    }
    AudioManager.getSingleton().asyncPlayFireAudio();
  }

  class TankClientHandler implements ClientHandler {

    @Override
    public void channelActive() {
      try {
        ClientLoginMessage msg = ClientLoginMessage.builder()
            .username(username)
            .password("123456")
            .build();
        logger.debug("--------->Login request: {}", msg);
        client.sendMessage(msg.convertToProtocolMessage());
      } catch (Exception e) {
        logger.error("login in error", e);
      }
    }

    @Override
    public void channelRead(ProtocolMessage message) {
      try {
        TankMessageType type = TankMessageType.values()[message.getType()];
        switch (type) {
          case INITIALIZATION:
            handle(SerializerUtils.fromBytes(message.getBody(), GameInitializationMessage.class));
            break;
          case NEW_OBJECT:
            handle(SerializerUtils.fromBytes(message.getBody(), NewObjectMessage.class));
            break;
          case OBJECT_DIE:
            handle(SerializerUtils.fromBytes(message.getBody(), ObjectDieMessage.class));
            break;
          case STATUS_UPDATE:
            handle(SerializerUtils.fromBytes(message.getBody(), StatusUpdateMessage.class));
            break;
          default:
            break;
        }
      } catch (Exception e) {
        logger.error("system error", e);
      }
    }

    protected void handle(GameInitializationMessage msg) {
      logger.debug("<---------Game Initialization: {}", msg);
      userId = msg.getUserId();
      TankClientService.this.initialize(msg.getTank(), msg.getObjects());
    }

    protected void handle(NewObjectMessage msg) {
      logger.debug("<---------New Object: {}", msg);
      GameObject object = msg.getObject();
      TankClientService.this.gameObjects.putIfAbsent(object.getId(), object);
    }

    protected void handle(ObjectDieMessage msg) {
      logger.debug("<---------Game Object Die: {}", msg);
      String id = msg.getId();
      if (TankClientService.this.myTank.getId().equals(id)) {
        gameOver = true;
      }
      TankClientService.this.gameObjects.remove(id);
    }

    protected void handle(StatusUpdateMessage msg) {
      logger.debug("<---------Position Update: {}", msg);
      List<ObjectStatus> update = msg.getList();
      if (update == null) {
        return;
      }
      update.forEach(u -> {
        GameObject object = TankClientService.this.gameObjects.get(u.getId());
        if (object == null) {
          return;
        }
        if (object instanceof Movable) {
          ((Movable) object).setLocation(u.getDirection(), u.getX(), u.getY());
        }
        object.injureBy(Math.abs(object.getHealthValue() - u.getHealth()));
      });
    }
  }

}
