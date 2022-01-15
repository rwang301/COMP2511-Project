package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Loads a dungeon from a .json file.
 *
 * By extending this class, a subclass can hook into entity creation. This is
 * useful for creating UI elements with corresponding entities.
 *
 * @author Robert Clifton-Everest
 *
 */
public abstract class DungeonLoader implements Observer{

    private JSONObject json;
    private Component goal;
    private int totalTreasure = 0;
    private HashMap<Integer, Portal> portals = new HashMap<>();
    private HashMap<Integer, Key> keys = new HashMap<>();
    private HashMap<Integer, Door> doors = new HashMap<>();
    private List<Enemy> enemies = new ArrayList<>();

    public DungeonLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("dungeons/" + filename)));
    }

    /**
     * Parses the JSON to create a dungeon.
     * @return
     */
    public Dungeon load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        Dungeon dungeon = new Dungeon(width, height);
        dungeon.attach(this);

        JSONArray jsonEntities = json.getJSONArray("entities");

        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(dungeon, jsonEntities.getJSONObject(i));
        }

        loadGoals(json.getJSONObject("goal-condition"), null);
        dungeon.setGoal(this.goal);
        dungeon.setTotalTreasure(this.totalTreasure);
        for (Enemy e : this.enemies) {
            dungeon.getPlayer().attach(e);
        }

        return dungeon;
    }

    private void loadEntity(Dungeon dungeon, JSONObject json) {
        String type = json.getString("type");
        int x = json.getInt("x");
        int y = json.getInt("y");

        Entity entity = null;
        switch (type) {
        case "player":
            Player player = new Player(dungeon, x, y);
            dungeon.setPlayer(player);
            onLoad(player);
            entity = player;
            break;
        case "wall":
            Wall wall = new Wall(x, y);
            onLoad(wall);
            entity = wall;
            break;
        // TODO Handle other possible entities
        case "portal":
            Integer portalId = json.getInt("id");
            Portal portal = new Portal(x, y);
            onLoad(portal);
            entity = portal;
            if (portals.containsKey(portalId)) {
                portals.get(portalId).setMatchingPortal(portal);
                portal.setMatchingPortal(portals.get(portalId));
            } else {
                portals.put(portalId, portal);
            }
            break;
        case "exit":
            Exit exit = new Exit(x, y);
            onLoad(exit);
            entity = exit;
            break;
        case "key":
            Integer keyId = json.getInt("id");
            Key key = new Key(x, y);
            onLoad(key);
            entity = key;
            if (doors.containsKey(keyId)) {
                doors.get(keyId).setKey(key);
                key.setDoor(doors.get(keyId));
            } 
            keys.put(keyId, key);
            break;
        case "door":
            Integer doorId = json.getInt("id");
            Door door = new Door(x, y);
            onLoad(door);
            entity = door;
            if (keys.containsKey(doorId)) {
                keys.get(doorId).setDoor(door);
                door.setKey(keys.get(doorId));
            } 
            doors.put(doorId, door);
            break;
        case "boulder":
            Boulder boulder = new Boulder(x, y);
            onLoad(boulder);
            entity = boulder;
            break;
        case "switch":
            Switch floorSwitch = new Switch(x, y);
            onLoad(floorSwitch);
            entity = floorSwitch;
            break;
        case "treasure":
            Treasure treasure = new Treasure(x, y);
            onLoad(treasure);
            entity = treasure;
            this.totalTreasure++;
            break;
        case "enemy":
            Enemy enemy = new Enemy(x, y);
            onLoad(enemy);
            entity = enemy;
            enemies.add(enemy);
            break;
        case "sword":
            Sword sword = new Sword(x, y);
            onLoad(sword);
            entity = sword;
            break;
        case "invincibility":
            Potion potion = new Potion(x, y);
            onLoad(potion);
            entity = potion;
            break;
        }
        dungeon.addEntity(entity);
    }

    private void loadGoals(JSONObject goals, Composite condition) {
        String goal = goals.getString("goal");
        switch (goal) {
            case "OR":
                Composite or = new Or(); 
                if (condition == null) {
                    this.goal = or;
                } else {
                    condition.add(or);
                }
                for (int i = 0; i < goals.getJSONArray("subgoals").length(); i++) {
                    loadGoals(goals.getJSONArray("subgoals").getJSONObject(i), or);
                }
                break;
            case "AND":
                Composite and = new And();
                if (condition == null) {
                    this.goal = and;
                } else {
                    condition.add(and);
                }
                for (int i = 0; i < goals.getJSONArray("subgoals").length(); i++) {
                    loadGoals(goals.getJSONArray("subgoals").getJSONObject(i), and);
                }
                break;
            default:
                Leaf leaf = null;
                switch (goal) {
                    case "exit":
                        leaf = new GoalExit(); 
                        break;
                    case "boulders":
                        leaf = new GoalBoulders(); 
                        break;
                    case "treasure":
                        leaf = new GoalTreasure(); 
                        break;
                    case "enemies":
                        leaf = new GoalEnemies(); 
                        break;
                }
                if (condition == null) this.goal = leaf;
                else condition.add(leaf);
                break;
        }
    }

    public abstract void onLoad(Entity player);

    public abstract void onLoad(Wall wall);

    public abstract void onLoad(Portal portal);

    public abstract void onLoad(Exit exit);

    public abstract void onLoad(Key key);

    public abstract void onLoad(Door door);

    public abstract void onLoad(Boulder boulder);

    public abstract void onLoad(Switch floorSwitch);

    public abstract void onLoad(Treasure treasure);
    
    public abstract void onLoad(Potion potion);

    public abstract void onLoad(Sword sword);

    public abstract void onLoad(Enemy Enemy);

    // TODO Create additional abstract methods for the other entities

}
