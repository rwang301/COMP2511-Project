package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

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
    private HashMap<Integer, Portal> portals = new HashMap<>();
    private HashMap<Integer, Key> keys = new HashMap<>();
    private HashMap<Integer, Door> doors = new HashMap<>();

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
        }
        dungeon.addEntity(entity);
    }

    public abstract void onLoad(Entity player);

    public abstract void onLoad(Wall wall);

    public abstract void onLoad(Portal portal);

    public abstract void onLoad(Exit exit);

    public abstract void onLoad(Key key);

    public abstract void onLoad(Door door);

    public abstract void onLoad(Boulder boulder);

    public abstract void onLoad(Switch floorSwitch);

    // TODO Create additional abstract methods for the other entities

}
