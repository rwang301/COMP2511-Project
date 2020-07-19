package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public class Boulder extends Entity implements Blockable {

    public Boulder(int x, int y) {
        super(x, y);
    }

    /**
     * Check if the player can push the boulder in the required direction
     * If so, move boulder one square in that direction otherwise do nothing
     * @param player
     * @param direction direction the boulder is to be pushed
     */
    public void push(Player player, String direction) {
        if (direction.equals("right")) {
            if (player.hasEntity((getX() + 1), getY())) return;
            untrigger(player);
            x().set(getX() + 1);
        } else if (direction.equals("down")) {
            if (player.hasEntity(getX(), (getY() + 1))) return;
            untrigger(player);
            y().set(getY() + 1);
        } else if (direction.equals("left")) {
            if (player.hasEntity((getX() - 1), getY())) return;
            untrigger(player);
            x().set(getX() - 1);
        } else {
            if (player.hasEntity(getX(), (getY() - 1))) return;
            untrigger(player);
            y().set(getY() - 1);
        }
        trigger(player);
    }

    /**
     * If the boulder is on a floor switch before being pushed then untrigger the floor switch
     * @param player
     */
    private boolean untrigger(Player player) {
        for (Entity floorSwitch: player.getEntities(Switch.class)) {
            if (this.isOn(floorSwitch)) {
                ((Switch)floorSwitch).setTriggered();
                return true;
            }
        }
        return false;
    }

    /**
     * If the boulder is on a floor switch after being pushed then trigger the floor switch
     * and check for the goal of having a boulder on all floor switches
     * @param player
     */
    private void trigger(Player player) {
        if (untrigger(player)) {
            player.complete();
        }
    }

    @Override
    public void block(Player player, IntegerProperty coordinate, int position) {
        player.setPosition(coordinate, position);
    }
    
}
