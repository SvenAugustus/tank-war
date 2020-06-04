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

import com.github.flysium.io.tank.Main;
import com.github.flysium.io.tank.config.AutomaticConfig;
import com.github.flysium.io.tank.config.GameConfig;
import com.github.flysium.io.tank.config.ResourceManager;
import com.github.flysium.io.tank.model.Bullet;
import com.github.flysium.io.tank.model.BulletAttributes;
import com.github.flysium.io.tank.model.Direction;
import com.github.flysium.io.tank.model.Explode;
import com.github.flysium.io.tank.model.FinalRectangle;
import com.github.flysium.io.tank.model.GameObject;
import com.github.flysium.io.tank.model.Group;
import com.github.flysium.io.tank.model.Lifecycle;
import com.github.flysium.io.tank.model.StaticRectangle;
import com.github.flysium.io.tank.model.Tank;
import com.github.flysium.io.tank.model.TankAttributes;
import com.github.flysium.io.tank.model.Wall;
import com.github.flysium.io.tank.service.automatic.AutomaticStrategy;
import com.github.flysium.io.tank.service.automatic.RandomAutomaticStrategy;
import com.github.flysium.io.tank.service.collision.PhysicsCollisionDetectorChain;
import com.github.flysium.io.tank.service.fire.DefaultFireStrategy;
import com.github.flysium.io.tank.service.fire.FireStrategy;
import com.github.flysium.io.tank.service.objectfactory.DefaultGameObjectFactory;
import com.github.flysium.io.tank.service.objectfactory.GameObjectFactory;
import com.github.flysium.io.tank.service.painter.GameObjectPainter;
import com.github.flysium.io.tank.service.painter.SimpleGameObjectPainter;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Game Model with manage all <code>GameObject</code>
 *
 * @author Sven Augustus
 * @version 1.0
 */
public class GameModel {

  private final GameConfig gameConfig = GameConfig.getSingleton();
  private final FinalRectangle bounds;

  private final GameObjectPainter painter;
  private final GameObjectFactory gameObjectFactory;
  private final PhysicsCollisionDetectorChain physicsCollisionDetectorChain = new PhysicsCollisionDetectorChain();

  private Tank mainTank;
  private final Map<String, GameObject> gameObjects = new ConcurrentHashMap<>();
  private transient volatile boolean stop = false;

  public static GameModel getSingleton() {
    return Holder.INSTANCE;
  }

  private GameModel() {
    this(Main.FINAL_RECTANGLE);
  }

  private GameModel(final FinalRectangle bounds) {
    this.bounds = bounds;
    this.painter = newGameObjectPainter(gameConfig.getPainter());
    this.gameObjectFactory = new DefaultGameObjectFactory();

    // init main tank
    this.mainTank = createTank(Group.MAIN_GROUP, 50, 50);
    this.mainTank.arise();

    // init enemy tanks
    int initEnemyTanksCount = gameConfig.getInitEnemyTanksCount();
    if (initEnemyTanksCount > 0) {
      for (int i = 0; i < initEnemyTanksCount; i++) {
        createTank(Group.ENEMY_GROUP, 150 + 100 * i, 80).arise();
      }
    }

    // init walls
    createWall(new StaticRectangle(150, 150, 200, 50)).arise();
    createWall(new StaticRectangle(550, 150, 200, 50)).arise();
    createWall(new StaticRectangle(300, 300, 50, 200)).arise();
    createWall(new StaticRectangle(550, 300, 50, 200)).arise();
  }

