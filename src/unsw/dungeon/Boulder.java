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
    void push(Player player, String direction) {
        if (direction.equals("up")) {
            if (player.hasEntity(getX(), (getY() - 1))) return;
            setTrigger(player, y(), getY() - 1, -1);
        } else if (direction.equals("down")) {
            if (player.hasEntity(getX(), (getY() + 1))) return;
            setTrigger(player, y(), getY() + 1, -1);
        } else if (direction.equals("left")) {
            if (player.hasEntity((getX() - 1), getY())) return;
            setTrigger(player, x(), getX() - 1, -1);
        } else if (direction.equals("right")) {
            if (player.hasEntity((getX() + 1), getY())) return;
            setTrigger(player, x(), getX() + 1, -1);
        }
        setTrigger(player, null, 0, 1);
    }

    /**
     * Trigger or untrigger a switch
     * @param player
     * @param coordinate
     * @param position
     * @param trigger 1 to trigger a switch -1 to untrigger it
     */
    private void setTrigger(Player player, IntegerProperty coordinate, int position, int trigger) {
        for (Entity floorSwitch: player.getEntities(Switch.class)) {
            if (this.isOn(floorSwitch)) {
                ((Switch) floorSwitch).setTriggered();
                if (trigger == 1) player.complete();
                player.setTriggers(trigger);
                break;
            }
        }
        if (trigger == -1) setPosition(coordinate, position);
    }
}
