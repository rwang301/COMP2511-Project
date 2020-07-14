package unsw.dungeon;

import javafx.beans.property.IntegerProperty;

public class Boulder extends Entity implements Blockable {

    public Boulder(int x, int y) {
        super(x, y);
    }

    /**
     * Check if the player can push the boulder in the required direction
     * If so, move boulder one sqaure in that direction otherwise do nothing
     * @param player
     * @param direction direction the boulder is to be pushed
     */
    public void push(Player player, String direction) {
        if (direction.equals("right")) {
            //check square to the right for entity
            if (player.hasEntity((getX() + 1), getY())) return;
            x().set(getX() + 1);
        } else if (direction.equals("down")) {
            if (player.hasEntity(getX(), (getY() + 1))) return;
        } else if (direction.equals("left")) {
            if (player.hasEntity((getX() - 1), getY())) return;
            x().set(getX() - 1);
        } else {
            if (player.hasEntity(getX(), (getY() - 1))) return;
            y().set(getY() - 1);
        }
        return;
    }

    @Override
    public void block(Player player, IntegerProperty coordinate, int position) {
        player.setPosition(coordinate, position);
    }
    
}