  /**
   * new <code>GameObjectPainter</code>.
   *
   * @param clazzName class name of <code>GameObjectPainter</code>.
   * @return <code>GameObjectPainter</code> instance.
   */
  @SuppressWarnings("unchecked")
  private GameObjectPainter newGameObjectPainter(String clazzName) {
    try {
      Class<GameObjectPainter> clazz = (Class<GameObjectPainter>) Class.forName(clazzName);
      return clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return new SimpleGameObjectPainter();
  }

  /**
   * paint <code>Graphics</code>
   *
   * @param g Graphics
   */
  public void paint(Graphics g) {
    // detect and handle any physics collisions
    physicsCollisionDetect(gameObjects.values(), gameObjects.values());

    // paint
    gameObjects.forEach((id, gameObject) -> {
      if (!gameObject.isAlive()) {
        if (gameObject instanceof Tank) {
          Explode explode = createExplode((Tank) gameObject);
          explode.arise();
          ResourceManager.getSingleton().asyncPlayExplodeAudio();
        }
        gameObjects.remove(gameObject.getId());
        return;
      }
      painter.paint(g, gameObject);
    });

    // messages.
    if (!mainTank.isAlive()) {
      painter.paint(g, Color.RED, "You Lose the War !");
      stop = true;
    } else if (gameObjects.values().stream()
        .filter(gameObject -> gameObject instanceof Tank && Group.ENEMY_GROUP
            .equals(gameObject.getGroup())).noneMatch(
            Lifecycle::isAlive)) {
      painter.paint(g, Color.BLUE, "You Win the War !");
      stop = true;
    } else {
      long wallsCount = gameObjects.values().stream().filter(o -> o instanceof Wall).count();
      long tanksCount = gameObjects.values().stream().filter(o -> o instanceof Tank).count();
      long bulletsCount = gameObjects.values().stream().filter(o -> o instanceof Bullet).count();
      painter.paint(g, Color.BLACK, "Walls: " + wallsCount + ", Tanks: " + tanksCount
          + ", Bullets: " + bulletsCount);
    }
  }

  /**
   * Get the main <code>Tank</code>
   *
   * @return the main Tank.
   */
  public Tank getMainTank() {
    return mainTank;
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
   * create a tank
   */
  private Tank createTank(Group group, final int x, final int y) {
    return createTank(group, x, y,
        TankAttributes.builder()
            .initialHealth(Group.MAIN_GROUP.equals(group) ?
                gameConfig.getMainTankInitialHealth()
                : gameConfig.getEnemyTankInitialHealth())
            .initialDirection(Direction.RIGHT)
            .shape(painter.getTankShape(group))
            .bounds(this.bounds)
            .movingSpeed(Group.MAIN_GROUP.equals(group) ?
                gameConfig.getMainTankMovingSpeed()
                : gameConfig.getEnemyTankMovingSpeed())
            .fireStrategy(newFireStrategy(Group.MAIN_GROUP.equals(group) ?
                gameConfig.getMainTankFireStrategy() :
                gameConfig.getEnemyTankFireStrategy()))
            .build());
  }

  /**
   * create a tank
   */
  private Tank createTank(Group group, final int x, final int y, TankAttributes attributes) {
    Tank tank = gameObjectFactory.createTank(group, x, y, attributes);
    gameObjects.putIfAbsent(tank.getId(), tank);
    return tank;
  }

  /**
   * new <code>FireStrategy</code>.
   *
   * @param clazzName class name of <code>FireStrategy</code>.
   * @return <code>FireStrategy</code> instance.
   */
  @SuppressWarnings("unchecked")
  private FireStrategy newFireStrategy(String clazzName) {
    try {
      Class<FireStrategy> clazz = (Class<FireStrategy>) Class.forName(clazzName);
      return clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return new DefaultFireStrategy();
  }

  /**
   * create a explode
   */
  public Explode createExplode(Tank tank) {
    Explode explode = gameObjectFactory.createExplode(tank);
    gameObjects.putIfAbsent(explode.getId(), explode);
    return explode;
  }

  /**
   * create a bullet and make it fly
   */
  public Bullet createBullet(Tank tank, BulletAttributes attributes) {
    Bullet bullet = gameObjectFactory.createBullet(tank,
        attributes.defaultOf(BulletAttributes.builder()
            .shape(painter.getBulletShape(tank.getGroup()))
            .bulletFlyingSpeed(Group.MAIN_GROUP.equals(tank.getGroup()) ?
                gameConfig.getMainTankBulletFlyingSpeed()
                : gameConfig.getEnemyTankBulletFlyingSpeed())
            .damageValue(Group.MAIN_GROUP.equals(tank.getGroup()) ?
                gameConfig.getMainTankBulletDamage() :
                gameConfig.getEnemyTankBulletDamage())
            .build()));
    gameObjects.putIfAbsent(bullet.getId(), bullet);
    return bullet;
  }

  /**
   * create a wall
   */
  public Wall createWall(StaticRectangle rectangle) {
    Wall wall = new Wall(rectangle);
    gameObjects.putIfAbsent(wall.getId(), wall);
    return wall;
  }

  private final AutomaticConfig automaticConfig = AutomaticConfig.getSingleton();
  private final AutomaticStrategy automaticStrategy = newAutomaticStrategy(
      automaticConfig.getEnemyTankStrategy());

  /**
   * automatic make enemies tanks to action (stop, go, fire, etc.)
   */
  public void automatic() {
    if (stop) {
      return;
    }
    gameObjects.values().stream()
        .filter(gameObject -> gameObject instanceof Tank
            && gameObject.isAlive()
            && Group.ENEMY_GROUP.equals(gameObject.getGroup()))
        .forEach(gameObject -> {
          automaticStrategy.automatic((Tank) gameObject);
        });
  }

  /**
   * new <code>AutomaticStrategy</code>.
   *
   * @param clazzName class name of <code>AutomaticStrategy</code>.
   * @return <code>AutomaticStrategy</code> instance.
   */
  @SuppressWarnings("unchecked")
  private AutomaticStrategy newAutomaticStrategy(String clazzName) {
    try {
      Class<AutomaticStrategy> clazz = (Class<AutomaticStrategy>) Class.forName(clazzName);
      return clazz.getConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return new RandomAutomaticStrategy();
  }

  private final File f = new File(System.getProperty("user.home") + "/tankWar.sav");

  /**
   * Load to memory
   */
  @SuppressWarnings("unchecked")
  public void load() {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
      this.mainTank = (Tank) ois.readObject();
      Map<String, GameObject> gameObjects = (Map<String, GameObject>) ois.readObject();
      this.gameObjects.clear();
      this.gameObjects.putAll(gameObjects);
      System.out.println("--------->Game Loaded:" + f.getAbsolutePath());
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Save to disk
   */
  public void save() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
      oos.writeObject(mainTank);
      oos.writeObject(gameObjects);
      System.out.println("--------->Game Saved:" + f.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static class Holder {

    // singleton instance.
    static final GameModel INSTANCE = new GameModel();
  }
}
