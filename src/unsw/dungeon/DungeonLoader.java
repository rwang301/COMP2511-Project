package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;

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
public abstract class DungeonLoader {

    private JSONObject json;
    private Portal portal = null;
    private Key key = null;
    private Door door = null;

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
            Portal portal = new Portal(x, y);
            onLoad(portal);
            entity = portal;
            if (this.portal != null) {
                this.portal.setMatchingPortal(portal);
                portal.setMatchingPortal(this.portal);
                this.portal = null;
            } else {
                this.portal = portal;
            }
            break;
        case "exit":
            Exit exit = new Exit(x, y);
            onLoad(exit);
            entity = exit;
            break;
        case "key":
            Key key = new Key(x, y);
            onLoad(key);
            entity = key;
            if (this.door != null) {
                key.setDoor(this.door);
                this.door.setKey(key);
                this.door = null;
            } else {
                this.key = key;
            }
            break;
        case "door":
            Door door = new Door(x, y);
            onLoad(door);
            entity = door;
            if (this.key != null) {
                door.setKey(this.key);
                this.key.setDoor(door);
                this.key = null;
            } else {
                this.door = door;
            }
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

    // TODO Create additional abstract methods for the other entities

}
